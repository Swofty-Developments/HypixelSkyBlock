package net.swofty.commons.skyblock.item.attribute.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.PotatoType;
import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ItemAttributeHotPotatoBookData extends ItemAttribute<ItemAttributeHotPotatoBookData.HotPotatoBookData> {

    @Override
    public String getKey() {
        return "hot_potato_book_data";
    }

    @Override
    public HotPotatoBookData getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return new HotPotatoBookData();
    }

    @Override
    public HotPotatoBookData loadFromString(String string) {
        HotPotatoBookData hotPotatoBookData = new HotPotatoBookData();

        JSONObject obj = new JSONObject(string);

        PotatoType potatoType = null;
        if (obj.has("potatoType")) {
            potatoType = obj.getEnum(PotatoType.class, "potatoType");
        }

        hotPotatoBookData.setPotatoType(potatoType);

        if (obj.has("applied")) {
            var list = obj.getJSONArray("applied");

            HashMap<ItemType, Integer> applied = new HashMap<>();
            for (int i = 0; i < list.length(); i++) {
                var value = list.getString(i);

                var split = value.split(":");
                applied.put(ItemType.valueOf(split[0]), Integer.parseInt(split[1]));
            }

            hotPotatoBookData.setAppliedItems(applied);
        }

        return hotPotatoBookData;
    }

    @Override
    public String saveIntoString() {
        JSONObject obj = new JSONObject();

        if (getValue().hasAppliedItem()) {
            obj.put("potatoType", getValue().getPotatoType());
        } else {
            obj.putOnce("potatoType", null);
        }

        JSONArray array = new JSONArray();

        for (Map.Entry<ItemType, Integer> appliedItem : getValue().getAppliedItems().entrySet()) {
                array.put(appliedItem.getKey() + ":" + appliedItem.getValue());
            }

        obj.put("applied", array);
        return obj.toString();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class HotPotatoBookData {
        private PotatoType potatoType = null;
        private HashMap<ItemType, Integer> appliedItems = new HashMap<>();

        public void addAmount(ItemType itemType, int amount) {
            if (!appliedItems.containsKey(itemType)) {
                appliedItems.put(itemType, amount);
            } else {
                appliedItems.put(itemType, appliedItems.get(itemType) + amount);
            }
        }

        public int getAmount(ItemType type) {
            return appliedItems.getOrDefault(type, 0);
        }

        public boolean hasAppliedItem(ItemType itemType) {
            return appliedItems.containsKey(itemType) && appliedItems.get(itemType) > 0;
        }

        public boolean hasAppliedItem() {
            return appliedItems.values().stream().mapToInt(Integer::intValue).sum() > 0;
        }
    }
}
