package net.swofty.types.generic.gui.inventory.inventories.sbmenu.storage;

import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBackpacks;
import net.swofty.types.generic.data.datapoints.DatapointStorage;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.BackpackComponent;
import net.swofty.types.generic.item.components.SkullHeadComponent;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Map;

public class GUIStorage extends SkyBlockAbstractInventory {

    public GUIStorage() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Storage")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Close and back buttons
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To SkyBlock Menu").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockMenu());
                    return true;
                })
                .build());

        // Header items
        attachItem(GUIItem.builder(4)
                .item(ItemStackCreator.getStack("§aEnder Chest", Material.ENDER_CHEST, 1,
                        "§7Store global items you can",
                        "§7access anywhere in your ender",
                        "§7chest.").build())
                .build());

        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack("§aBackpacks", Material.CHEST, 1,
                        "§7Place backpack items in these slots",
                        "§7to use them as additional storage",
                        "§7that can be accessed anywhere.").build())
                .build());

        setupEnderChestSlots(player);
        setupBackpackSlots(player);
    }

    private void setupEnderChestSlots(SkyBlockPlayer player) {
        DatapointStorage.PlayerStorage storage = player.getDataHandler().get(
                DataHandler.Data.STORAGE, DatapointStorage.class
        ).getValue();

        if (!storage.hasPage(1)) {
            storage.addPage(1);
            storage.addPage(2);
        }

        for (int ender_slot = 9; ender_slot < 18; ender_slot++) {
            final int page = ender_slot - 8;

            attachItem(GUIItem.builder(ender_slot)
                    .item(() -> {
                        if (!storage.hasPage(page))
                            return ItemStackCreator.getStack("§cLocked Page", Material.RED_STAINED_GLASS_PANE, 1,
                                    "§7Unlock more Ender Chest pages in",
                                    "§7the community shop!").build();

                        Material material = storage.getPage(page).display;

                        return ItemStackCreator.getStack("§aEnder Chest Page " + page, material, page,
                                " ",
                                "§eLeft-click to open!",
                                "§eRight-click to change icon!").build();
                    })
                    .onClick((ctx, item) -> {
                        if (!storage.hasPage(page)) return true;

                        if (ctx.clickType() == ClickType.RIGHT_CLICK) {
                            ctx.player().openInventory(new GUIInventoryStorageIconSelection(page, this));
                        } else {
                            ctx.player().openInventory(new GUIStoragePage(page));
                        }
                        return true;
                    })
                    .build());
        }
    }

    private void setupBackpackSlots(SkyBlockPlayer player) {
        DatapointBackpacks.PlayerBackpacks backpacks = player.getDataHandler().get(
                DataHandler.Data.BACKPACKS, DatapointBackpacks.class
        ).getValue();

        Map<Integer, UnderstandableSkyBlockItem> backpackItems = backpacks.getBackpacks();

        for (int backpack_slot = 27; backpack_slot <= 44; backpack_slot++) {
            final int slot = backpack_slot - 26;

            if (backpacks.getUnlockedSlots() < slot) {
                attachItem(GUIItem.builder(backpack_slot)
                        .item(ItemStackCreator.getStack("§cLocked Backpack Slot " + slot,
                                Material.GRAY_DYE, 1,
                                "§7Talk to Tia the Fairy to unlock more",
                                "§7Backpack Slots!").build())
                        .build());
                continue;
            }

            if (!backpackItems.containsKey(slot)) {
                attachEmptyBackpackSlot(backpack_slot, slot, backpacks, backpackItems);
                continue;
            }

            attachFilledBackpackSlot(backpack_slot, slot, backpackItems.get(slot), backpackItems);
        }
    }

    private void attachEmptyBackpackSlot(int backpack_slot, int slot,
                                         DatapointBackpacks.PlayerBackpacks backpacks,
                                         Map<Integer, UnderstandableSkyBlockItem> backpackItems) {
        attachItem(GUIItem.builder(backpack_slot)
                .item(ItemStackCreator.getStack("§eEmpty Backpack Slot " + slot,
                        Material.BROWN_STAINED_GLASS_PANE, slot,
                        " ",
                        "§eLeft-click a backpack item on this",
                        "§eslot to place it!").build())
                .onClick((ctx, clickedItem) -> {
                    SkyBlockItem item = new SkyBlockItem(ctx.cursorItem());

                    if (item.isNA() || !item.hasComponent(BackpackComponent.class)) {
                        return true;
                    }

                    backpackItems.put(slot, item.toUnderstandable());
                    ctx.player().getDataHandler().get(DataHandler.Data.BACKPACKS, DatapointBackpacks.class).setValue(
                            new DatapointBackpacks.PlayerBackpacks(backpackItems, backpacks.getUnlockedSlots())
                    );

                    ctx.player().sendMessage("§ePlacing backpack in slot " + slot + "...");
                    ctx.player().sendMessage("§aSuccess!");

                    handleOpen(ctx.player());
                    return false;
                })
                .build());
    }

    private void attachFilledBackpackSlot(int backpack_slot, int slot, UnderstandableSkyBlockItem backpackItem,
                                          Map<Integer, UnderstandableSkyBlockItem> backpackItems) {
        SkyBlockItem item = new SkyBlockItem(backpackItem);

        attachItem(GUIItem.builder(backpack_slot)
                .item(() -> ItemStackCreator.getStackHead("§6Backpack Slot " + slot,
                        item.getComponent(SkullHeadComponent.class).getSkullTexture(item), slot,
                        item.getAttributeHandler().getRarity().getColor() +
                                item.getAttributeHandler().getPotentialType().getDisplayName(),
                        "§7This backpack has §a" + (item.getComponent(BackpackComponent.class).getRows() * 9) + " §7slots.",
                        " ",
                        "§eLeft-click to open!",
                        "§eRight-click to remove!").build())
                .onClick((ctx, clickedItem) -> {
                    if (ctx.clickType() == ClickType.RIGHT_CLICK) {
                        if (!item.getAttributeHandler().getBackpackData().items().isEmpty()
                                && !item.getAttributeHandler().getBackpackData().items()
                                .stream()
                                .map(SkyBlockItem::new).allMatch(SkyBlockItem::isNA)) {
                            ctx.player().sendMessage("§cThe backpack in slot " + slot + " is not empty! Please empty it before removing it.");
                            return true;
                        }

                        ctx.player().sendMessage("§aRemoved backpack from slot " + slot + "!");
                        setItemStack(backpack_slot, PlayerItemUpdater.playerUpdate(ctx.player(), item.getItemStack()).build());
                        backpackItems.remove(slot);
                        return false;
                    }

                    ctx.player().openInventory(new GUIStorageBackpackPage(slot, item));
                    return true;
                })
                .build());
    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {
        // Empty implementation
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {
        // Empty implementation
    }

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        ItemStack stack = event.getClickedItem();
        SkyBlockItem item = new SkyBlockItem(stack);

        if (item.isNA()) return;
        if (!item.hasComponent(BackpackComponent.class))
            event.setCancelled(true);
    }
}