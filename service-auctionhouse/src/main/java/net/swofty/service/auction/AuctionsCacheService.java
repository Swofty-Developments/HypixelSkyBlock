package net.swofty.service.auction;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mongodb.client.model.Filters;
import net.swofty.commons.auctions.AuctionsFilter;
import org.bson.Document;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AuctionsCacheService {
    private final Cache<String, Object> cache;

    public AuctionsCacheService() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();
    }

    public Object getAuctions(String categoryFilter, AuctionsFilter filter) {
        // Check the cache first
        Object result = cache.getIfPresent(categoryFilter + "-" + filter);
        if (result != null) {
            return result; // Return cached result
        }

        // If not in cache, fetch from MongoDB and store in cache
        List<Document> documents = getDocuments(categoryFilter);

        switch (filter) {
            case SHOW_ALL:
                result = documents;
                break;
            case AUCTIONS_ONLY:
                result = documents.stream().filter(document -> document.getString("type").equals("auction")).findFirst().orElse(null);
                break;
            case BIN_ONLY:
                result = documents.stream().filter(document -> document.getString("type").equals("bin")).findFirst().orElse(null);
                break;
        }

        cache.put(categoryFilter + "-" + filter, result);
        return result;
    }

    public List<Document> getDocuments(String category) {
        Object result = cache.getIfPresent(category);
        if (result != null) {
            return (List<Document>) result;
        }

        List<Document> documents = AuctionActiveDatabase.collection.find(Filters.eq("category", category)).into(List.of());
        cache.put(category, documents);

        return documents;
    }
}
