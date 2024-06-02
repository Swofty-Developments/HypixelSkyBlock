package net.swofty.types.generic.item.attribute.attributes;

import lombok.SneakyThrows;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeStatistics extends ItemAttribute<ItemStatistics> {
    @Override
    public String getKey() {
        return "statistics";
    }

    @SneakyThrows
    @Override
    public ItemStatistics getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass) {
        if (itemClass != null)
            return itemClass.newInstance().getStatistics(null);
        return ItemStatistics.builder().build();
    }

    @Override
    public ItemStatistics loadFromString(String string) {
        return ItemStatistics.fromString(string);
    }

    @Override
    public String saveIntoString() {
        return getValue().toString();
    }
}
