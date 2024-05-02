package net.swofty.types.generic.user;

import net.swofty.types.generic.event.SkyBlockEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public record PlayerHookManager(SkyBlockPlayer player, Map<Class, Map<Consumer<SkyBlockPlayer>, Boolean>> hooks) {
    public void registerHook(Class hook, Consumer<SkyBlockPlayer> consumer, boolean before) {
        if (hooks.containsKey(hook)) {
            hooks.get(hook).put(consumer, before);
        } else {
            hooks.put(hook, new HashMap<>());
            hooks.get(hook).put(consumer, before);
        }
    }

    public void hookBefore(Class hook, Consumer<SkyBlockPlayer> consumer) {
        registerHook(hook, consumer, false);
    }

    public void hookAfter(Class hook, Consumer<SkyBlockPlayer> consumer) {
        registerHook(hook, consumer, true);
    }

    public void callAndClearHooks(Class hook, boolean before) {
        hooks.forEach((skyBlockEvent, consumerMap) -> {
            if (hook == skyBlockEvent) {
                consumerMap.forEach((consumer, isBefore) -> {
                    if (isBefore == before) {
                        consumer.accept(player);
                    }
                });
            }
        });
    }
}
