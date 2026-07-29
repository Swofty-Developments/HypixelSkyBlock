package net.swofty.type.skyblockgeneric.slayer;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public record SlayerQuest(
    SlayerType type,
    SlayerTier tier,
    long startedAt,
    int combatXp,
    boolean bossSpawned,
    @Nullable UUID bossUuid
) {
    public SlayerQuest addCombatXp(int amount, int requiredCombatXp) {
        return new SlayerQuest(type, tier, startedAt, Math.min(requiredCombatXp, combatXp + amount), false, null);
    }

    public SlayerQuest markBossSpawned(UUID bossUuid) {
        return new SlayerQuest(type, tier, startedAt, combatXp, true, bossUuid);
    }
}
