package net.swofty.types.generic.item.impl;

import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.entity.ServerCrystalImpl;

import java.util.List;
import java.util.function.Function;

public interface ServerOrb {
    Function<ServerCrystalImpl, Block> getOrbSpawnMaterial();
    List<Material> getBlocksToPlaceOn();
}
