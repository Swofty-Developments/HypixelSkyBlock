package net.swofty.item.attribute.attributes;

import net.swofty.item.Rarity;
import net.swofty.item.attribute.ItemAttribute;

public class ItemAttributeRarity extends ItemAttribute<Rarity> {

    @Override
    public String getKey() {
        return "rarity";
    }

    @Override
    public Rarity getDefaultValue() {
        return Rarity.COMMON;
    }

    @Override
    public Rarity loadFromString(String string) {
        return Rarity.valueOf(string);
    }

    @Override
    public String saveIntoString() {
        return this.value.name();
    }
}
