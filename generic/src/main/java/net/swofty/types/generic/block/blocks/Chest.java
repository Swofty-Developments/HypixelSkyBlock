package net.swofty.types.generic.block.blocks;

import lombok.NonNull;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.block.SkyBlockBlock;
import net.swofty.types.generic.block.attribute.BlockAttributeHandler;
import net.swofty.types.generic.block.impl.BlockBreakable;
import net.swofty.types.generic.block.impl.BlockInteractable;
import net.swofty.types.generic.block.impl.CustomSkyBlockBlock;
import net.swofty.types.generic.gui.inventory.inventories.GUIChest;
import net.swofty.types.generic.item.ChestImpl;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class Chest implements CustomSkyBlockBlock, BlockInteractable, BlockBreakable {
    @Override
    public @NonNull Block getDisplayMaterial() {
        return Block.CHEST;
    }

    @Override
    public @NonNull Boolean shouldPlace(SkyBlockPlayer player) {
        return SkyBlockConst.isIslandServer();
    }

    @Override
    public @NonNull Boolean shouldDestroy(SkyBlockPlayer player) {
        return SkyBlockConst.isIslandServer();
    }

    @Override
    public void onInteract(PlayerBlockInteractEvent event, SkyBlockBlock block) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (!SkyBlockConst.isIslandServer()) return;
        event.setBlockingItemUse(true);

        Instance instance = event.getInstance();
        Point position = event.getBlockPosition();

        ChestImpl chest = new ChestImpl(instance, position);
        new GUIChest(chest).open(player);
    }

    @Override
    public void onBreak(PlayerBlockBreakEvent event, SkyBlockBlock block) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        Instance instance = event.getInstance();
        Point position = event.getBlockPosition();

        ChestImpl chest = new ChestImpl(instance, position);
        chest.getItemsList().forEach(player::addAndUpdateItem);
    }
}
