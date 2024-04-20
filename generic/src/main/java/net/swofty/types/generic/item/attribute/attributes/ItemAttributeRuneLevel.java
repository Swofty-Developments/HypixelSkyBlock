package net.swofty.types.generic.item.attribute.attributes;

import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeRuneLevel extends ItemAttribute<Integer> {
    @Override
    public String getKey() {
        return "rune_level";
    }

    @Override
    public Integer getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass) {
        return 1;
    }

    @Override
    public Integer loadFromString(String string) {
        return Integer.parseInt(string);
    }

    @Override
    public String saveIntoString() {
        return getValue().toString();
    }
}
