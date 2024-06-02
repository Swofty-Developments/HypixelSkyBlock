package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.block.BlockType;

public interface DecorationHead extends SkullHead, PlaceableCustomSkyBlockItem {

    @Override
    default  BlockType getAssociatedBlockType(){
        return BlockType.DECORATION;
    }
}