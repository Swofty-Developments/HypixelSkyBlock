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

public class GUIBitsEnrichments extends SkyBlockInventoryGUI {

    private final int[] displaySlots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    };

    private enum BitItems {
        SPEED_ENRICHMENT(ItemType.SPEED_ENRICHMENT, 5000),
        INTELLIGENCE_ENRICHMENT(ItemType.INTELLIGENCE_ENRICHMENT, 5000),
        CRITICAL_DAMAGE_ENRICHMENT(ItemType.CRITICAL_DAMAGE_ENRICHMENT, 5000),
        CRITICAL_CHANCE_ENRICHMENT(ItemType.CRITICAL_CHANCE_ENRICHMENT, 5000),
        STRENGTH_ENRICHMENT(ItemType.STRENGTH_ENRICHMENT, 5000),
        DEFENSE_ENRICHMENT(ItemType.DEFENSE_ENRICHMENT, 5000),
        HEALTH_ENRICHMENT(ItemType.HEALTH_ENRICHMENT, 5000),
        MAGIC_FIND_ENRICHMENT(ItemType.MAGIC_FIND_ENRICHMENT, 5000),
        FEROCITY_ENRICHMENT(ItemType.FEROCITY_ENRICHMENT, 5000),
        SEA_CREATURE_CHANCE_ENRICHMENT(ItemType.SEA_CREATURE_CHANCE_ENRICHMENT, 5000),
        ATTACK_SPEED_ENRICHMENT(ItemType.ATTACK_SPEED_ENRICHMENT, 5000),
        ACCESSORY_ENRICHMENT_SWAPPER(ItemType.ACCESSORY_ENRICHMENT_SWAPPER, 200),
        ;
        private final ItemType item;
        private final Integer price;
        BitItems(ItemType item, Integer price) {
            this.item = item;
            this.price = price;
        }
    }

    public GUIBitsEnrichments() {
        super("Bits Shop - Enrichments", InventoryType.CHEST_5_ROW);
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
