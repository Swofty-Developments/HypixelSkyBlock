package net.swofty.types.generic.item.handlers.orbs;

import net.minestom.server.instance.block.Block;
import net.swofty.types.generic.entity.ServerCrystalImpl;

import java.util.function.Function;

public record ServerOrbHandler(
        Function<ServerCrystalImpl, Block> spawnMaterialFunction
) {
}