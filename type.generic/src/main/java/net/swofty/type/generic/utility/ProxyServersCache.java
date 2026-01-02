package net.swofty.type.generic.utility;

import net.swofty.commons.UnderstandableProxyServer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class ProxyServersCache {
    private ProxyServersCache() {}

    private static final Map<String, UUID> nameToUuid = new ConcurrentHashMap<>();
    private static volatile List<String> suggestions = List.of();
    private static volatile long lastRefreshMs = 0;

    public static List<String> getSuggestions() {
        return suggestions;
    }

    public static UUID resolve(String input) {
        if (input == null) return null;
        return nameToUuid.get(input.toLowerCase(Locale.ROOT));
    }

    public static boolean shouldRefresh(long ttlMs) {
        return (System.currentTimeMillis() - lastRefreshMs) > ttlMs;
    }

    public static void update(List<UnderstandableProxyServer> servers) {
        Map<String, UUID> newMap = new HashMap<>();
        List<String> newSuggestions = new ArrayList<>();

        for (UnderstandableProxyServer s : servers) {
            if (s == null) continue;

            UUID uuid = s.uuid();
            if (uuid == null) continue;

            String shortName = s.shortName();
            String name = s.name();

            if (shortName != null && !shortName.isBlank()) {
                newMap.put(shortName.toLowerCase(Locale.ROOT), uuid);
                newSuggestions.add(shortName);
            }
            if (name != null && !name.isBlank()) {
                newMap.put(name.toLowerCase(Locale.ROOT), uuid);
                newSuggestions.add(name);
            }
        }

        // de-dupe + sort (short ones first)
        newSuggestions = newSuggestions.stream()
                .distinct()
                .sorted(Comparator.comparingInt(String::length).thenComparing(String::compareToIgnoreCase))
                .collect(Collectors.toList());

        nameToUuid.clear();
        nameToUuid.putAll(newMap);
        suggestions = newSuggestions;
        lastRefreshMs = System.currentTimeMillis();
    }
}
