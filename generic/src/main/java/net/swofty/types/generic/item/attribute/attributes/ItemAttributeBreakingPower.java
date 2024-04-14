package net.swofty.types.generic.item.attribute.attributes;

import lombok.SneakyThrows;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.BreakingPower;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeBreakingPower extends ItemAttribute<Integer> {
    @Override
    public String getKey() {
        return "breaking-power";
    }

    @SneakyThrows
    @Override
    public Integer getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass) {
        if (itemClass != null && itemClass.newInstance() instanceof BreakingPower power)
            return power.getBreakingPower();
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
