package net.swofty.types.generic.item;

import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;

import java.util.Arrays;
import java.util.List;

public class ChestImpl {

    private static final Tag<List<ItemStack>> ITEMS_TAG = Tag.ItemStack("items").list();

    private final Instance instance;
    private final Point position;

    private final Block chestBlock;

    @Getter
    private final InventoryType inventoryType;

    @Getter
    private ItemStack[] items;

    public ChestImpl(Instance instance, Point position) {
        this.instance = instance;
        this.position = position;
        this.chestBlock = instance.getBlock(position);
        this.inventoryType = InventoryType.CHEST_3_ROW; // todo : implement double chest
        this.items = new ItemStack[inventoryType.getSize()];
        load();
    }

    private void load() {
        List<ItemStack> itemsList = chestBlock.getTag(ITEMS_TAG);

        if (itemsList == null) {
            ItemStack[] newItems = new ItemStack[inventoryType.getSize()];
            Arrays.fill(newItems, ItemStack.AIR);
            this.items = newItems;
        } else {
            this.items = itemsList.toArray(ItemStack[]::new);
        }
    }

    private void save() {
        instance.setBlock(position, chestBlock.withTag(ITEMS_TAG, List.of(items)));
    }

    public void setItem(int slot, ItemStack stack) {
        items[slot] = stack;
        save();
    }

    public void removeItem(int slot) {
        items[slot] = ItemStack.AIR;
        save();
    }

    public ItemStack getItem(int slot) {
        return items[slot];
    }


    public List<ItemStack> getItemsList() {
        return List.of(items);
    }

}
