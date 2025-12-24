package net.swofty.commons.skyblock.item.attribute.attributes;

import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeMinionData extends ItemAttribute<ItemAttributeMinionData.MinionData> {

    @Override
    public String getKey() {
        return "minion_tier";
    }

    @Override
    public MinionData getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return new MinionData(1, 0);
    }

    @Override
    public MinionData loadFromString(String string) {
        if (string.isEmpty()) {
            return new MinionData(1, 0);
        }

        String[] split = string.split(",");
        return new MinionData(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    @Override
    public String saveIntoString() {
        return this.value.tier() + "," + this.value.generatedResources();
    }

    public record MinionData(int tier, int generatedResources) {}
}
