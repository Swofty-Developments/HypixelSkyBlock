package net.swofty.commons.item.attribute.attributes;

import lombok.SneakyThrows;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.commons.item.attribute.ItemAttribute;
import net.swofty.commons.protocol.serializers.InventorySerializer;
import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemAttributeBackpackData extends ItemAttribute<ItemAttributeBackpackData.BackpackData> {
    @Override
    public String getKey() {
        return "backpack_data";
    }

    @Override
    public BackpackData getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return new BackpackData();
    }

    @SneakyThrows
    @Override
    public ItemAttributeBackpackData.BackpackData loadFromString(String string) {
        InventorySerializer<BackpackData> serializer =
                new InventorySerializer<>(BackpackData.class);

        return serializer.deserialize(string);
    }

    @SneakyThrows
    @Override
    public String saveIntoString() {
        InventorySerializer<BackpackData> serializer =
                new InventorySerializer<>(BackpackData.class);

        return serializer.serialize(this.value);
    }

    public record BackpackData(List<UnderstandableSkyBlockItem> items) {
        public BackpackData() {
            this(new ArrayList<>());
        }
    }
}

