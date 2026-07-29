package net.swofty.dungeons.catacombs.boss.state;

import java.time.Instant;

public record BossFightTrace(String phaseId, BossFightEvent event, Instant happenedAt, String detail) {
    public static BossFightTrace of(String phaseId, BossFightEvent event, String detail) {
        return new BossFightTrace(phaseId, event, Instant.now(), detail);
    }
}
