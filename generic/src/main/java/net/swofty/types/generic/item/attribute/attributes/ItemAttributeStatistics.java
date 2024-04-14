package net.swofty.types.generic.item.attribute.attributes;

import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeStatistics extends ItemAttribute<ItemStatistics> {
    @Override
    public String getKey() {
        return "statistics";
    }

    @Override
    public ItemStatistics getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass) {
        return ItemStatistics.builder().build();
    }

    @Override
    public ItemStatistics loadFromString(String string) {
        String[] split = string.split(",");

        double damage = Math.max(Double.parseDouble(split[0]), 0D);
        double defense = Math.max(Double.parseDouble(split[1]), 0D);
        double health = Math.max(Double.parseDouble(split[2]), 0D);
        double intelligence = Math.max(Double.parseDouble(split[3]), 0D);
        double strength = Math.max(Double.parseDouble(split[4]), 0D);
        double miningSpeed = Math.max(Double.parseDouble(split[5]), 0D);

        if (damage > 300) damage = 0;
        if (defense > 300) defense = 0;
        if (health > 300) health = 0;
        if (intelligence > 300) intelligence = 0;
        if (strength > 300) strength = 0;
        if (miningSpeed > 300) miningSpeed = 0;

        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, damage)
                .with(ItemStatistic.DEFENSE, defense)
                .with(ItemStatistic.HEALTH, health)
                .with(ItemStatistic.INTELLIGENCE, intelligence)
                .with(ItemStatistic.STRENGTH, strength)
                .with(ItemStatistic.MINING_SPEED, miningSpeed)
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
                this.value.get(ItemStatistic.STRENGTH) +
                "," +
                this.value.get(ItemStatistic.MINING_SPEED);
    }
}
