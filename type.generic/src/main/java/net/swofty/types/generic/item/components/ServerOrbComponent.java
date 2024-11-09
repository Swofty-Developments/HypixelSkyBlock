package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.entity.ServerCrystalImpl;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.item.handlers.orbs.ServerOrbRegistry;

import java.util.List;
import java.util.function.Function;

public class ServerOrbComponent extends SkyBlockItemComponent {
    @Getter
    private final Function<ServerCrystalImpl, Block> spawnMaterialFunction;
    @Getter
    private final List<Material> validBlocks;

    public ServerOrbComponent(String handlerId, List<Material> validBlocks) {
        this.spawnMaterialFunction = ServerOrbRegistry.getHandler(handlerId).spawnMaterialFunction();
        this.validBlocks = validBlocks;
    }
}