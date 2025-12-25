package net.swofty.type.generic.leaderboard;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.user.HypixelPlayer;
import org.bson.Document;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerNameCache {

	// In-memory cache with expiration tracking
	private static final Map<UUID, CachedName> cache = new ConcurrentHashMap<>();
	private static final long CACHE_DURATION_MS = 60 * 60 * 1000; // 1 hour

	private record CachedName(String name, long cachedAt) {
		boolean isExpired() {
			return System.currentTimeMillis() - cachedAt > CACHE_DURATION_MS;
		}
	}

	public static String getUsername(UUID uuid) {
		if (uuid == null) return "Unknown";

		// 1. Check if player is online
		Player onlinePlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
		if (onlinePlayer != null) {
			String name = onlinePlayer.getUsername();
			cache.put(uuid, new CachedName(name, System.currentTimeMillis()));
			return name;
		}

		// 2. Check in-memory cache
		CachedName cached = cache.get(uuid);
		if (cached != null && !cached.isExpired()) {
			return cached.name();
		}

		// 3. Query MongoDB (this is synchronous, consider async for performance)
		try {
			Document doc = UserDatabase.collection.find(new org.bson.Document("_id", uuid.toString())).first();
			if (doc != null) {
				// Try to get username from various possible fields
				String name = null;
				if (doc.containsKey("username")) {
					name = doc.getString("username");
				} else if (doc.containsKey("ign")) {
					name = doc.getString("ign");
				}

				if (name != null) {
					cache.put(uuid, new CachedName(name, System.currentTimeMillis()));
					return name;
				}
			}
		} catch (Exception e) {
			// Ignore, fall through to fallback
		}

		// 4. Fallback to shortened UUID
		return uuid.toString().substring(0, 8);
	}

	public static void cacheUsername(UUID uuid, String username) {
		if (uuid != null && username != null) {
			cache.put(uuid, new CachedName(username, System.currentTimeMillis()));
		}
	}

	public static void invalidate(UUID uuid) {
		cache.remove(uuid);
	}

	public static void clearExpired() {
		cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
	}
}
