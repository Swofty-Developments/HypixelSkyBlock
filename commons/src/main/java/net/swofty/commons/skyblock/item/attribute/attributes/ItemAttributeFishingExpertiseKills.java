package net.swofty.commons.skyblock.item.attribute.attributes;

import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeFishingExpertiseKills extends ItemAttribute<Long> {
    @Override
    public String getKey() {
        return "fishing_expertise_kills";
    }

    @Override
    public Long getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return 0L;
    }

    @Override
    public Long loadFromString(String string) {
        return Long.parseLong(string);
    }

    @Override
    public String saveIntoString() {
        return String.valueOf(getValue());
    }
}
