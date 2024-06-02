package net.swofty.types.generic.item.attribute.attributes;

import lombok.SneakyThrows;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeBreakingPower extends ItemAttribute<Integer> {
    @Override
    public String getKey() {
        return "breaking-power";
    }

    @SneakyThrows
    @Override
    public Integer getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass) {
        try {
            return itemClass.newInstance().getStatistics(null).getOverall(ItemStatistic.BREAKING_POWER).intValue();
        } catch (Exception e) {}
        return 0;
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
