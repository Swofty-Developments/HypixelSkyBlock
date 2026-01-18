package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.storage;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointBackpacks;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointStorage;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.BackpackComponent;
import net.swofty.type.skyblockgeneric.item.components.SkullHeadComponent;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;

public class GUIStorage extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Storage", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 48, ctx);
        Components.close(layout, 49);

        layout.slot(4, (s, c) -> ItemStackCreator.getStack("§aEnder Chest", Material.ENDER_CHEST, 1,
                "§7Store global items you can",
                "§7access anywhere in your ender",
                "§7chest."));

        layout.slot(22, (s, c) -> ItemStackCreator.getStack("§aBackpacks", Material.CHEST, 1,
                "§7Place backpack items in these slots",
                "§7to use them as additional storage",
                "§7that can be accessed anywhere."));

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        DatapointStorage.PlayerStorage storage = player.getSkyblockDataHandler().get(
                SkyBlockDataHandler.Data.STORAGE, DatapointStorage.class
        ).getValue();

        // Initialize empty storages
        if (!storage.hasPage(1)) {
            storage.addPage(1);
            storage.addPage(2);
        }

        // Ender chest pages (slots 9-17)
        for (int enderSlot = 9; enderSlot < 18; enderSlot++) {
            int page = enderSlot - 8;

            layout.slot(enderSlot, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                DatapointStorage.PlayerStorage playerStorage = p.getSkyblockDataHandler().get(
                        SkyBlockDataHandler.Data.STORAGE, DatapointStorage.class
                ).getValue();

                if (!playerStorage.hasPage(page))
                    return ItemStackCreator.getStack("§cLocked Page", Material.RED_STAINED_GLASS_PANE, 1,
                            "§7Unlock more Ender Chest pages in",
                            "§7the community shop!");

                Material material = playerStorage.getPage(page).display;

                return ItemStackCreator.getStack("§aEnder Chest Page " + page, material, page,
                        " ",
                        "§eLeft-click to open!",
                        "§eRight-click to change icon!");
            }, (click, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                DatapointStorage.PlayerStorage playerStorage = p.getSkyblockDataHandler().get(
                        SkyBlockDataHandler.Data.STORAGE, DatapointStorage.class
                ).getValue();

                if (!playerStorage.hasPage(page)) return;

                if (click.click() instanceof Click.Right) {
                    p.openView(new GUIStorageIconSelection(page), GUIStorageIconSelection.initialState());
                } else {
                    p.openView(new GUIStoragePage(page));
                }
            });
        }

        // Backpack slots (slots 27-44)
        DatapointBackpacks.PlayerBackpacks backpacks = player.getSkyblockDataHandler().get(
                SkyBlockDataHandler.Data.BACKPACKS, DatapointBackpacks.class
        ).getValue();

        Map<Integer, UnderstandableSkyBlockItem> backpackItems = backpacks.getBackpacks();

        for (int backpackSlot = 27; backpackSlot <= 44; backpackSlot++) {
            int slot = backpackSlot - 26;

            if (backpacks.getUnlockedSlots() < slot) {
                layout.slot(backpackSlot, (s, c) -> ItemStackCreator.getStack("§cLocked Backpack Slot " + slot,
                        Material.GRAY_DYE, 1,
                        "§7Talk to Tia the Fairy to unlock more",
                        "§7Backpack Slots!"));
                continue;
            }

            if (!backpackItems.containsKey(slot)) {
                layout.slot(backpackSlot, (s, c) -> ItemStackCreator.getStack("§eEmpty Backpack Slot " + slot,
                                Material.BROWN_STAINED_GLASS_PANE, slot,
                                " ",
                                "§eLeft-click a backpack item on this",
                                "§eslot to place it!"),
                        (click, c) -> handleEmptyBackpackSlotClick(click, c, slot));
                continue;
            }

            SkyBlockItem item = new SkyBlockItem(backpackItems.get(slot));

            layout.slot(backpackSlot, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                return ItemStackCreator.getStackHead("§6Backpack Slot " + slot,
                        item.getComponent(SkullHeadComponent.class).getSkullTexture(item), slot,
                        item.getAttributeHandler().getRarity().getColor() +
                                item.getAttributeHandler().getPotentialType().getDisplayName(),
                        "§7This backpack has §a" + (item.getComponent(BackpackComponent.class).getRows() * 9) + " §7slots.",
                        " ",
                        "§eLeft-click to open!",
                        "§eRight-click to remove!");
            }, (click, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                DatapointBackpacks.PlayerBackpacks playerBackpacks = p.getSkyblockDataHandler().get(
                        SkyBlockDataHandler.Data.BACKPACKS, DatapointBackpacks.class
                ).getValue();
                Map<Integer, UnderstandableSkyBlockItem> playerBackpackItems = playerBackpacks.getBackpacks();
                SkyBlockItem backpackItem = new SkyBlockItem(playerBackpackItems.get(slot));

                if (click.click() instanceof Click.Right) {
                    if (!backpackItem.getAttributeHandler().getBackpackData().items().isEmpty()
                            && !backpackItem.getAttributeHandler().getBackpackData().items()
                            .stream()
                            .map(SkyBlockItem::new).allMatch(SkyBlockItem::isNA)) {
                        p.sendMessage("§cThe backpack in slot " + slot + " is not empty! Please empty it before removing it.");
                        return;
                    }

                    p.sendMessage("§aRemoved backpack from slot " + slot + "!");
                    p.getInventory().setCursorItem(PlayerItemUpdater.playerUpdate(p, backpackItem.getItemStack()).build());

                    playerBackpackItems.remove(slot);
                    c.session(DefaultState.class).refresh();
                    return;
                }

                p.openView(new GUIStorageBackpackPage(slot, backpackItem));
            });
        }
    }

    private void handleEmptyBackpackSlotClick(ClickContext<DefaultState> click, ViewContext ctx, int slot) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        SkyBlockItem item = new SkyBlockItem(player.getInventory().getCursorItem());

        if (item.isNA()) return;
        if (!(item.hasComponent(BackpackComponent.class))) return;

        DatapointBackpacks.PlayerBackpacks backpacks = player.getSkyblockDataHandler().get(
                SkyBlockDataHandler.Data.BACKPACKS, DatapointBackpacks.class
        ).getValue();
        Map<Integer, UnderstandableSkyBlockItem> backpackItems = backpacks.getBackpacks();

        backpackItems.put(slot, item.toUnderstandable());
        player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.BACKPACKS, DatapointBackpacks.class).setValue(
                new DatapointBackpacks.PlayerBackpacks(backpackItems, backpacks.getUnlockedSlots())
        );

        player.sendMessage("§ePlacing backpack in slot " + slot + "...");
        player.sendMessage("§aSuccess!");
        player.getInventory().setCursorItem(ItemStack.AIR);

        ctx.session(DefaultState.class).refresh();
    }

    @Override
    public boolean onBottomClick(ClickContext<DefaultState> click, ViewContext ctx) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        ItemStack cursorItem = player.getInventory().getCursorItem();
        SkyBlockItem cursorItemAsItem = new SkyBlockItem(cursorItem);

        if (cursorItemAsItem.isNA()) return false;
        if (cursorItemAsItem.hasComponent(BackpackComponent.class)) return true;

        ItemStack clickedItem = player.getInventory().getItemStack(click.slot());
        SkyBlockItem clickedItemAsItem = new SkyBlockItem(clickedItem);

        if (clickedItemAsItem.isNA()) return false;
        if (clickedItemAsItem.hasComponent(BackpackComponent.class)) return true;

        return false;
    }
}
