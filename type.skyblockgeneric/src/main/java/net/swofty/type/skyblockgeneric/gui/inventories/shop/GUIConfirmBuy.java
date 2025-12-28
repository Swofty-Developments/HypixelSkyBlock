package net.swofty.type.skyblockgeneric.gui.inventories.shop;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;

public class GUIConfirmBuy extends HypixelInventoryGUI {
    private final SkyBlockItem item;
    private final int price;

    public GUIConfirmBuy(SkyBlockItem item, int price) {
        super("Confirm", InventoryType.CHEST_3_ROW);
        this.item = item;
        this.price = price;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " "));

        set(new GUIClickableItem(12) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;

                if (skyBlockPlayer.getCoins() >= price) {
                    skyBlockPlayer.addAndUpdateItem(item);
                    skyBlockPlayer.removeCoins(price);

                    skyBlockPlayer.sendMessage("§aYou bought " + item.getDisplayName() + " §afor §6" + price + " Coins§a!");
                } else {
                    skyBlockPlayer.sendMessage("§4You don't have enough coins!");
                }
                skyBlockPlayer.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                ArrayList<String> lore = new ArrayList<>();
                lore.add("§7Buying: " + item.getDisplayItem());
                lore.add("§7Cost: §6" + StringUtility.commaify(price) + " Coins");

                return ItemStackCreator.getStack("§aConfirm", Material.LIME_TERRACOTTA, 1, lore);
            }
        });

        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§4Cancel", Material.RED_TERRACOTTA, 1);
            }
        });

        updateItemStacks(e.inventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
