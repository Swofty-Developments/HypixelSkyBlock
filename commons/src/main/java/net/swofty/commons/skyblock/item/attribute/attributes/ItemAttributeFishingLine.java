package net.swofty.commons.skyblock.item.attribute.attributes;

import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeFishingLine extends ItemAttribute<String> {
    @Override
    public String getKey() {
        return "fishing_line";
    }

    @Override
    public String getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return "none";
    }

    @Override
    public String loadFromString(String string) {
        return string == null || string.isBlank() ? "none" : string;
    }

    @Override
    public String saveIntoString() {
        return getValue() == null || getValue().isBlank() ? "none" : getValue();
    }
}
