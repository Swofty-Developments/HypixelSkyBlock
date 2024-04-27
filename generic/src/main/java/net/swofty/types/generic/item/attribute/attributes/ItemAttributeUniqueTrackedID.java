package net.swofty.types.generic.item.attribute.attributes;

import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeUniqueTrackedID extends ItemAttribute<String> {
    @Override
    public String getKey() {
        return "unique-tracked-id";
    }

    @Override
    public String getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass) {
        return "none";
    }

    @Override
    public String loadFromString(String string) {
        return string;
    }

    @Override
    public String saveIntoString() {
        return this.value;
    }
}
