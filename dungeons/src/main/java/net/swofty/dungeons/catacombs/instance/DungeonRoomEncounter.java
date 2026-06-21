package net.swofty.dungeons.catacombs.instance;

import net.swofty.dungeons.catacombs.mob.DungeonMobDefinition;
import net.swofty.dungeons.catacombs.mob.DungeonMobInstance;

import java.util.ArrayList;
import java.util.List;

public final class DungeonRoomEncounter {
    private final int roomId;
    private final List<DungeonMobInstance> mobs = new ArrayList<>();
    private boolean keyDropped;

    public DungeonRoomEncounter(int roomId, List<DungeonMobDefinition> definitions) {
        this.roomId = roomId;
        definitions.forEach(definition -> mobs.add(new DungeonMobInstance(definition)));
    }

    public int roomId() {
        return roomId;
    }

    public List<DungeonMobInstance> mobs() {
        return List.copyOf(mobs);
    }

    public boolean cleared() {
        return mobs.stream().allMatch(DungeonMobInstance::dead);
    }

    public boolean keyDropped() {
        return keyDropped;
    }

    public void damageMob(int index, long amount) {
        mobs.get(index).damage(amount);
        if (cleared()) {
            keyDropped = true;
        }
    }
}
