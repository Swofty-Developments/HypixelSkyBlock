package net.swofty.type.village.gui.elizabeth.subguis;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.village.gui.elizabeth.GUIBitsShop;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;

public class GUIBitsInfernoFuel extends SkyBlockInventoryGUI {

    private final int[] displaySlots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    };

    private enum BitItems {
        INFERNO_FUEL_BLOCK1(ItemType.INFERNO_FUE_BLOCK, 75, 1),
        INFERNO_FUEL_BLOCK64(ItemType.INFERNO_FUE_BLOCK, 3600, 64),
        ;
        private final ItemType item;
        private final Integer price;
        private final Integer amount;
        BitItems(ItemType item, Integer price, Integer amount) {
            this.item = item;
            this.price = price;
            this.amount = amount;
        }
    }

    public GUIBitsInfernoFuel() {
        super("Bits Shop - Inferno Fuel", InventoryType.CHEST_5_ROW);
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(40, new GUIBitsShop()));

        BitItems[] allBitItems = BitItems.values();
        int index = 0;
        for (int slot : displaySlots) {
            if (index + 1 <= BitItems.values().length) {
                BitItems bitItems = allBitItems[index];
                set(new GUIClickableItem(slot) {
                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        if (player.getBits() >= bitItems.price) {
                            if (!player.getPurchaseConfirmationBits()) {
                                SkyBlockItem item = new SkyBlockItem(bitItems.item);
                                ItemStack.Builder itemStack = new NonPlayerItemUpdater(item).getUpdatedItem();
                                itemStack.amount(bitItems.amount);
                                SkyBlockItem skyBlockItem = new SkyBlockItem(itemStack.build());
                                player.addAndUpdateItem(skyBlockItem);
                                Integer remainingBits = player.getBits() - bitItems.price;
                                player.setBits(remainingBits);
                            } else {
                                new GUIBitsConfirmBuy(bitItems.item, bitItems.price).open(player);
                            }
                        } else {
                            player.sendMessage("§cYou don't have enough Bits to buy that!");
                        }
                    }

                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        SkyBlockItem item = new SkyBlockItem(bitItems.item);
                        ItemStack.Builder itemStack = new NonPlayerItemUpdater(item).getUpdatedItem();
                        itemStack.amount(bitItems.amount);
                        ArrayList<String> lore = new ArrayList<>(itemStack.build().getLore().stream().map(StringUtility::getTextFromComponent).toList());
                        lore.add(" ");
                        lore.add("§7Cost");
                        lore.add("§b" + StringUtility.commaify(bitItems.price) + " Bits");
                        lore.add(" ");
                        lore.add("§eClick to trade!");
                        return ItemStackCreator.updateLore(itemStack, lore);
                    }
                });
                index++;
            }
        }
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
