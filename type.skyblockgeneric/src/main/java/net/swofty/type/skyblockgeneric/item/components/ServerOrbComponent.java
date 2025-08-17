package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.entity.ServerCrystalImpl;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.handlers.orbs.ServerOrbRegistry;
import org.tinylog.Logger;

import java.util.List;
import java.util.function.Function;

public class ServerOrbComponent extends SkyBlockItemComponent {
    @Getter
    private Function<ServerCrystalImpl, Block> spawnMaterialFunction;
    @Getter
    private List<Material> validBlocks;

    public ServerOrbComponent(String handlerId, List<Material> validBlocks) {
        try {
            this.spawnMaterialFunction = ServerOrbRegistry.getHandler(handlerId).spawnMaterialFunction();
            this.validBlocks = validBlocks;
        } catch (NullPointerException e) {
            this.spawnMaterialFunction = (impl) -> {
                throw new RuntimeException("Failed to get spawn material function for " + handlerId);
            };
            this.validBlocks = validBlocks;
            Logger.error("Failed to get spawn material function for " + handlerId);
        }
    }
}