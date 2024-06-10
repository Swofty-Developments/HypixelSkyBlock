package net.swofty.commons.item.attribute.attributes;

import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;
import net.swofty.commons.item.attribute.ItemAttribute;

public class ItemAttributeMithrilInfusion extends ItemAttribute<Boolean> {
    @Override
    public String getKey() {
        return "mithril_infusion";
    }

    @Override
    public Boolean getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return false;
    }

    @Override
    public Boolean loadFromString(String string) {
        return Boolean.parseBoolean(string);
    }

    @Override
    public String saveIntoString() {
        return this.value.toString();
    }
}
