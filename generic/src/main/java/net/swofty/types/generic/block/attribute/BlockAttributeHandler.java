package net.swofty.types.generic.block.attribute;

import net.swofty.types.generic.block.BlockType;
import net.swofty.types.generic.block.SkyBlockBlock;
import net.swofty.types.generic.block.attribute.attributes.BlockAttributeType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeType;

public class BlockAttributeHandler {
    SkyBlockBlock block;

    public BlockAttributeHandler(SkyBlockBlock block) {
        this.block = block;
    }

    public BlockType getBlockType() {
        return BlockType.valueOf(((BlockAttributeType) block.getAttribute("block_type")).getValue());
    }

}
