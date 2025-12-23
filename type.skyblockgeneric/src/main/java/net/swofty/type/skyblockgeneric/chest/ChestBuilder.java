package net.swofty.type.skyblockgeneric.chest;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.utility.ChestUtility;
import net.swofty.type.skyblockgeneric.block.BlockType;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;

public class ChestBuilder {
    private final Instance instance;
    private final Point position;
    private final ChestType chestType;

    public ChestBuilder(Instance instance, Point position, ChestType chestType) {
        this.instance = instance;
        this.position = position;
        this.chestType = chestType;
    }

    public ChestBuilder(Instance instance, Point position) {
        this(instance, position, ChestType.SINGLE);
    }

    public Chest build() {
        if (!instance.getBlock(position).name().equals("minecraft:chest") || !SkyBlockBlock.isSkyBlockBlock(instance.getBlock(position)))
            instance.setBlock(position, new SkyBlockBlock(BlockType.CHEST).toBlock());
        if (chestType == ChestType.SINGLE) {
            return new SingleChest(instance, position);
        } else if (chestType == ChestType.DOUBLE) {
            Point[] positions = ChestUtility.getDoubleChestPositions(instance, position);
            return new DoubleChest(instance, positions[0], positions[1]);
        }
        throw new IllegalArgumentException("Invalid chest type: " + chestType);
    }

    public ChestBuilder setItem(int slot, ItemStack item) {
        Chest chest = build();
        chest.setItem(slot, item);
        return this;
    }

    public ChestBuilder removeItem(int slot) {
        Chest chest = build();
        chest.removeItem(slot);
        return this;
    }


}
