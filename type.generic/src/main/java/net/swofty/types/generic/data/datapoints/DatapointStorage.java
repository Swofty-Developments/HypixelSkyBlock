package net.swofty.types.generic.data.datapoints;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minestom.server.item.Material;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.protocol.serializers.UnderstandableSkyBlockItemSerializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.SkyBlockItem;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatapointStorage extends Datapoint<DatapointStorage.PlayerStorage> {
    private static final Serializer<DatapointStorage.PlayerStorage> serializer = new Serializer<PlayerStorage>() {
        @Override
        public String serialize(PlayerStorage value) {
            JSONObject jsonObject = new JSONObject();
            List<JSONObject> slots = new ArrayList<>();

            for (PlayerStorage.StorageSlot slot : value.slots) {
                slots.add(slot.toJson());
            }

            jsonObject.put("slots", slots);
            return jsonObject.toString();
        }

        @Override
        public PlayerStorage deserialize(String json) {
            JSONObject jsonObject = new JSONObject(json);
            List<PlayerStorage.StorageSlot> slots = new ArrayList<>();

            for (Object slot : jsonObject.getJSONArray("slots")) {
                slots.add(PlayerStorage.StorageSlot.fromJson((JSONObject) slot));
            }

            return new PlayerStorage(slots);
        }

        @Override
        public PlayerStorage clone(PlayerStorage value) {
            return new PlayerStorage(value.slots);
        }
    };

    public DatapointStorage(String key, DatapointStorage.PlayerStorage value) {
        super(key, value, serializer);
    }

    public DatapointStorage(String key) {
        this(key, new PlayerStorage());
    }

    @JsonIgnoreProperties(ignoreUnknown = true) // Due to protocols serializing "highestPage"
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

            public JSONObject toJson() {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("page", page);
                jsonObject.put("display", display.namespace().asString());
                List<String> items = new ArrayList<>();
                for (SkyBlockItem item : this.items) {
                    if (item == null) {
                        items.add("null");
                        continue;
                    }
                    items.add(item.toUnderstandable().serialize());
                }
                jsonObject.put("configuration/items", items);
                return jsonObject;
            }

            public static StorageSlot fromJson(JSONObject jsonObject) {
                int page = jsonObject.getInt("page");
                Material display = Material.fromNamespaceId(jsonObject.getString("display"));
                List<SkyBlockItem> items = new ArrayList<>();
                for (Object item : jsonObject.getJSONArray("configuration/items")) {
                    if (item.equals("null")) {
                        items.add(null);
                        continue;
                    }
                    items.add(new SkyBlockItem(new UnderstandableSkyBlockItemSerializer().deserialize((String) item)));
                }
                return new StorageSlot(page, display, items.toArray(new SkyBlockItem[0]));
            }
        }
    }
}
