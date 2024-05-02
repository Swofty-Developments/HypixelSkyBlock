package net.swofty.types.generic.gui.inventory.inventories;


import net.kyori.adventure.sound.Sound;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.sound.SoundEvent;
import net.swofty.types.generic.chest.Chest;
import net.swofty.types.generic.chest.ChestAnimationType;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.stream.IntStream;

public class GUIChest extends SkyBlockInventoryGUI {

    private final Chest chest;

    public GUIChest(Chest chest) {
        super(chest.getName(), chest.getSize());
        this.chest = chest;
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        IntStream.range(0 , chest.getItems().size()).forEach(index->{
            set(new GUIItem(index) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getFromStack(chest.getItem(index));
                }

                @Override
                public boolean canPickup() {
                    return true;
                }
            });
        });

    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        e.getPlayer().playSound(Sound.sound(SoundEvent.BLOCK_CHEST_CLOSE, Sound.Source.RECORD, 1f, 1f));
        ChestAnimationType.CLOSE.play(chest.getInstance(), chest.getPosition());

        Inventory inventory = e.getInventory();
        IntStream.range(0, inventory.getItemStacks().length).forEach(i -> chest.setItem(i, inventory.getItemStack(i)));
    }

    @Override
    public boolean allowHotkeying() {
        return true;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }
}
