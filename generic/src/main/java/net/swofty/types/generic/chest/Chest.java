package net.swofty.types.generic.chest;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.BlockActionPacket;
import net.swofty.types.generic.block.blocks.BlockChest;

import java.util.Arrays;
import java.util.List;

public interface Chest {

    default String getName() {
        return "Chest";
    }

    Instance getInstance();

    Point[] getPosition();

    BlockChest.ChestType getType();

    void setItem(int slot, ItemStack stack);

    void removeItem(int slot);

    ItemStack getItem(int slot);

    List<ItemStack> getItems();

    InventoryType getSize();

    default void playAnimation(Instance instance, Point[] position, BlockChest.ChestAnimation animation) {
        Arrays.stream(position).forEach((pos) -> {
            BlockActionPacket actionPacket = new BlockActionPacket(pos, (byte) 1, animation == BlockChest.ChestAnimation.OPEN ? (byte) 1 : 0, instance.getBlock(pos));
            instance.getPlayers().forEach(player -> player.sendPacket(actionPacket));
        });
    }

}
