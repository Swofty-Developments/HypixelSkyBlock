package net.swofty.commons.skyblock.item.attribute.attributes;

import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.item.reforge.ReforgeLoader;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

public class ItemAttributeReforge extends ItemAttribute<String> {
    @Override
    public String getKey() {
        return "reforge";
    }

    @Override
    public String getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return null;
    }

    @Override
    public String loadFromString(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }

        if (ReforgeLoader.getReforge(string) != null) {
            return string;
        }

        Logger.warn("Unknown reforge: {}", string);
        return null;
    }

    @Override
    public String saveIntoString() {
        String toSave = getValue();
        if (toSave == null || toSave.isEmpty()) {
            return "";
        }
        return toSave;
    }
}