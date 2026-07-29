package net.swofty.dungeons.catacombs.boss;

import java.util.List;

public record CatacombsBossEncounter(
        String id,
        String displayName,
        String status,
        List<CatacombsBossPhase> phases
) {
    public CatacombsBossEncounter {
        phases = List.copyOf(phases);
    }
}
