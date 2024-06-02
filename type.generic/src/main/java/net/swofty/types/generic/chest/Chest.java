package net.swofty.types.generic.chest;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;

import java.util.List;

public interface Chest {

    String getName();

    Instance getInstance();

    Point[] getPosition();

    ChestType getType();

    void setItem(int slot, ItemStack stack);

    void removeItem(int slot);

    ItemStack getItem(int slot);

    List<ItemStack> getItems();

    InventoryType getSize();

    void update();

}
