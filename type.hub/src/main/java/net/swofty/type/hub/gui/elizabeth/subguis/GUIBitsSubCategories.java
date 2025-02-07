package net.swofty.type.hub.gui.elizabeth.subguis;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.type.hub.gui.elizabeth.CommunityShopItem;
import net.swofty.types.generic.data.datapoints.DatapointToggles;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIBitsSubCategories extends SkyBlockAbstractInventory {
    private static final int[] DISPLAY_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    };

    private final List<CommunityShopItem> items;
    private final String guiName;
    private final SkyBlockAbstractInventory previousGUI;

    public GUIBitsSubCategories(List<CommunityShopItem> items, String guiName, SkyBlockAbstractInventory previousGUI) {
        super(InventoryType.CHEST_5_ROW);
        this.items = items;
        this.guiName = guiName;
        this.previousGUI = previousGUI;
        doAction(new SetTitleAction(Component.text("Bits Shop - " + guiName)));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        // Set border
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build(), 0, 44);

        // Back button
        attachItem(GUIItem.builder(40)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To " + previousGUI.getTitleAsString()).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(previousGUI);
                    return true;
                })
                .build());

        setupShopItems();
    }

    private void setupShopItems() {
        int index = 0;
        for (CommunityShopItem shopItem : items) {
            if (index >= DISPLAY_SLOTS.length) break;

            int slot = DISPLAY_SLOTS[index];
            attachShopItemToSlot(slot, shopItem);
            index++;
        }
    }

    private void attachShopItemToSlot(int slot, CommunityShopItem shopItem) {
        ItemType item = shopItem.getItemType();
        int price = shopItem.getPrice();
        int amount = shopItem.getAmount();

        attachItem(GUIItem.builder(slot)
                .item(() -> {
                    SkyBlockItem skyBlockItem = new SkyBlockItem(item);
                    ItemStack.Builder itemStack = new NonPlayerItemUpdater(skyBlockItem).getUpdatedItem();
                    itemStack.amount(amount);

                    ArrayList<String> lore = new ArrayList<>(itemStack.build()
                            .get(ItemComponent.LORE).stream()
                            .map(StringUtility::getTextFromComponent)
                            .toList());
                    lore.add(" ");
                    lore.add("§7Cost");
                    lore.add("§b" + StringUtility.commaify(price) + " Bits");
                    lore.add(" ");
                    lore.add("§eClick to trade!");

                    return ItemStackCreator.updateLore(itemStack, lore).build();
                })
                .onClick((ctx, clickedItem) -> {
                    SkyBlockPlayer player = ctx.player();
                    if (player.getBits() >= price) {
                        SkyBlockItem skyBlockItem = new SkyBlockItem(item);
                        ItemStack.Builder itemStack = new NonPlayerItemUpdater(skyBlockItem).getUpdatedItem();
                        itemStack.amount(amount);
                        SkyBlockItem finalItem = new SkyBlockItem(itemStack.build());

                        if (!player.getToggles().get(DatapointToggles.Toggles.ToggleType.PURCHASE_CONFIRMATION_BITS)) {
                            player.addAndUpdateItem(finalItem);
                            player.setBits(player.getBits() - price);
                            player.openInventory(new GUIBitsSubCategories(items, guiName, previousGUI));
                        } else {
                            player.openInventory(new GUIBitsConfirmBuy(finalItem, price));
                        }
                    } else {
                        player.sendMessage("§cYou don't have enough Bits to buy that!");
                    }
                    return true;
                })
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
    }
}