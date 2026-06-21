package net.swofty.type.island.lifecycle;

import net.swofty.type.skyblockgeneric.structure.structures.IslandPortal;
import net.swofty.type.skyblockgeneric.user.island.IslandLifecycleContext;
import net.swofty.type.skyblockgeneric.user.island.IslandLifecyclePhase;
import net.swofty.type.skyblockgeneric.user.island.IslandLifecycleStep;

public class PortalBuildStep implements IslandLifecycleStep {
    @Override
    public IslandLifecyclePhase phase() {
        return IslandLifecyclePhase.LOAD;
    }

    @Override
    public int order() {
        return 20;
    }

    @Override
    public void run(IslandLifecycleContext context) {
        IslandPortal portal = new IslandPortal(0, 6, 100, 43);
        portal.setType(IslandPortal.PortalType.HUB);
        portal.build(context.island().getIslandInstance());
    }
}
