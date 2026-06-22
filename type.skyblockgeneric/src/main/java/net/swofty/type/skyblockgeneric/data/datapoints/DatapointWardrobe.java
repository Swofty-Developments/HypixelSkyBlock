package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.protocol.serializers.UnderstandableSkyBlockItemSerializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatapointWardrobe extends SkyBlockDatapoint<DatapointWardrobe.WardrobeData> {
    private static final Serializer<WardrobeData> SERIALIZER = new Serializer<>() {
        @Override
        public String serialize(WardrobeData value) {
            JSONObject json = new JSONObject();
            json.put("equippedSlot", value.equippedSlot);
            json.put("communitySlots", value.communitySlots);
            List<JSONObject> sets = new ArrayList<>();
            for (ArmorSet set : value.sets) sets.add(set.toJson());
            json.put("sets", sets);
            return json.toString();
        }

        @Override
        public WardrobeData deserialize(String serialized) {
            JSONObject json = new JSONObject(serialized);
            WardrobeData data = new WardrobeData();
            data.equippedSlot = json.optInt("equippedSlot", -1);
            data.communitySlots = Math.clamp(json.optInt("communitySlots", 0), 0, 9);
            JSONArray sets = json.optJSONArray("sets");
            if (sets != null) {
                for (int i = 0; i < Math.min(27, sets.length()); i++) {
                    data.sets[i] = ArmorSet.fromJson(sets.getJSONObject(i));
                }
            }
            return data;
        }

        @Override
        public WardrobeData clone(WardrobeData value) {
            return deserialize(serialize(value));
        }
    };

    public DatapointWardrobe(String key) {
        super(key, new WardrobeData(), SERIALIZER);
    }

    public DatapointWardrobe(String key, WardrobeData value) {
        super(key, value, SERIALIZER);
    }

    @Getter
    @Setter
    public static class WardrobeData {
        private ArmorSet[] sets = new ArmorSet[27];
        private int equippedSlot = -1;
        private int communitySlots;

        public WardrobeData() {
            for (int i = 0; i < sets.length; i++) sets[i] = new ArmorSet();
        }
    }

    @Getter
    @Setter
    public static class ArmorSet {
        private SkyBlockItem[] pieces = new SkyBlockItem[4];
        private long firstWorn;

        public boolean isEmpty() {
            for (SkyBlockItem piece : pieces) if (piece != null && !piece.isNA()) return false;
            return true;
        }

        public boolean isComplete() {
            for (SkyBlockItem piece : pieces) if (piece == null || piece.isNA()) return false;
            return true;
        }

        private JSONObject toJson() {
            JSONObject json = new JSONObject();
            List<String> saved = new ArrayList<>();
            for (SkyBlockItem piece : pieces) {
                saved.add(piece == null || piece.isNA() ? "null" : piece.toUnderstandable().serialize());
            }
            json.put("pieces", saved);
            json.put("firstWorn", firstWorn);
            return json;
        }

        private static ArmorSet fromJson(JSONObject json) {
            ArmorSet set = new ArmorSet();
            JSONArray pieces = json.optJSONArray("pieces");
            if (pieces != null) {
                for (int i = 0; i < Math.min(4, pieces.length()); i++) {
                    String saved = pieces.optString(i, "null");
                    if (!saved.equals("null")) {
                        set.pieces[i] = new SkyBlockItem(new UnderstandableSkyBlockItemSerializer().deserialize(saved));
                    }
                }
            }
            set.firstWorn = json.optLong("firstWorn", 0);
            return set;
        }
    }
}
