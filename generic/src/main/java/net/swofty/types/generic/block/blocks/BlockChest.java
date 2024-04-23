package net.swofty.types.generic.block.blocks;

import lombok.NonNull;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.item.ItemStack;
import net.minestom.server.sound.SoundEvent;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.block.SkyBlockBlock;
import net.swofty.types.generic.block.impl.BlockBreakable;
import net.swofty.types.generic.block.impl.BlockInteractable;
import net.swofty.types.generic.block.impl.CustomSkyBlockBlock;
import net.swofty.types.generic.chest.Chest;
import net.swofty.types.generic.chest.DoubleChest;
import net.swofty.types.generic.chest.SingleChest;
import net.swofty.types.generic.gui.inventory.inventories.GUIChest;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class BlockChest implements CustomSkyBlockBlock, BlockInteractable, BlockBreakable {
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

        Chest chest;

        ChestType chestType = getChestType(instance, position);

        if (chestType == ChestType.DOUBLE) {
            Point[] chestPositions = getDoubleChestPositions(instance, position);
            chest = new DoubleChest(instance, chestPositions[0], chestPositions[1]);
        } else {
            chest = new SingleChest(instance, position);
        }


        chest.playAnimation(chest.getInstance(), chest.getPosition(), ChestAnimation.OPEN);

        player.playSound(Sound.sound(SoundEvent.BLOCK_CHEST_OPEN, Sound.Source.RECORD, 1f, 1f));

        new GUIChest(chest).open(player);
    }


    @Override
    public void onBreak(PlayerBlockBreakEvent event, SkyBlockBlock block) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        Instance instance = event.getInstance();
        Point position = event.getBlockPosition();

        ChestType chestType = getChestType(instance, position);

        List<ItemStack> items;

        if (chestType == ChestType.DOUBLE) {
            Point[] chestPositions = getDoubleChestPositions(instance, position);
            items = new DoubleChest(instance, chestPositions[0], chestPositions[1]).getItems(position);
        } else {
            items = new SingleChest(instance, position).getItems();
        }

        items.forEach(player::addAndUpdateItem);
    }


    private ChestType getChestType(Instance instance, Point position) {
        for (BlockFace face : BlockFace.values()) {
            Block adjacent = instance.getBlock(position.add(face.toDirection().normalX(), face.toDirection().normalY(), face.toDirection().normalZ()));
            if (adjacent.name().equals("minecraft:chest")) {
                if (face == BlockFace.EAST || face == BlockFace.WEST || face == BlockFace.NORTH || face == BlockFace.SOUTH) {
                    return ChestType.DOUBLE;
                }
            }
        }
        return ChestType.SINGLE;
    }


    public Point[] getDoubleChestPositions(Instance instance, Point point) {
        Point[] positions = new Point[2];
        int x = point.blockX();
        int y = point.blockY();
        int z = point.blockZ();

        if (instance.getBlock(x, y, z + 1).name().equals("minecraft:chest")) {
            positions[0] = new Pos(x, y, z);
            positions[1] = new Pos(x, y, z + 1);
        } else if (instance.getBlock(x, y, z - 1).name().equals("minecraft:chest")) {
            positions[0] = new Pos(x, y, z - 1);
            positions[1] = new Pos(x, y, z);
        }
        if (instance.getBlock(x + 1, y, z).name().equals("minecraft:chest")) {
            positions[0] = new Pos(x + 1, y, z);
            positions[1] = new Pos(x, y, z);
        } else if (instance.getBlock(x - 1, y, z).name().equals("minecraft:chest")) {
            positions[0] = new Pos(x, y, z);
            positions[1] = new Pos(x - 1, y, z);
        }

        return positions;
    }

    public enum ChestType {
        SINGLE,
        DOUBLE
    }

    public enum ChestAnimation {
        OPEN,
        CLOSE
    }

}
