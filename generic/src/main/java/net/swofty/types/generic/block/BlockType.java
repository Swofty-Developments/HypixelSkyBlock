package net.swofty.types.generic.block;

import net.swofty.types.generic.block.blocks.Chest;
import net.swofty.types.generic.block.impl.CustomSkyBlockBlock;

public enum BlockType {
    CHEST(Chest.class),
    ;

    public final Class<? extends CustomSkyBlockBlock> clazz;

    BlockType(Class<? extends CustomSkyBlockBlock> clazz) {
        this.clazz = clazz;
    }
}
