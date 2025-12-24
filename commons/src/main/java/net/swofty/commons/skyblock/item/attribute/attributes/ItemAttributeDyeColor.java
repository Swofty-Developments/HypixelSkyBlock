package net.swofty.commons.skyblock.item.attribute.attributes;

import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeDyeColor extends ItemAttribute<String> {

    @Override
    public String getKey() {
        return "dye_color";
    }

    @Override
    public String getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return null;
    }

    @Override
    public String loadFromString(String string) {
        if (string == null || string.isEmpty() || string.equals("null")) {
            return null;
        }
        return string;
    }

    @Override
    public String saveIntoString() {
        return getValue() != null ? getValue() : "null";
    }
}
