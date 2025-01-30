package net.swofty.types.generic.gui.inventory.inventories.shop;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.shop.ShopItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.shop.ShopPrice;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.commons.StringUtility;

import java.util.ArrayList;
import java.util.List;

public final class GUIGenericTradingOptions extends SkyBlockAbstractInventory {
    private final ShopItem item;
    private final SkyBlockShopGUI retPointer;
    private final ShopPrice stackPrice;

    public GUIGenericTradingOptions(ShopItem item, SkyBlockShopGUI retPoint, ShopPrice stackPrice) {
        super(InventoryType.CHEST_6_ROW);
        this.item = item;
        this.retPointer = retPoint;
        this.stackPrice = stackPrice;
        doAction(new SetTitleAction(Component.text("Shop Trading Options")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());

        // Add trade options for different amounts
        attachItem(createTradeItem(20, 1));
        attachItem(createTradeItem(21, 5));
        attachItem(createTradeItem(22, 10));
        attachItem(createTradeItem(23, 32));
        attachItem(createTradeItem(24, 64));

        // Back button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To " + retPointer.getTitle()).build())
                .onClick((ctx, clickedItem) -> {
                    ctx.player().openInventory(retPointer);
                    return false;
                })
                .build());
    }

    private GUIItem createTradeItem(int slot, int amount) {
        ShopPrice totalPrice = stackPrice.multiply(amount);
        SkyBlockItem sbItem = item.getItem();
        ItemStack.Builder itemStack = new NonPlayerItemUpdater(sbItem).getUpdatedItem();

        return GUIItem.builder(slot)
                .item(() -> {
                    List<String> lore = new ArrayList<>(itemStack.build()
                            .get(ItemComponent.LORE).stream()
                            .map(StringUtility::getTextFromComponent)
                            .toList());

                    lore.add("");
                    lore.add("§7Cost");
                    lore.addAll(totalPrice.getGUIDisplay());
                    lore.add("");
                    lore.add("§7Stock");
                    lore.add("§6 " + owner.getShoppingData().getStock(item.getItem().toUnderstandable()) + " §7remaining");
                    lore.add("");
                    lore.add("§eClick to purchase!");

                    String displayName = StringUtility.getTextFromComponent(itemStack.build()
                            .get(ItemComponent.CUSTOM_NAME)
                            .append(Component.text(" §8x" + amount)));

                    return ItemStackCreator.getStack(displayName, itemStack.build().material(), amount, lore).build();
                })
                .onClick((ctx, clickedItem) -> {
                    SkyBlockPlayer player = ctx.player();

                    if (!player.getShoppingData().canPurchase(item.getItem().toUnderstandable(), amount)) {
                        player.sendMessage("§cYou have reached the maximum amount of items you can buy!");
                        return true;
                    }

                    if (!totalPrice.canAfford(player)) {
                        player.sendMessage("§cYou don't have enough " + stackPrice.getNamePlural() + "!");
                        return true;
                    }

                    totalPrice.processPurchase(player);

                    ItemStack.Builder cleanStack = new NonPlayerItemUpdater(sbItem).getUpdatedItem();
                    cleanStack.amount(amount);
                    player.addAndUpdateItem(cleanStack.build());
                    player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));
                    player.getShoppingData().documentPurchase(item.getItem().toUnderstandable(), amount);
                    player.openInventory(this);
                    return false;
                })
                .build();
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