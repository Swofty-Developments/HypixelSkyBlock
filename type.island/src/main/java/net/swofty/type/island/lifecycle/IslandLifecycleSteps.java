package net.swofty.type.island.lifecycle;

import net.swofty.type.skyblockgeneric.user.island.IslandLifecycle;

public final class IslandLifecycleSteps {
    private static boolean registered = false;

    private IslandLifecycleSteps() {
    }

    public static void register() {
        if (registered) return;

        IslandLifecycle.register(new JerryDefaultsStep());
        IslandLifecycle.register(new StarterChestStep());
        IslandLifecycle.register(new FirstMinionStep());
        IslandLifecycle.register(new JerryLoadStep());
        IslandLifecycle.register(new MinionLoadStep());
        IslandLifecycle.register(new PortalBuildStep());
        IslandLifecycle.register(new MinionSaveStep());
        IslandLifecycle.register(new JerrySaveStep());

        registered = true;
    }
}
