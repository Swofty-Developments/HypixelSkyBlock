package net.swofty.type.skyblockgeneric.item.handlers.orbs;

import net.minestom.server.instance.block.Block;
import net.swofty.type.generic.entity.ServerCrystalImpl;

import java.util.function.Function;

public record ServerOrbHandler(
        Function<ServerCrystalImpl, Block> spawnMaterialFunction
) {
}