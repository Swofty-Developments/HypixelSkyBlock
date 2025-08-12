package net.swofty.type.skyblockgeneric.block.blocks;

import lombok.NonNull;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.sound.SoundEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;
import net.swofty.type.skyblockgeneric.block.impl.BlockBreakable;
import net.swofty.type.skyblockgeneric.block.impl.BlockInteractable;
import net.swofty.type.skyblockgeneric.block.impl.CustomSkyBlockBlock;
import net.swofty.type.skyblockgeneric.chest.*;
import net.swofty.type.skyblockgeneric.gui.inventories.GUIChest;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.utility.ChestUtility;

import java.util.List;

public class BlockChest implements CustomSkyBlockBlock, BlockInteractable, BlockBreakable {
    @Override
    public @NonNull Block getDisplayMaterial() {
        return Block.CHEST;
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

        Chest chest;

        ChestType chestType = ChestType.from(instance, position);

        if (chestType == ChestType.DOUBLE) {
            Point[] chestPositions = ChestUtility.getDoubleChestPositions(instance, position);
            chest = new DoubleChest(instance, chestPositions[0], chestPositions[1]);
        } else {
            chest = new SingleChest(instance, position);
        }

        ChestAnimationType.OPEN.play(chest.getInstance() , chest.getPosition());
        player.playSound(Sound.sound(SoundEvent.BLOCK_CHEST_OPEN, Sound.Source.RECORD, 1f, 1f));

        new GUIChest(chest).open(player);
    }


    @Override
    public void onBreak(PlayerBlockBreakEvent event, SkyBlockBlock block) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        Instance instance = event.getInstance();
        Point position = event.getBlockPosition();

        ChestType chestType = ChestType.from(instance, position);

        List<ItemStack> items;

        if (chestType == ChestType.DOUBLE) {
            Point[] chestPositions = ChestUtility.getDoubleChestPositions(instance, position);
            items = new DoubleChest(instance, chestPositions[0], chestPositions[1]).getItems(position);
        } else {
            items = new SingleChest(instance, position).getItems();
        }

        items.forEach(player::addAndUpdateItem);
    }
}
