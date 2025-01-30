package net.swofty.types.generic.gui.inventory.inventories.sbmenu.bags;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.SackComponent;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GUISack extends SkyBlockAbstractInventory {
    private static final String STATE_HAS_ITEMS = "has_items_";
    private static final String STATE_EMPTY = "empty_";

    private final ItemType itemTypeLinker;
    private final Boolean closeGUIButton;

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

    public GUISack(ItemType sack, Boolean closeGUIButton) {
        super(getSackSize(sack).getInventoryType());
        this.itemTypeLinker = sack;
        this.closeGUIButton = closeGUIButton;
        doAction(new SetTitleAction(Component.text(StringUtility.toNormalCase(sack.name()))));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());
        setupNavigationButton();
        setupSackItems(player);
    }

    private void setupNavigationButton() {
        int buttonSlot = switch (getInventoryType()) {
            case CHEST_4_ROW -> 31;
            case CHEST_5_ROW -> 40;
            case CHEST_6_ROW -> 49;
            default -> throw new IllegalStateException("Unexpected inventory type");
        };

        if (!closeGUIButton) {
            attachItem(GUIItem.builder(buttonSlot)
                    .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                            "§7To Sack of Sacks").build())
                    .onClick((ctx, item) -> {
                        ctx.player().openInventory(new GUISackOfSacks());
                        return true;
                    })
                    .build());
        } else {
            attachItem(GUIItem.builder(buttonSlot)
                    .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                    .onClick((ctx, item) -> {
                        ctx.player().closeInventory();
                        return true;
                    })
                    .build());
        }
    }

    private void setupSackItems(SkyBlockPlayer player) {
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
            if (index >= sackItems.size()) break;

            SkyBlockItem skyBlockItem = sackItems.get(index);
            ItemType linker = skyBlockItem.getAttributeHandler().getPotentialType();
            final int finalIndex = index;

            Integer amount = player.getSackItems().getAmount(linker);
            if (amount > 0) {
                doAction(new AddStateAction(STATE_HAS_ITEMS + finalIndex));
            } else {
                doAction(new AddStateAction(STATE_EMPTY + finalIndex));
            }

            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        ItemStack.Builder builder = PlayerItemUpdater.playerUpdate(player, skyBlockItem.getItemStack());
                        ArrayList<String> lore = new ArrayList<>();
                        int currentAmount = player.getSackItems().getAmount(linker);
                        int maxStorage = player.getMaxSackStorage(itemTypeLinker);
                        String color = (currentAmount == maxStorage) ? "§a" : "§e";

                        lore.add("§8" + StringUtility.toNormalCase(itemTypeLinker.name()));
                        lore.add("");
                        lore.add("§7Stored: " + color + currentAmount + "§7/" +
                                StringUtility.shortenNumber(StringUtility.roundTo(maxStorage, 0)));
                        lore.add("");

                        if (currentAmount > 0) {
                            lore.add("§bRight-Click for stack!");
                            lore.add("§eClick to pickup!");
                        } else {
                            lore.add("§8Empty sack!");
                        }

                        return ItemStackCreator.updateLore(builder, lore).build();
                    })
                    .onClick((ctx, clickedItem) -> handleSackItemClick(ctx, linker))
                    .requireStates(STATE_HAS_ITEMS + finalIndex, STATE_EMPTY + finalIndex)
                    .build());

            index++;
        }
    }

    private boolean handleSackItemClick(GUIItem.ClickContext ctx, ItemType linker) {
        SkyBlockPlayer player = ctx.player();
        Integer amount = player.getSackItems().getAmount(linker);

        if (amount == 0) return false;

        if (ctx.clickType() == ClickType.RIGHT_CLICK) {
            int stackAmount = Math.min(amount, 64);
            player.getSackItems().decrease(linker, stackAmount);
            SkyBlockItem itemAdded = new SkyBlockItem(linker);
            itemAdded.setAmount(stackAmount);
            player.addAndUpdateItem(itemAdded);
        } else if (ctx.clickType() == ClickType.LEFT_CLICK) {
            int airSlots = (int) Arrays.stream(player.getInventory().getItemStacks())
                    .filter(ItemStack::isAir)
                    .count();
            int maxSpace = 64 * airSlots;
            int transferAmount = Math.min(amount, maxSpace);

            player.getSackItems().decrease(linker, transferAmount);

            while (transferAmount > 0) {
                int stackSize = Math.min(transferAmount, 64);
                SkyBlockItem itemAdded = new SkyBlockItem(linker);
                itemAdded.setAmount(stackSize);
                player.addAndUpdateItem(itemAdded);
                transferAmount -= stackSize;
            }
        }

        player.openInventory(new GUISack(itemTypeLinker, closeGUIButton));
        return true;
    }

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

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}
}