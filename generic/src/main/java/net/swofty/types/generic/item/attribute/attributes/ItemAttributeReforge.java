package net.swofty.types.generic.item.attribute.attributes;

import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeReforge extends ItemAttribute<ReforgeType.Reforge> {
    @Override
    public String getKey() {
        return "reforge";
    }

    @Override
    public ReforgeType.Reforge getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass) {
        return null;
    }

    @Override
    public ReforgeType.Reforge loadFromString(String string) {
        for (ReforgeType reforgeType : ReforgeType.values()) {
            for (ReforgeType.Reforge reforge : reforgeType.getReforges()) {
                if (reforge.prefix().equals(string)) {
                    return reforge;
                }
            }
        }
        return null;
    }

    @Override
    public String saveIntoString() {
        ReforgeType.Reforge toSave = getValue();
        if (toSave == null) {
            return "";
        }
        return toSave.prefix();
    }
}
