package net.swofty.type.skyblockgeneric.block.attribute.attributes;

import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.type.skyblockgeneric.block.attribute.BlockAttribute;
import net.swofty.type.skyblockgeneric.block.impl.CustomSkyBlockBlock;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import org.jetbrains.annotations.Nullable;

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
            items.add(new SkyBlockItem(UnderstandableSkyBlockItem.deserialize(item)));
        }

        return items;
    }

    @Override
    public String saveIntoString() {
        List<SkyBlockItem> items = this.value;
        List<String> serializedItems = new ArrayList<>();

        items.forEach(item -> {
            serializedItems.add(item.toUnderstandable().serialize());
        });

        return String.join(",", serializedItems);
    }
}
