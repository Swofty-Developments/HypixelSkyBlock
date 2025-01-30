package net.swofty.types.generic.gui.inventory.inventories;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.sound.SoundEvent;
import net.swofty.types.generic.chest.Chest;
import net.swofty.types.generic.chest.ChestAnimationType;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.stream.IntStream;

public class GUIChest extends SkyBlockAbstractInventory {

    private final Chest chest;

    public GUIChest(Chest chest) {
        super(chest.getSize());
        this.chest = chest;
        doAction(new SetTitleAction(Component.text(chest.getName())));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        IntStream.range(0, chest.getItems().size()).forEach(index -> {
            attachItem(GUIItem.builder(index)
                    .item(() -> ItemStackCreator.getFromStack(chest.getItem(index)).build())
                    .onClick((ctx, item) -> {
                        // Return true to allow picking up the item
                        return true;
                    })
                    .build());
        });
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        event.getPlayer().playSound(Sound.sound(SoundEvent.BLOCK_CHEST_CLOSE, Sound.Source.RECORD, 1f, 1f));
        ChestAnimationType.CLOSE.play(chest.getInstance(), chest.getPosition());

        // Save the items back to the chest
        ItemStack[] items = getItemStacks();
        IntStream.range(0, items.length)
                .forEach(i -> chest.setItem(i, items[i]));
    }

    @Override
    public boolean allowHotkeying() {
        return true;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        // Allow interactions with the player's inventory
        event.setCancelled(false);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
    }
}