package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bags;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.SackComponent;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GUISack extends HypixelInventoryGUI {
    ItemType itemTypeLinker;
    Boolean closeGUIButton;

    private static final List<SackSize> SACK_SIZES = List.of(
            new SackSize(14, InventoryType.CHEST_4_ROW, List.of(
                    10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25)),
            new SackSize(21, InventoryType.CHEST_5_ROW, List.of(
                    10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34)),
            new SackSize(28, InventoryType.CHEST_6_ROW, List.of(
                    10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43)),
            new SackSize(45, InventoryType.CHEST_6_ROW, List.of(
                    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44))
    );

    private static SackSize getSackSize(ItemType sack) {
        int sackItemSize = 0;
        SkyBlockItem item = new SkyBlockItem(sack);

        if (item.hasComponent(SackComponent.class)) {
            SackComponent sackInstance = item.getComponent(SackComponent.class);
            sackItemSize = sackInstance.getValidItems().size();
        }

        final int finalSackItemSize = sackItemSize;

        return SACK_SIZES.stream()
                .filter(sackSize -> sackSize.getAmountItems() >= finalSackItemSize)
                .min(Comparator.comparingInt(SackSize::getAmountItems))
                .orElse(SACK_SIZES.getLast());
    }

    public GUISack(ItemType sack, Boolean closeGUIButton) {
        super(StringUtility.toNormalCase(sack.name()), getSackSize(sack).getInventoryType());
        this.itemTypeLinker = sack;
        this.closeGUIButton = closeGUIButton;
    }


    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        if (!closeGUIButton) {
            switch (GUISack.super.size) {
                case InventoryType.CHEST_4_ROW -> set(GUIClickableItem.getGoBackItem(31, new GUISackOfSacks()));
                case InventoryType.CHEST_5_ROW -> set(GUIClickableItem.getGoBackItem(40, new GUISackOfSacks()));
                case InventoryType.CHEST_6_ROW -> set(GUIClickableItem.getGoBackItem(49, new GUISackOfSacks()));
            }
        } else {
            switch (GUISack.super.size) {
                case InventoryType.CHEST_4_ROW -> set(GUIClickableItem.getCloseItem(31));
                case InventoryType.CHEST_5_ROW -> set(GUIClickableItem.getCloseItem(40));
                case InventoryType.CHEST_6_ROW -> set(GUIClickableItem.getCloseItem(49));
            }
        }

        List<SkyBlockItem> sackItems = new ArrayList<>();
        SkyBlockItem item = new SkyBlockItem(itemTypeLinker);

        if (item.hasComponent(SackComponent.class)) {
            SackComponent sackInstance = item.getComponent(SackComponent.class);
            for (ItemType linker : sackInstance.getValidItems()) {
                sackItems.add(new SkyBlockItem(linker));
            }
        }

        int index = 0;
        for (Integer slot : getSackSize(itemTypeLinker).getSlots()) {
            int finalMaxStorage = e.player().getMaxSackStorage(itemTypeLinker);
            int finalIndex = index;

            if (finalIndex < sackItems.size()) {
                SkyBlockItem skyBlockItem = sackItems.get(index);
                ItemType linker = skyBlockItem.getAttributeHandler().getPotentialType();
                set(new GUIClickableItem(slot) {
                    @Override
                    public void run(InventoryPreClickEvent e, net.swofty.type.generic.user.HypixelPlayer p) {
                net.swofty.type.skyblockgeneric.user.SkyBlockPlayer player = (net.swofty.type.skyblockgeneric.user.SkyBlockPlayer) p; 
                        Integer amount = player.getSackItems().getAmount(linker);
                        if (e.getClickType() == ClickType.RIGHT_CLICK) {
                            if (amount == 0) return;
                            if (amount >= 64) {
                                player.getSackItems().decrease(linker, 64);
                                SkyBlockItem itemAdded = new SkyBlockItem(linker);
                                itemAdded.setAmount(64);
                                player.addAndUpdateItem(itemAdded);
                                new GUISack(itemTypeLinker, closeGUIButton).open(player);
                            } else {
                                player.getSackItems().decrease(linker, amount);
                                SkyBlockItem itemAdded = new SkyBlockItem(linker);
                                itemAdded.setAmount(amount);
                                player.addAndUpdateItem(itemAdded);
                                new GUISack(itemTypeLinker, closeGUIButton).open(player);
                            }
                        } else if (e.getClickType() == ClickType.LEFT_CLICK) {
                            int airSlots = 0;
                            for (ItemStack itemStack : player.getInventory().getItemStacks()) {
                                if (itemStack.isAir()) airSlots++;
                            }
                            int maxSpace = 64 * airSlots;
                            if (amount >= maxSpace) {
                                player.getSackItems().decrease(linker, maxSpace);
                                SkyBlockItem itemAdded = new SkyBlockItem(linker);
                                itemAdded.setAmount(64);
                                for (int i = 0; i < airSlots; i++) {
                                    player.addAndUpdateItem(itemAdded);
                                }
                                new GUISack(itemTypeLinker, closeGUIButton).open(player);
                            } else {
                                player.getSackItems().decrease(linker, amount);
                                SkyBlockItem itemAdded = new SkyBlockItem(linker);
                                itemAdded.setAmount(amount);
                                player.addAndUpdateItem(itemAdded);
                                new GUISack(itemTypeLinker, closeGUIButton).open(player);
                            }
                        }
                    }

                    @Override
                    public ItemStack.Builder getItem(net.swofty.type.generic.user.HypixelPlayer p) {
                net.swofty.type.skyblockgeneric.user.SkyBlockPlayer player = (net.swofty.type.skyblockgeneric.user.SkyBlockPlayer) p; 
                        ItemStack.Builder builder = PlayerItemUpdater.playerUpdate(player, skyBlockItem.getItemStack());
                        ArrayList<String> lore = new ArrayList<>();
                        Integer amount = player.getSackItems().getAmount(linker);
                        String color = (amount == finalMaxStorage) ? "§a" : "§e";
                        lore.add("§8" + StringUtility.toNormalCase(itemTypeLinker.name()));
                        lore.add("");
                        lore.add("§7Stored: " + color + amount + "§7/" + StringUtility.shortenNumber(StringUtility.roundTo(finalMaxStorage, 0)));
                        lore.add("");
                        if (amount != 0) {
                            lore.add("§bRight-Click for stack!");
                            lore.add("§eClick to pickup!");
                        } else  {
                            lore.add("§8Empty sack!");
                        }
                        return ItemStackCreator.updateLore(builder, lore);
                    }
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
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
