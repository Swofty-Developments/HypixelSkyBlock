package net.swofty.types.generic.item.attribute.attributes;

import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.UUID;

public class ItemAttributeStackable extends ItemAttribute<String> {
    @Override
    public String getKey() {
        return "stackable";
    }

    @Override
    public String getDefaultValue() {
        return "none";
    }

    @Override
    public String loadFromString(String string) {
        return string;
    }

    @Override
    public String saveIntoString() {
        return this.value;
    }
}
