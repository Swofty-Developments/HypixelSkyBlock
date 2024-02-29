package net.swofty.service.bazaar;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

public class BazaarCacheService {
    private final Cache<String, Object> cache;

    public BazaarCacheService() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build();
    }

    public Document getItem(String itemKey) {
        Object result = cache.getIfPresent(itemKey);
        if (result != null) {
            return (Document) result;
        }

        Document document = new BazaarDatabase(itemKey).getDocument();
        cache.put(itemKey, document);
        return document;
    }

    public void setItem(String itemKey, Document document) {
        cache.put(itemKey, document);

        new BazaarDatabase(itemKey).setDocument(document);
    }

    public void invalidateCache(String itemKey) {
        cache.invalidate(itemKey);
    }

    public boolean isEmpty() {
        return cache.estimatedSize() == 0;
    }
}
