package net.swofty.type.island.lifecycle;

import net.swofty.type.skyblockgeneric.minion.IslandMinionData;
import net.swofty.type.skyblockgeneric.user.island.IslandLifecycleContext;
import net.swofty.type.skyblockgeneric.user.island.IslandLifecyclePhase;
import net.swofty.type.skyblockgeneric.user.island.IslandLifecycleStep;

public class MinionSaveStep implements IslandLifecycleStep {
    @Override
    public IslandLifecyclePhase phase() {
        return IslandLifecyclePhase.SAVE;
    }

    @Override
    public void run(IslandLifecycleContext context) {
        IslandMinionData minionData = context.island().getMinionData();
        minionData.getMinions().forEach(IslandMinionData.IslandMinion::removeMinion);
        context.island().getDatabase().insertOrUpdate("minions", minionData.serialize());
    }
}
