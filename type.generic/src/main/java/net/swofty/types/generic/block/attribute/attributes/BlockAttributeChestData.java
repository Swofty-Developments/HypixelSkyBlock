package net.swofty.types.generic.block.attribute.attributes;

import net.swofty.types.generic.block.attribute.BlockAttribute;
import net.swofty.types.generic.block.impl.CustomSkyBlockBlock;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.serializer.SkyBlockItemDeserializer;
import net.swofty.types.generic.serializer.SkyBlockItemSerializer;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BlockAttributeChestData extends BlockAttribute<List<SkyBlockItem>> {
    @Override
    public String getKey() {
        return "chest";
    }

    @Override
    public List<SkyBlockItem> getDefaultValue(@Nullable Class<? extends CustomSkyBlockBlock> blockClass) {
        return new ArrayList<>();
    }

    @Override
    public List<SkyBlockItem> loadFromString(String string) {
        List<SkyBlockItem> items = new ArrayList<>();

        if (string.isEmpty()) {
            return items;
        }

        String[] split = string.split(",");
        for (String item : split) {
            items.add(SkyBlockItemDeserializer.deserializeJSON(new JSONObject(item)));
        }

        return items;
    }

    @Override
    public String saveIntoString() {
        List<SkyBlockItem> items = this.value;
        List<String> serializedItems = new ArrayList<>();

        items.forEach(item -> {
            serializedItems.add(SkyBlockItemSerializer.serializeJSON(item).toString());
        });

        return String.join(",", serializedItems);
    }
}
