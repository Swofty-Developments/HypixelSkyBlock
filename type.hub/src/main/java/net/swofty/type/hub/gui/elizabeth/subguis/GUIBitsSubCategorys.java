package net.swofty.type.hub.gui.elizabeth.subguis;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.hub.gui.elizabeth.CommunityShopItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public class GUIBitsSubCategorys extends SkyBlockInventoryGUI {

    private final int[] displaySlots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    };

    private final List<CommunityShopItem> items;
    private final String guiName;
    private final SkyBlockInventoryGUI previousGUI;

    public GUIBitsSubCategorys(List<CommunityShopItem> items, String guiName, SkyBlockInventoryGUI previousGUI) {
        super("Bits Shop - " + guiName, InventoryType.CHEST_5_ROW);
        this.items = items;
        this.guiName = guiName;
        this.previousGUI = previousGUI;
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(40, previousGUI));
        int index = 0;
        for (int slot : displaySlots) {
            if (index < items.size()) {
                items.forEach(shopItem -> {
                    ItemType item = shopItem.getItemType();
                    Integer price = shopItem.getPrice();
                    Integer amount = shopItem.getAmount();
                    set(new GUIClickableItem(slot) {
                        @Override
                        public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                            if (player.getBits() >= price) {
                                SkyBlockItem skyBlockItem = new SkyBlockItem(item);
                                ItemStack.Builder itemStack = new NonPlayerItemUpdater(skyBlockItem).getUpdatedItem();
                                itemStack.amount(amount);
                                SkyBlockItem finalItem = new SkyBlockItem(itemStack.build());
                                if (!player.getPurchaseConfirmationBits()) {
                                    player.addAndUpdateItem(finalItem);
                                    int remainingBits = player.getBits() - price;
                                    player.setBits(remainingBits);
                                    new GUIBitsSubCategorys(items, guiName, previousGUI).open(player);
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
                            itemStack.amount(amount);
                            ArrayList<String> lore = new ArrayList<>(itemStack.build().getLore().stream().map(StringUtility::getTextFromComponent).toList());
                            lore.add(" ");
                            lore.add("§7Cost");
                            lore.add("§b" + StringUtility.commaify(price) + " Bits");
                            lore.add(" ");
                            lore.add("§eClick to trade!");
                            return ItemStackCreator.updateLore(itemStack, lore);
                        }
                    });
                });

            }
            index++;
        }
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }
}
