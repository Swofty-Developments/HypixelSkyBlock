package net.swofty.commons.item.attribute.attributes;

import net.swofty.commons.item.ReforgeType;
import net.swofty.commons.item.attribute.ItemAttribute;
import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeReforge extends ItemAttribute<ReforgeType.Reforge> {
    @Override
    public String getKey() {
        return "reforge";
    }

    @Override
    public ReforgeType.Reforge getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
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
