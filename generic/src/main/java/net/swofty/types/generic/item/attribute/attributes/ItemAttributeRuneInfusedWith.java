package net.swofty.types.generic.item.attribute.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.RuneItem;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public class ItemAttributeRuneInfusedWith extends ItemAttribute<ItemAttributeRuneInfusedWith.RuneData> {

    @Override
    public String getKey() {
        return "rune_infused_with";
    }

    @Override
    public RuneData getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass) {
        return new RuneData(null, null);
    }

    @Override
    public RuneData loadFromString(String string) {
        if (string.equals("null")) {
            return new RuneData(null, null);
        }
        String[] split = string.split(";");
        Integer level = Integer.parseInt(split[0]);;
        ItemType runeType = ItemType.valueOf(split[1]);;

        return new RuneData(level, runeType);
    }

    @Override
    public String saveIntoString() {
        if (getValue() == null || !getValue().hasRune()) {
            return "null";
        }
        return getValue().getLevel() + ";" + getValue().getRuneType().name();
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class RuneData {
        private @Nullable Integer level;
        private @Nullable ItemType runeType;

        public boolean hasRune() {
            return runeType != null;
        }

        public RuneItem getAsRuneItem() {
            try {
                return (RuneItem) runeType.clazz.getConstructor().newInstance();
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
