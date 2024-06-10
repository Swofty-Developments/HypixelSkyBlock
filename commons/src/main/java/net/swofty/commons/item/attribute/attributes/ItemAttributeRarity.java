package net.swofty.commons.item.attribute.attributes;

import net.swofty.commons.item.Rarity;
import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;
import net.swofty.commons.item.attribute.ItemAttribute;

public class ItemAttributeRarity extends ItemAttribute<Rarity> {
    @Override
    public String getKey() {
        return "rarity";
    }

    @Override
    public Rarity getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
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
