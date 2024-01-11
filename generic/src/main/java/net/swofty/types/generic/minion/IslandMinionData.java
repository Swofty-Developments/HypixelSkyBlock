package net.swofty.types.generic.minion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.SharedInstance;
import net.swofty.types.generic.entity.MinionEntityImpl;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeMinionData;
import net.swofty.types.generic.user.SkyBlockIsland;

import java.util.*;

@Getter
public class IslandMinionData {
    private final SkyBlockIsland island;
    private final List<IslandMinion> minions = new ArrayList<>();

    public IslandMinionData(SkyBlockIsland island) {
        this.island = island;
    }

    public IslandMinion initializeMinion(
            Pos position, MinionRegistry minion, ItemAttributeMinionData.MinionData previousData) {
        IslandMinion islandMinion = new IslandMinion(
                UUID.randomUUID(), position, minion,
                0, previousData.generatedResources(), null);
        minions.add(islandMinion);
        return islandMinion;
    }

    public void spawn(IslandMinion minion) {
        minion.spawnMinion(getIsland().getSharedInstance().join());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class IslandMinion {
        private final UUID minionUUID;
        private final Pos position;
        private final MinionRegistry minion;
        private int itemsInMinion;
        private int generatedItems;
        private MinionEntityImpl minionEntity;

        public void spawnMinion(SharedInstance instance) {
            minionEntity = new MinionEntityImpl(minion.asSkyBlockMinion());
            minionEntity.setInstance(instance, position);

            minionEntity.spawn();
        }

        public Map<String, Object> serialize() {
            Map<String, Object> data = new HashMap<>();
            data.put("position", position.blockX() + "," + position.blockY() + "," + position.blockZ());
            data.put("itemsInMinion", itemsInMinion);
            data.put("minion", minion.name());
            data.put("generatedItems", generatedItems);
            data.put("minionUUID", minionUUID.toString());
            return data;
        }

        public static IslandMinion deserialize(Map<String, Object> data) {
            return new IslandMinion(
                    UUID.fromString(data.get("minionUUID").toString()),
                    new Pos(
                            Integer.parseInt(data.get("position").toString().split(",")[0]),
                            Integer.parseInt(data.get("position").toString().split(",")[1]),
                            Integer.parseInt(data.get("position").toString().split(",")[2])
                    ),
                    MinionRegistry.valueOf(data.get("minion").toString()),
                    (int) data.get("itemsInMinion"),
                    (int) data.get("generatedItems"),
                    null
            );
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();

        minions.forEach(minion -> {
            data.put(minion.minionUUID.toString(), minion.serialize());
        });

        return data;
    }

    public static IslandMinionData deserialize(Map<String, Object> data, SkyBlockIsland island) {
        IslandMinionData islandMinionData = new IslandMinionData(island);
        data.forEach((uuid, minionData) -> {
            islandMinionData.minions.add(IslandMinion.deserialize((Map<String, Object>) minionData));
        });
        return islandMinionData;
    }
}
