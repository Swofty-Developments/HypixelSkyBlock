package net.swofty.type.hub.gui.elizabeth.subguis;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.hub.gui.elizabeth.GUIBitsShop;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;

public class GUIBitsAbiphone extends SkyBlockInventoryGUI {

    public GUIBitsAbiphone() {
        super("Bits Shop - Abiphone", InventoryType.CHEST_4_ROW);
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(31, new GUIBitsShop()));

        set(new GUIClickableItem(12) {
            Integer price = 6450;
            ItemType item = ItemType.ABIPHONE_CONTACTS_TRIO;
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (player.getBits() >= price) {
                    SkyBlockItem skyBlockItem = new SkyBlockItem(item);
                    ItemStack.Builder itemStack = new NonPlayerItemUpdater(skyBlockItem).getUpdatedItem();
                    SkyBlockItem finalItem = new SkyBlockItem(itemStack.build());
                    if (!player.getPurchaseConfirmationBits()) {
                        player.addAndUpdateItem(finalItem);
                        Integer remainingBits = player.getBits() - price;
                        player.setBits(remainingBits);
                    } else {
                        new GUIBitsConfirmBuy(finalItem, price).open(player);
                    }
                } else {
                    player.sendMessage("§cYou don't have enough Bits to buy that!");
                }
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                SkyBlockItem skyBlockItem = new SkyBlockItem(item);
                ItemStack.Builder itemStack = new NonPlayerItemUpdater(skyBlockItem).getUpdatedItem();
                ArrayList<String> lore = new ArrayList<>(itemStack.build().getLore().stream().map(StringUtility::getTextFromComponent).toList());
                lore.add(" ");
                lore.add("§7Cost");
                lore.add("§b" + StringUtility.commaify(price) + " Bits");
                lore.add(" ");
                lore.add("§eClick to trade!");
                return ItemStackCreator.updateLore(itemStack, lore);
            }
        });
        set(new GUIClickableItem(14) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIBitsAbicases().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§5Abicases", "a3c153c391c34e2d328a60839e683a9f82ad3048299d8bc6a39e6f915cc5a", 1,
                        "§7Any expensive Abiphone needs some",
                        "§7accessories!",
                        " ",
                        "§7Get an Abicase! It keeps your",
                        "§7accessory bag safe while you hold",
                        "§7your Abiphone in your hands.",
                        " ",
                        "§dThree brands to choose from!",
                        "§7Only ONE Abicase will work at a time.",
                        "§eClick to view Abicases!");
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }
}
