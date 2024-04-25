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

public class GUIBitsUpgradeComponents extends SkyBlockInventoryGUI {

    private final int[] displaySlots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    };

    private enum BitItems {
        HEAT_CORE(ItemType.HEAT_CORE, 3000),
        HYPER_CATALYST_UPGRADER(ItemType.HYPER_CATALYST_UPGRADER, 300),
        ULTIMATE_CARROT_CANDY_UPGRADE(ItemType.ULTIMATE_CARROT_CANDY_UPGRADE, 8000),
        COLOSSAL_EXPERIENCE_BOTTLE_UPGRADE(ItemType.COLOSSAL_EXPERIENCE_BOTTLE_UPGRADE, 1200),
        JUMBO_BACKPACK_UPGRADE(ItemType.JUMBO_BACKPACK_UPGRADE, 4000),
        MINION_STORAGE_EXPANDER(ItemType.MINION_STORAGE_EXPANDER, 1500),
        ;
        private final ItemType item;
        private final Integer price;
        BitItems(ItemType item, Integer price) {
            this.item = item;
            this.price = price;
        }
    }

    public GUIBitsUpgradeComponents() {
        super("Bits Shop - Upgrade Components", InventoryType.CHEST_5_ROW);
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
                                player.addAndUpdateItem(bitItems.item);
                                Integer remainingBits = player.getBits() - bitItems.price;
                                player.setBits(remainingBits);
                                new GUIBitsShop().open(player);
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
