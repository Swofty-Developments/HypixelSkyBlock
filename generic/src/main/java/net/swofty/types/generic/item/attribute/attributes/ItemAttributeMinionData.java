package net.swofty.types.generic.item.attribute.attributes;

import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeMinionData extends ItemAttribute<ItemAttributeMinionData.MinionData> {

    @Override
    public String getKey() {
        return "minion_tier";
    }

    @Override
    public MinionData getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass) {
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
