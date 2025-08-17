package net.swofty.type.generic.user;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public record PlayerHookManager(HypixelPlayer player, Map<Class, Map<Consumer<HypixelPlayer>, Boolean>> hooks) {
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
}
