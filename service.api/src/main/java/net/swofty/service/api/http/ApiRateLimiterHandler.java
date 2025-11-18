package net.swofty.service.api.http;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import net.swofty.service.api.APIKeyDatabase;
import net.swofty.service.api.APIKeyDatabaseObject;
import org.bson.Document;
import org.tinylog.Logger;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles rate limiting for API keys based on daily request limits.
 * Uses an in-memory cache to track requests per day with MongoDB persistence.
 */
public class ApiRateLimiterHandler {
    private static final String COLLECTION_NAME = "api-request-counts";
    private static final ZoneId UTC = ZoneId.of("UTC");

    // Static instance for singleton access
    private static ApiRateLimiterHandler instance;

    // In-memory cache for request counts (apiKey -> requestTracker)
    private final ConcurrentHashMap<String, RequestTracker> requestCache = new ConcurrentHashMap<>();

    // MongoDB collection for persistent storage
    private MongoCollection<Document> collection;

    /**
     * Private constructor for singleton pattern
     */
    private ApiRateLimiterHandler() {
        // Initialize MongoDB collection reference
        collection = APIKeyDatabase.database.getCollection(COLLECTION_NAME);

        // Load existing request counts from database
        loadRequestCountsFromDatabase();
    }

    /**
     * Get singleton instance of ApiRateLimiterHandler
     */
    public static synchronized ApiRateLimiterHandler getInstance() {
        if (instance == null) {
            instance = new ApiRateLimiterHandler();
        }
        return instance;
    }

    /**
     * Inner class to track request counts for a given API key
     */
    @Getter
    private static class RequestTracker {
        private int dailyCount;
        private LocalDate date;

        public RequestTracker() {
            this.dailyCount = 0;
            this.date = LocalDate.now(UTC);
        }

        public RequestTracker(int dailyCount, LocalDate date) {
            this.dailyCount = dailyCount;
            this.date = date;
        }

        /**
         * Increments the daily count and checks if limit is exceeded
         * @param limit The maximum allowed requests per day
         * @return true if request is allowed, false if limit exceeded
         */
        public boolean incrementAndCheck(long limit) {
            LocalDate now = LocalDate.now(UTC);

            // Reset counter if it's a new day
            if (!date.equals(now)) {
                dailyCount = 0;
                date = now;
            }

            // Check if adding another request would exceed the limit
            if (dailyCount >= limit) {
                return false;
            }

            // Increment the counter
            dailyCount++;
            return true;
        }
    }

    /**
     * Load request counts from database into memory cache
     */
    private void loadRequestCountsFromDatabase() {
        try {
            LocalDate today = LocalDate.now(UTC);
            String dateKey = today.toString();

            // Only load today's counts
            FindIterable<Document> documents = collection.find(
                    Filters.eq("date", dateKey)
            );

            for (Document doc : documents) {
                String apiKey = doc.getString("apiKey");
                int count = doc.getInteger("count", 0);

                // Add to memory cache
                requestCache.put(apiKey, new RequestTracker(count, today));
            }
        } catch (Exception e) {
            Logger.error(e, "Failed to load API request counts from database");
        }
    }

    /**
     * Check if a request for the given API key is allowed based on daily limit
     * @param apiKey The API key
     * @return true if request is allowed, false if rate limit exceeded
     */
    public boolean isRequestAllowed(String apiKey) {
        // Get or create the API key database object
        APIKeyDatabase keyDatabase = new APIKeyDatabase(apiKey);
        APIKeyDatabaseObject keyObject = keyDatabase.fetch();

        if (keyObject == null) {
            return false; // Invalid API key
        }

        // Get the daily request limit
        long dailyLimit = keyObject.getRequestsPerDay();

        // Get or create a request tracker for this API key
        RequestTracker tracker = requestCache.computeIfAbsent(apiKey, k -> new RequestTracker());

        // Check if adding another request would exceed the limit
        boolean allowed = tracker.incrementAndCheck(dailyLimit);

        // If allowed, store the updated count
        if (allowed) {
            updateRequestCountInDatabase(apiKey, tracker);
        }

        return allowed;
    }

    /**
     * Update the request count in MongoDB
     */
    private void updateRequestCountInDatabase(String apiKey, RequestTracker tracker) {
        try {
            String dateKey = tracker.getDate().toString();
            String docId = apiKey + "_" + dateKey;

            Document document = new Document(Map.of(
                    "_id", docId,
                    "apiKey", apiKey,
                    "date", dateKey,
                    "count", tracker.getDailyCount()
            ));

            // Using upsert to handle both insert and update cases
            collection.replaceOne(
                    Filters.eq("_id", docId),
                    document,
                    new ReplaceOptions().upsert(true)
            );
        } catch (Exception e) {
            Logger.error(e, "Failed to update API request count for key: {}", apiKey);
        }
    }

    /**
     * Get remaining requests for an API key for the current day
     * @param apiKey The API key
     * @return Number of remaining requests, or -1 if API key is invalid
     */
    public long getRemainingRequests(String apiKey) {
        APIKeyDatabase keyDatabase = new APIKeyDatabase(apiKey);
        APIKeyDatabaseObject keyObject = keyDatabase.fetch();

        if (keyObject == null) {
            return -1; // Invalid API key
        }

        long dailyLimit = keyObject.getRequestsPerDay();
        RequestTracker tracker = requestCache.get(apiKey);

        if (tracker == null) {
            return dailyLimit; // No requests made yet
        }

        // Reset if it's a new day
        LocalDate today = LocalDate.now(UTC);
        if (!tracker.getDate().equals(today)) {
            return dailyLimit;
        }

        return Math.max(0, dailyLimit - tracker.getDailyCount());
    }

    /**
     * Get all request counts for all API keys
     * @return List of documents containing request count data
     */
    public List<Document> getAllRequestCounts() {
        List<Document> result = new ArrayList<>();
        FindIterable<Document> documents = collection.find();

        for (Document doc : documents) {
            result.add(doc);
        }

        return result;
    }

    /**
     * Get request counts for a specific API key
     * @param apiKey The API key
     * @return List of documents containing request count data for the API key
     */
    public List<Document> getRequestCountsForKey(String apiKey) {
        List<Document> result = new ArrayList<>();
        FindIterable<Document> documents = collection.find(
                Filters.eq("apiKey", apiKey)
        );

        for (Document doc : documents) {
            result.add(doc);
        }

        return result;
    }

    /**
     * Reset all request counts for a specific API key
     * @param apiKey The API key to reset
     */
    public void resetCountsForKey(String apiKey) {
        // Remove from cache
        requestCache.remove(apiKey);

        // Remove from database
        collection.deleteMany(Filters.eq("apiKey", apiKey));
    }
}