package net.swofty.types.generic.chest;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;

import java.util.Arrays;
import java.util.List;

public class SingleChest implements Chest {

    private static final Tag<List<ItemStack>> ITEMS_TAG = Tag.ItemStack("items").list();

    private final Instance instance;
    private final Point position;

    private ItemStack[] items;

    public SingleChest(Instance instance, Point position) {
        this.instance = instance;
        this.position = position;
        this.items = new ItemStack[InventoryType.CHEST_3_ROW.getSize()];
        load();
    }

    @Override
    public String getName() {return "Chest";}

    @Override
    public Instance getInstance() {
        return instance;
    }

    @Override
    public Point[] getPosition() {
        return new Point[]{position};
    }

    @Override
    public ChestType getType() {
        return ChestType.SINGLE;
    }

    public void load() {
        List<ItemStack> itemsList = instance.getBlock(position).getTag(ITEMS_TAG);

        if (itemsList == null) {
            ItemStack[] newItems = new ItemStack[InventoryType.CHEST_3_ROW.getSize()];
            Arrays.fill(newItems, ItemStack.AIR);
            this.items = newItems;
        } else {
            this.items = itemsList.toArray(ItemStack[]::new);
        }
    }


    public void save() {
        instance.setBlock(position, instance.getBlock(position).withTag(ITEMS_TAG, List.of(items)));
    }

    @Override
    public void update() {
        save();
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items[slot] = stack;
        save();
    }

    @Override
    public void removeItem(int slot) {
        items[slot] = ItemStack.AIR;
        save();
    }

    @Override
    public ItemStack getItem(int slot) {
        return items[slot];
    }

    @Override
    public List<ItemStack> getItems() {
        return List.of(items);
    }

    @Override
    public InventoryType getSize() {
        return InventoryType.CHEST_3_ROW;
    }
}
