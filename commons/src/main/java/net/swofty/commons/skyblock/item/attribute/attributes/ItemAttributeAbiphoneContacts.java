package net.swofty.commons.skyblock.item.attribute.attributes;

import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemAttributeAbiphoneContacts extends ItemAttribute<List<String>> {
    @Override
    public String getKey() {
        return "abiphone_contacts";
    }

    @Override
    public List<String> getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return List.of();
    }

    @Override
    public List<String> loadFromString(String string) {
        try {
            return List.of(string.split(","));
        } catch (NumberFormatException e) {
            return List.of();
        }
    }

    @Override
    public String saveIntoString() {
        return String.join(",", getValue());
    }
}
