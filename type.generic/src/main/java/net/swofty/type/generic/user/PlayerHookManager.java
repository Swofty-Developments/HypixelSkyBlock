package net.swofty.type.generic.user;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public record PlayerHookManager(HypixelPlayer player, Map<Class, Map<Consumer<HypixelPlayer>, Boolean>> hooks) {
    // Storage for temporary string data (like teleport targets)
    private static final Map<HypixelPlayer, Map<String, Object>> temporaryData = new HashMap<>();
    public void registerHook(Class hook, Consumer<HypixelPlayer> consumer, boolean before) {
        if (hooks.containsKey(hook)) {
            hooks.get(hook).put(consumer, before);
        } else {
            hooks.put(hook, new HashMap<>());
            hooks.get(hook).put(consumer, before);
        }
    }

    public void hookBefore(Class hook, Consumer<HypixelPlayer> consumer) {
        registerHook(hook, consumer, false);
    }

    public void hookAfter(Class hook, Consumer<HypixelPlayer> consumer) {
        registerHook(hook, consumer, true);
    }

    public void callAndClearHooks(Class hook, boolean before) {
        hooks.forEach((hypixelEvent, consumerMap) -> {
            if (hook == hypixelEvent) {
                consumerMap.forEach((consumer, isBefore) -> {
                    if (isBefore == before) {
                        consumer.accept(player);
                    }
                });
            }
        });
    }

    /**
     * Set a temporary hook value (e.g., for cross-server teleport targets)
     */
    public void setHook(String key, Object value) {
        temporaryData.computeIfAbsent(player, k -> new HashMap<>()).put(key, value);
    }

    /**
     * Get a temporary hook value
     */
    public Object getHook(String key) {
        Map<String, Object> playerData = temporaryData.get(player);
        return playerData != null ? playerData.get(key) : null;
    }

    /**
     * Remove a temporary hook value
     */
    public void removeHook(String key) {
        Map<String, Object> playerData = temporaryData.get(player);
        if (playerData != null) {
            playerData.remove(key);
            if (playerData.isEmpty()) {
                temporaryData.remove(player);
            }
        }
    }

    /**
     * Clear all temporary hook values for this player
     */
    public void clearHooks() {
        temporaryData.remove(player);
    }
}
