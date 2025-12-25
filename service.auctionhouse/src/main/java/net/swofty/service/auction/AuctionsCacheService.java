package net.swofty.service.auction;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mongodb.client.model.Filters;
import net.swofty.commons.skyblock.auctions.AuctionsFilter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AuctionsCacheService {
    private final Cache<String, Object> cache;

    public AuctionsCacheService() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();
    }

    public List<Document> getAuctions(String categoryFilter, AuctionsFilter filter) {
        // Check the cache first
        List<Document> result = (List<Document>) cache.getIfPresent(categoryFilter + "-" + filter);
        if (result != null) {
            return result; // Return cached result
        }

        // If not in cache, fetch from MongoDB and store in cache
        List<Document> documents = getDocuments(categoryFilter);

        documents.forEach(document -> {
            if (document.getLong("end") < System.currentTimeMillis()) {
                AuctionInactiveDatabase.collection.insertOne(document);
                AuctionActiveDatabase.collection.deleteOne(Filters.eq("_id", document.getString("_id")));
            }
        });

        result = switch (filter) {
            case SHOW_ALL -> documents;
            case AUCTIONS_ONLY -> documents.stream().filter(document -> document.getBoolean("bin").equals(false)).toList();
            case BIN_ONLY -> documents.stream().filter(document -> document.getBoolean("bin").equals(true)).toList();
        };

        cache.put(categoryFilter + "-" + filter, result);
        return result;
    }

    public List<Document> getDocuments(String category) {
        Object result = cache.getIfPresent(category);
        if (result != null) {
            return (List<Document>) result;
        }

        ArrayList<Document> documents = new ArrayList<>(
                AuctionActiveDatabase.collection.find(Filters.eq("category", category.toUpperCase())).into(new ArrayList<>()));
        cache.put(category, documents);

        return documents;
    }
}
