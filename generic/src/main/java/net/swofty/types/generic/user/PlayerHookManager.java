package net.swofty.types.generic.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public record PlayerHookManager(SkyBlockPlayer player, Map<Hooks, List<Consumer<SkyBlockPlayer>>> hooks) {
    public void registerHook(Hooks hook, Consumer<SkyBlockPlayer> consumer) {
        if (!hooks.containsKey(hook)) {
            hooks.put(hook, new ArrayList<>());
        }
        hooks.get(hook).add(consumer);
    }

    public void callAndClearHooks(Hooks hook) {
        if (!hooks.containsKey(hook)) {
            return;
        }
        hooks.get(hook).forEach(consumer -> consumer.accept(player));
        hooks.get(hook).clear();
    }

    public enum Hooks {
        BEFORE_DATA_SAVE,
        AFTER_DATA_SAVE,
    }
}
