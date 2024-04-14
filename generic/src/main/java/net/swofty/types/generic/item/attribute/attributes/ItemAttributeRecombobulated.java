package net.swofty.types.generic.item.attribute.attributes;

import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeRecombobulated extends ItemAttribute<Boolean> {
    @Override
    public String getKey() {
        return "recombobulated";
    }

    @Override
    public Boolean getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass) {
        return false;
    }

    @Override
    public Boolean loadFromString(String string) {
        return Boolean.valueOf(string);
    }

    @Override
    public String saveIntoString() {
        return this.value.toString();
    }
}
