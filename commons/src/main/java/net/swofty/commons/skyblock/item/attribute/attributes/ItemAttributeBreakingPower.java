package net.swofty.commons.skyblock.item.attribute.attributes;

import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeBreakingPower extends ItemAttribute<Integer> {
    @Override
    public String getKey() {
        return "breaking-power";
    }

    @Override
    public Integer getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        if (defaultStatistics == null)
            return 0;
        return defaultStatistics.getOverall(ItemStatistic.BREAKING_POWER).intValue();
    }

    @Override
    public Integer loadFromString(String string) {
        return Integer.valueOf(string);
    }

    @Override
    public String saveIntoString() {
        return this.value.toString();
    }
}
