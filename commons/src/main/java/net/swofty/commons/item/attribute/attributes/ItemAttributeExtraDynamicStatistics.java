package net.swofty.commons.item.attribute.attributes;

import net.swofty.commons.item.attribute.ItemAttribute;
import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

/**
 * Stores extra dynamic statistics that are added on top of the base item statistics.
 */
public class ItemAttributeExtraDynamicStatistics extends ItemAttribute<ItemStatistics> {
    @Override
    public String getKey() {
        return "extra_dynamic_statistics";
    }

    @Override
    public ItemStatistics getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return ItemStatistics.empty();
    }

    @Override
    public ItemStatistics loadFromString(String string) {
        if (string == null || string.isEmpty()) {
            return ItemStatistics.empty();
        }
        return ItemStatistics.fromString(string);
    }

    @Override
    public String saveIntoString() {
        if (getValue() == null) {
            return "";
        }
        return getValue().toString();
    }
}
