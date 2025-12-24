package net.swofty.commons.skyblock.item.attribute.attributes;

import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeSoulbound extends ItemAttribute<ItemAttributeSoulbound.SoulBoundData> {
    @Override
    public String getKey() {
        return "soul_bound";
    }

    @Override
    public SoulBoundData getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return null;
    }

    @Override
    public ItemAttributeSoulbound.SoulBoundData loadFromString(String string) {
        if (string.isEmpty()) {
            return null;
        } else {
            return new SoulBoundData(string.equals("true"));
        }
    }

    @Override
    public String saveIntoString() {
        SoulBoundData toSave = getValue();
        if (toSave == null) {
            return "";
        }
        return toSave.isCoopAllowed() ? "true" : "false";
    }

    public record SoulBoundData(boolean isCoopAllowed) {}
}
