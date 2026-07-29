package net.swofty.dungeons.catacombs.flow;

import java.time.Instant;

public record CatacombsJoinTrace(CatacombsJoinStep step, Instant happenedAt, String detail) {
    public static CatacombsJoinTrace of(CatacombsJoinStep step, String detail) {
        return new CatacombsJoinTrace(step, Instant.now(), detail);
    }
}
