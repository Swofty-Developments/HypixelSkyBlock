package net.swofty.commons.skyblock.item.attribute.attributes;

import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeStatistics extends ItemAttribute<ItemStatistics> {
    @Override
    public String getKey() {
        return "statistics";
    }

    @Override
    public ItemStatistics getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        if (defaultStatistics != null)
            return defaultStatistics;
        return ItemStatistics.builder().build();
    }

    @Override
    public ItemStatistics loadFromString(String string) {
        return ItemStatistics.fromString(string);
    }

    @Override
    public String saveIntoString() {
        return getValue().toString();
    }
}
