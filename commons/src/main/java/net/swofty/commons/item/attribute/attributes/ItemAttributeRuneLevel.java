package net.swofty.commons.item.attribute.attributes;

import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;
import net.swofty.commons.item.attribute.ItemAttribute;

public class ItemAttributeRuneLevel extends ItemAttribute<Integer> {
    @Override
    public String getKey() {
        return "rune_level";
    }

    @Override
    public Integer getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return 1;
    }

    @Override
    public Integer loadFromString(String string) {
        return Integer.parseInt(string);
    }

    @Override
    public String saveIntoString() {
        return getValue().toString();
    }
}
