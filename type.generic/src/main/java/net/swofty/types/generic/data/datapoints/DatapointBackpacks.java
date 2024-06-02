package net.swofty.types.generic.data.datapoints;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.serializer.InventorySerializer;

import java.util.HashMap;
import java.util.Map;

public class DatapointBackpacks extends Datapoint<DatapointBackpacks.PlayerBackpacks> {
    private static final InventorySerializer<DatapointBackpacks.PlayerBackpacks> serializer =
            new InventorySerializer<>(DatapointBackpacks.PlayerBackpacks.class);

    public DatapointBackpacks(String key, DatapointBackpacks.PlayerBackpacks value) {
        super(key, value, serializer);
    }

    public DatapointBackpacks(String key) {
        super(key, new PlayerBackpacks(), serializer);
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlayerBackpacks {
        private final Map<Integer, SkyBlockItem> backpacks;
        @Setter
        private int unlockedSlots;

        public PlayerBackpacks() {
            this.backpacks = new HashMap<>();
            this.unlockedSlots = 1;
        }

        public PlayerBackpacks(Map<Integer, SkyBlockItem> backpacks, int unlockedSlots) {
            this.backpacks = backpacks;
            this.unlockedSlots = unlockedSlots;
        }

        public int getHighestBackpackSlot() {
            return backpacks.keySet().stream().max(Integer::compareTo).orElse(0);
        }

        public int getLowestBackpackSlot() {
            return backpacks.keySet().stream().min(Integer::compareTo).orElse(0);
        }
    }
}