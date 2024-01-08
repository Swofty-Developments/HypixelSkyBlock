package net.swofty.types.generic.item.attribute.attributes;

import net.swofty.types.generic.item.attribute.ItemAttribute;

public class ItemAttributeBreakingPower extends ItemAttribute<Integer> {
    @Override
    public String getKey() {
        return "breaking_power";
    }

    @Override
    public Integer getDefaultValue() {
        return 0;
    }

    @Override
    public Integer loadFromString(String string) {
        return Integer.parseInt(string);
    }

    @Override
    public String saveIntoString() {
        return String.valueOf(this.value);
    }
}
