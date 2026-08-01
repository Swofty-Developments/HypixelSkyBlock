package net.swofty.proxyapi;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Short-lived player documents handed directly from one game server to another.
 */
public final class PlayerTransferDataCache {
    private static final long TTL_MILLIS = 30_000;
    private static final Map<UUID, Entry> CACHE = new ConcurrentHashMap<>();

    private PlayerTransferDataCache() {
    }

    /**
     * RedisAPI serialises request payloads as JSON. BSON JSON can contain control
     * characters in nested item data, so documents cross that boundary as Base64.
     */
    public static String encodeDocument(String document) {
        return Base64.getEncoder().encodeToString(document.getBytes(StandardCharsets.UTF_8));
    }

    static void put(UUID uuid, String encodedAccountDocument, String encodedProfileDocument) {
        CACHE.put(uuid, new Entry(
                decodeDocument(encodedAccountDocument),
                encodedProfileDocument == null ? null : decodeDocument(encodedProfileDocument),
                System.currentTimeMillis() + TTL_MILLIS));
    }

    private static String decodeDocument(String encodedDocument) {
        return new String(Base64.getDecoder().decode(encodedDocument), StandardCharsets.UTF_8);
    }

    public static String takeAccountDocument(UUID uuid) {
        Entry entry = validEntry(uuid);
        if (entry == null) return null;
        if (entry.profileDocument() == null) CACHE.remove(uuid, entry);
        return entry.accountDocument();
    }

    public static String takeProfileDocument(UUID uuid) {
        Entry entry = validEntry(uuid);
        if (entry == null) return null;
        CACHE.remove(uuid, entry);
        return entry.profileDocument();
    }

    private static Entry validEntry(UUID uuid) {
        Entry entry = CACHE.get(uuid);
        if (entry == null) return null;
        if (entry.expiresAt() < System.currentTimeMillis()) {
            CACHE.remove(uuid, entry);
            return null;
        }
        return entry;
    }

    private record Entry(String accountDocument, String profileDocument, long expiresAt) {
    }
}
