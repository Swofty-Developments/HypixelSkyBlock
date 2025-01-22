package net.swofty.commons.item.attribute.attributes;

import net.swofty.commons.item.attribute.ItemAttribute;
import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeRecombobulated extends ItemAttribute<Boolean> {
    @Override
    public String getKey() {
        return "recombobulated";
    }

    @Override
    public Boolean getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
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
