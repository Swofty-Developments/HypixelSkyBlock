package net.swofty.types.generic.chest;

import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DoubleChest implements Chest {

    private static final Tag<List<ItemStack>> ITEMS_TAG = Tag.ItemStack("items").list();
    private final Instance instance;
    private final Point leftPosition;
    private final Point rightPosition;
    @Getter
    private final ItemStack[] leftItems;
    @Getter
    private final ItemStack[] rightItems;

    public DoubleChest(Instance instance, Point leftPosition, Point rightPosition) {
        this.instance = instance;
        this.leftPosition = leftPosition;
        this.rightPosition = rightPosition;
        this.leftItems = loadItems(leftPosition);
        this.rightItems = loadItems(rightPosition);
    }

    @Override
    public String getName() {
        return "Large Chest";
    }

    @Override
    public Instance getInstance() {
        return instance;
    }

    @Override
    public Point[] getPosition() {
        return new Point[]{leftPosition, rightPosition};
    }

    @Override
    public ChestType getType() {
        return ChestType.DOUBLE;
    }

    private ItemStack[] loadItems(Point position) {
        ItemStack[] itemStacks;
        List<ItemStack> itemsList = instance.getBlock(position).getTag(ITEMS_TAG);
        if (itemsList == null) {
            itemStacks = new ItemStack[InventoryType.CHEST_3_ROW.getSize()];
            Arrays.fill(itemStacks, ItemStack.AIR);
        } else {
            itemStacks = itemsList.toArray(ItemStack[]::new);
        }
        return itemStacks;

    }


    private void save(Point position, ItemStack[] itemStacks) {
        instance.setBlock(position, instance.getBlock(position).withTag(ITEMS_TAG, List.of(itemStacks)));
    }

    @Override
    public void update() {
        save(leftPosition , leftItems);
        save(rightPosition , rightItems);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot < leftItems.length) {
            leftItems[slot] = stack;
            save(leftPosition, leftItems);
        } else {
            rightItems[slot - leftItems.length] = stack;
            save(rightPosition, rightItems);
        }
    }

    @Override
    public void removeItem(int slot) {
        if (slot < leftItems.length) {
            leftItems[slot] = ItemStack.AIR;
            save(leftPosition, leftItems);
        } else {
            rightItems[slot - leftItems.length] = ItemStack.AIR;
            save(rightPosition, rightItems);
        }
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < leftItems.length) {
            return leftItems[slot];
        } else {
            return rightItems[slot - leftItems.length];
        }
    }

    @Override
    public List<ItemStack> getItems() {
        List<ItemStack> stacks = new ArrayList<>();
        Collections.addAll(stacks, leftItems);
        Collections.addAll(stacks, rightItems);
        return stacks;
    }

    public List<ItemStack> getItems(Point point) {
        return List.of(loadItems(point));
    }

    @Override
    public InventoryType getSize() {
        return InventoryType.CHEST_6_ROW;
    }
}
