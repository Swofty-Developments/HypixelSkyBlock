package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.protocol.serializers.UnderstandableSkyBlockItemSerializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import org.json.JSONArray;
import org.json.JSONObject;

public class DatapointLoadouts extends SkyBlockDatapoint<DatapointLoadouts.LoadoutsData> {
    public static final int LOADOUT_COUNT = 36;
    public static final int EQUIPMENT_SET_COUNT = 18;
    public static final int TREE_SLOT_COUNT = 5;

    private static final Serializer<LoadoutsData> SERIALIZER = new Serializer<>() {
        @Override
        public String serialize(LoadoutsData value) {
            JSONArray loadouts = new JSONArray();
            for (Loadout loadout : value.loadouts) loadouts.put(loadout.toJson());
            JSONArray equipmentSets = new JSONArray();
            for (EquipmentSet set : value.equipmentSets) equipmentSets.put(set.toJson());
            return new JSONObject()
                    .put("equipped", value.equipped)
                    .put("loadouts", loadouts)
                    .put("equipmentSets", equipmentSets)
                    .put("hotmNames", new JSONArray(value.hotmNames))
                    .put("hotfNames", new JSONArray(value.hotfNames))
                    .put("activeHotmSlot", value.activeHotmSlot)
                    .put("activeHotfSlot", value.activeHotfSlot)
                    .put("lastHotmSwap", value.lastHotmSwap)
                    .put("lastHotfSwap", value.lastHotfSwap)
                    .toString();
        }

        @Override
        public LoadoutsData deserialize(String serialized) {
            LoadoutsData data = new LoadoutsData();
            if (serialized == null || serialized.isBlank()) return data;
            JSONObject json = new JSONObject(serialized);
            data.equipped = json.optInt("equipped", -1);
            JSONArray loadouts = json.optJSONArray("loadouts");
            if (loadouts != null) {
                for (int i = 0; i < Math.min(LOADOUT_COUNT, loadouts.length()); i++) {
                    data.loadouts[i] = Loadout.fromJson(loadouts.getJSONObject(i), i);
                }
            }
            JSONArray equipmentSets = json.optJSONArray("equipmentSets");
            if (equipmentSets != null) {
                for (int i = 0; i < Math.min(EQUIPMENT_SET_COUNT, equipmentSets.length()); i++) {
                    data.equipmentSets[i] = EquipmentSet.fromJson(equipmentSets.getJSONObject(i));
                }
            }
            readNames(json.optJSONArray("hotmNames"), data.hotmNames, "Heart of the Mountain");
            readNames(json.optJSONArray("hotfNames"), data.hotfNames, "Heart of the Forest");
            data.activeHotmSlot = Math.clamp(json.optInt("activeHotmSlot", 0), 0, TREE_SLOT_COUNT - 1);
            data.activeHotfSlot = Math.clamp(json.optInt("activeHotfSlot", 0), 0, TREE_SLOT_COUNT - 1);
            data.lastHotmSwap = json.optLong("lastHotmSwap", 0);
            data.lastHotfSwap = json.optLong("lastHotfSwap", 0);
            return data;
        }

        @Override
        public LoadoutsData clone(LoadoutsData value) {
            return deserialize(serialize(value));
        }
    };

    public DatapointLoadouts(String key) {
        super(key, new LoadoutsData(), SERIALIZER);
    }

    private static void readNames(JSONArray source, String[] target, String prefix) {
        if (source == null) return;
        for (int i = 0; i < Math.min(target.length, source.length()); i++) {
            String name = source.optString(i, "").trim();
            if (!name.isEmpty()) target[i] = name;
        }
    }

    @Getter
    @Setter
    public static class LoadoutsData {
        private Loadout[] loadouts = new Loadout[LOADOUT_COUNT];
        private EquipmentSet[] equipmentSets = new EquipmentSet[EQUIPMENT_SET_COUNT];
        private String[] hotmNames = defaultNames("Heart of the Mountain");
        private String[] hotfNames = defaultNames("Heart of the Forest");
        private int equipped = -1;
        private int activeHotmSlot;
        private int activeHotfSlot;
        private long lastHotmSwap;
        private long lastHotfSwap;

        public LoadoutsData() {
            for (int i = 0; i < loadouts.length; i++) loadouts[i] = new Loadout("Loadout " + (i + 1));
            for (int i = 0; i < equipmentSets.length; i++) equipmentSets[i] = new EquipmentSet();
        }

        private static String[] defaultNames(String prefix) {
            String[] names = new String[TREE_SLOT_COUNT];
            for (int i = 0; i < names.length; i++) names[i] = prefix + " " + (i + 1);
            return names;
        }
    }

    @Getter
    @Setter
    public static class Loadout {
        private String name;
        private SkyBlockItem[] armor = new SkyBlockItem[4];
        private SkyBlockItem[] equipment = new SkyBlockItem[4];
        private int armorSet = -1;
        private String petType;
        private int hotmSlot = -1;
        private int hotfSlot = -1;

        public Loadout(String name) {
            this.name = name;
        }

        public boolean isEmpty() {
            for (SkyBlockItem item : armor) if (present(item)) return false;
            for (SkyBlockItem item : equipment) if (present(item)) return false;
            return petType == null && hotmSlot < 0 && hotfSlot < 0;
        }

        private JSONObject toJson() {
            return new JSONObject()
                    .put("name", name)
                    .put("armor", items(armor))
                    .put("equipment", items(equipment))
                    .put("armorSet", armorSet)
                    .put("petType", petType == null ? JSONObject.NULL : petType)
                    .put("hotmSlot", hotmSlot)
                    .put("hotfSlot", hotfSlot);
        }

        private static Loadout fromJson(JSONObject json, int index) {
            Loadout loadout = new Loadout(json.optString("name", "Loadout " + (index + 1)));
            loadout.armor = readItems(json.optJSONArray("armor"), 4);
            loadout.equipment = readItems(json.optJSONArray("equipment"), 4);
            loadout.armorSet = json.optInt("armorSet", -1);
            loadout.petType = json.isNull("petType") ? null : json.optString("petType", null);
            loadout.hotmSlot = json.optInt("hotmSlot", -1);
            loadout.hotfSlot = json.optInt("hotfSlot", -1);
            return loadout;
        }
    }

    @Getter
    @Setter
    public static class EquipmentSet {
        private SkyBlockItem[] pieces = new SkyBlockItem[4];

        public boolean isEmpty() {
            for (SkyBlockItem piece : pieces) if (present(piece)) return false;
            return true;
        }

        private JSONObject toJson() {
            return new JSONObject().put("pieces", items(pieces));
        }

        private static EquipmentSet fromJson(JSONObject json) {
            EquipmentSet set = new EquipmentSet();
            set.pieces = readItems(json.optJSONArray("pieces"), 4);
            return set;
        }
    }

    private static boolean present(SkyBlockItem item) {
        return item != null && !item.isNA();
    }

    private static JSONArray items(SkyBlockItem[] items) {
        JSONArray array = new JSONArray();
        for (SkyBlockItem item : items) {
            array.put(present(item) ? item.toUnderstandable().serialize() : JSONObject.NULL);
        }
        return array;
    }

    private static SkyBlockItem[] readItems(JSONArray array, int size) {
        SkyBlockItem[] items = new SkyBlockItem[size];
        if (array == null) return items;
        for (int i = 0; i < Math.min(size, array.length()); i++) {
            if (!array.isNull(i)) {
                items[i] = new SkyBlockItem(new UnderstandableSkyBlockItemSerializer().deserialize(array.getString(i)));
            }
        }
        return items;
    }
}
