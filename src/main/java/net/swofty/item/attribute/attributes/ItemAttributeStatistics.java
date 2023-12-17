package net.swofty.item.attribute.attributes;

import net.swofty.Utility;
import net.swofty.item.attribute.ItemAttribute;
import net.swofty.user.statistics.ItemStatistic;
import net.swofty.user.statistics.ItemStatistics;

public class ItemAttributeStatistics extends ItemAttribute<ItemStatistics> {

    @Override
    public String getKey() {
        return "statistics";
    }

    @Override
    public ItemStatistics getDefaultValue() {
        return ItemStatistics.builder().build();
    }

    @Override
    public ItemStatistics loadFromString(String string) {
        String[] split = string.split(",");
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, Integer.parseInt(split[0]))
                .with(ItemStatistic.DEFENSE, Integer.parseInt(split[1]))
                .with(ItemStatistic.HEALTH, Integer.parseInt(split[2]))
                .with(ItemStatistic.INTELLIGENCE, Integer.parseInt(split[3]))
                .with(ItemStatistic.STRENGTH, Integer.parseInt(split[4]))
                .with(ItemStatistic.MINING_SPEED, Utility.arrayDValue(split, 5, 0))
                .build();
    }

    @Override
    public String saveIntoString() {
        return this.value.get(ItemStatistic.DAMAGE) +
                "," +
                this.value.get(ItemStatistic.DEFENSE) +
                "," +
                this.value.get(ItemStatistic.HEALTH) +
                "," +
                this.value.get(ItemStatistic.INTELLIGENCE) +
                "," +
                this.value.get(ItemStatistic.STRENGTH);
    }
}
