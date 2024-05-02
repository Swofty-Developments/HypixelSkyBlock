package net.swofty.types.generic.data.datapoints;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.serializer.StorageSerializer;

import java.util.ArrayList;
import java.util.List;

public class DatapointStorage extends Datapoint<DatapointStorage.PlayerStorage> {
    private static final StorageSerializer serializer = new StorageSerializer();

    public DatapointStorage(String key, DatapointStorage.PlayerStorage value) {
        super(key, value, serializer);
    }

    public DatapointStorage(String key) {
        this(key, new PlayerStorage());
    }

    @JsonIgnoreProperties(ignoreUnknown = true) // Due to serializer serializing "highestPage"
    @NoArgsConstructor
    public static class PlayerStorage {
        public final List<StorageSlot> slots = new ArrayList<>();

        public PlayerStorage(List<StorageSlot> slots) {
            this.slots.addAll(slots);
        }

        public boolean hasPage(int page) {
            return slots.stream().anyMatch(slot -> slot.page == page);
        }

        public StorageSlot getPage(int page) {
            return slots.stream().filter(slot -> slot.page == page).findFirst().orElse(null);
        }

        public int getHighestPage() {
            return slots.stream().mapToInt(slot -> slot.page).max().orElse(0);
        }

        public void addPage(int page) {
            if (hasPage(page)) return;
            slots.add(new StorageSlot(page, Material.PURPLE_STAINED_GLASS_PANE, new SkyBlockItem[45]));
        }

        public void removePage(int page) {
            if (!hasPage(page)) return;
            slots.removeIf(slot -> slot.page == page);
        }

        public void setDisplay(int page, Material display) {
            if (!hasPage(page)) return;
            getPage(page).display = display;
        }

        public void setItems(int page, SkyBlockItem[] items) {
            if (!hasPage(page)) return;
            getPage(page).items = items;
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class StorageSlot {
            public int page;
            public Material display;
            public SkyBlockItem[] items;
        }
    }
}
