package net.swofty.types.generic.item.attribute.attributes;

import lombok.SneakyThrows;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.serializer.InventorySerializer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemAttributeBackpackData extends ItemAttribute<ItemAttributeBackpackData.BackpackData> {
    @Override
    public String getKey() {
        return "backpack_data";
    }

    @Override
    public ItemAttributeBackpackData.BackpackData getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass) {
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

    public record BackpackData(List<SkyBlockItem> items) {
        public BackpackData() {
            this(new ArrayList<>());
        }
    }
}

