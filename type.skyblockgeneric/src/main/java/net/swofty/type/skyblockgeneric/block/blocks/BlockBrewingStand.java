package net.swofty.type.skyblockgeneric.block.blocks;

import lombok.NonNull;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;
import net.swofty.type.skyblockgeneric.block.impl.BlockInteractable;
import net.swofty.type.skyblockgeneric.block.impl.CustomSkyBlockBlock;
import net.swofty.type.skyblockgeneric.gui.inventories.GUIBrewingStand;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class BlockBrewingStand implements CustomSkyBlockBlock, BlockInteractable {

    @Override
    public @NonNull Block getDisplayMaterial() {
        return Block.BREWING_STAND;
    }

    @Override
    public @NonNull Boolean shouldPlace(SkyBlockPlayer player) {
        return HypixelConst.isIslandServer();
    }

    @Override
    public @NonNull Boolean shouldDestroy(SkyBlockPlayer player) {
        return HypixelConst.isIslandServer();
    }

    @Override
    public void onInteract(PlayerBlockInteractEvent event, SkyBlockBlock block) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (!HypixelConst.isIslandServer()) return;
        event.setBlockingItemUse(true);

        Instance instance = event.getInstance();
        Point position = event.getBlockPosition();

        new GUIBrewingStand(instance, position, block).open(player);
    }
}
