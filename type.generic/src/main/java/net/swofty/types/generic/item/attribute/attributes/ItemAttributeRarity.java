package net.swofty.types.generic.item.attribute.attributes;

import net.swofty.types.generic.item.Rarity;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeRarity extends ItemAttribute<Rarity> {
    @Override
    public String getKey() {
        return "rarity";
    }

    @Override
    public Rarity getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass) {
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
