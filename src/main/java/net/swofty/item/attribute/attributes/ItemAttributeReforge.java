package net.swofty.item.attribute.attributes;

import net.swofty.item.ReforgeType;
import net.swofty.item.attribute.ItemAttribute;
import net.swofty.user.statistics.ItemStatistic;
import net.swofty.user.statistics.ItemStatistics;
import net.swofty.utility.MathUtility;

import java.util.Arrays;
import java.util.List;

public class ItemAttributeReforge extends ItemAttribute<ReforgeType.Reforge> {

    @Override
    public String getKey() {
        return "reforge";
    }

    @Override
    public ReforgeType.Reforge getDefaultValue() {
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
