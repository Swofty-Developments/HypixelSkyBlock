package net.swofty.type.skyblockgeneric.block.attribute;

import net.swofty.type.skyblockgeneric.block.BlockType;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;
import net.swofty.type.skyblockgeneric.block.attribute.attributes.BlockAttributeType;

public class BlockAttributeHandler {
    SkyBlockBlock block;

    public BlockAttributeHandler(SkyBlockBlock block) {
        this.block = block;
    }

    public BlockType getBlockType() {
        return BlockType.valueOf(((BlockAttributeType) block.getAttribute("block_type")).getValue());
    }

}
