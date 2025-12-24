package net.swofty.commons.skyblock.item.attribute.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeRuneInfusedWith extends ItemAttribute<ItemAttributeRuneInfusedWith.RuneData> {

    @Override
    public String getKey() {
        return "rune_infused_with";
    }

    @Override
    public RuneData getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
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
    }
}
