package net.swofty.dungeons.catacombs.boss;

import java.util.Set;

public record CatacombsBossPhase(
        String id,
        String displayName,
        BossPhaseTrigger trigger,
        int triggerValue,
        Set<String> mechanics
) {
    public CatacombsBossPhase {
        mechanics = Set.copyOf(mechanics);
    }
}
