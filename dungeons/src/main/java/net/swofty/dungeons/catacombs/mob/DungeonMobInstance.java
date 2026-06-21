package net.swofty.dungeons.catacombs.mob;

import java.util.UUID;

public final class DungeonMobInstance {
    private final UUID id = UUID.randomUUID();
    private final DungeonMobDefinition definition;
    private long health;

    public DungeonMobInstance(DungeonMobDefinition definition) {
        this.definition = definition;
        this.health = definition.health();
    }

    public UUID id() {
        return id;
    }

    public DungeonMobDefinition definition() {
        return definition;
    }

    public long health() {
        return health;
    }

    public boolean dead() {
        return health <= 0;
    }

    public void damage(long amount) {
        health = Math.max(0, health - amount);
    }
}
