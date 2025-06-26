package net.swofty.commons.item.attribute.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.item.PotatoType;
import net.swofty.commons.item.attribute.ItemAttribute;
import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import org.json.JSONObject;

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
        

        String[] split = string.split(",");
        for (String s : split) {
            String[] split1 = s.split(":");
            switch (split1[0]) {
                case "potatoType":
                    if (!split1[1].equals("null")) {
                        hotPotatoBookData.setPotatoType(PotatoType.valueOf(split1[1]));
                    } else {
                        hotPotatoBookData.setPotatoType(null);
                    }
                    break;
                case "amount":
                    hotPotatoBookData.setAmount(Integer.parseInt(split1[1]));
                    break;
            }
        }
        return hotPotatoBookData;
    }

    @Override
    public String saveIntoString() {
        JSONObject obj = new JSONObject();

        if (getValue().hasPotatoBook()) {
            obj.put("potatoType", getValue().getPotatoType());
        } else {
            obj.put("potatoType", null);
        }

        JSONArray array = new JSONArray();

        for (Map.Entry<ItemType, Integer> appliedItem : getValue().getAppliedItems()) {
                array.put(appliedItem.getKey() + ":" + appliableItem.getValue())
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
            if (!appliedItems.containsKey(itemType)){
                appliedItems.put(itemType, amount)
            }else{
                appliedItems.put(itemType, appliableItems.get(itemType) + amount)
            }
        }

        public bool hasAppliedItem(ItemType itemType){
            return appliedItems.containsKey(itemType) && appliedItems.get(itemType) > 0;
        }
    }
}
