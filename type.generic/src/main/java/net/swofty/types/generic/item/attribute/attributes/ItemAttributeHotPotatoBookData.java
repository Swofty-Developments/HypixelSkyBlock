package net.swofty.types.generic.item.attribute.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.HotPotatoable;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeHotPotatoBookData extends ItemAttribute<ItemAttributeHotPotatoBookData.HotPotatoBookData> {

    @Override
    public String getKey() {
        return "hot_potato_book_data";
    }

    @Override
    public HotPotatoBookData getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass) {
        return new HotPotatoBookData();
    }

    @Override
    public HotPotatoBookData loadFromString(String string) {
        HotPotatoBookData hotPotatoBookData = new HotPotatoBookData();
        String[] split = string.split(",");
        for (String s : split) {
            String[] split1 = s.split(":");
            switch (split1[0]) {
                case "potatoType":
                    if (!split1[1].equals("null")) {
                        hotPotatoBookData.setPotatoType(HotPotatoable.PotatoType.valueOf(split1[1]));
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
        StringBuilder stringBuilder = new StringBuilder();
        if (getValue().hasPotatoBook()) {
            stringBuilder.append("potatoType:").append(getValue().getPotatoType()).append(",");
        } else {
            stringBuilder.append("potatoType:null,");
        }
        stringBuilder.append("amount:").append(getValue().getAmount());
        return stringBuilder.toString();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class HotPotatoBookData {
        private HotPotatoable.PotatoType potatoType = null;
        private int amount = 0;

        public void addAmount(int amount) {
            this.amount += amount;
        }

        public boolean hasPotatoBook() {
            return amount > 0;
        }
    }
}
