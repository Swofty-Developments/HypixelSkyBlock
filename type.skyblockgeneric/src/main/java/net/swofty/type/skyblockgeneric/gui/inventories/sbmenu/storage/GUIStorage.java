package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.storage;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointBackpacks;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointStorage;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.BackpackComponent;
import net.swofty.type.skyblockgeneric.item.components.SkullHeadComponent;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Locale;
import java.util.Map;

public class GUIStorage extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.translatable("gui_sbmenu.storage.title", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 48, ctx);
        Components.close(layout, 49);

        layout.slot(4, (s, c) -> {
            Locale l = c.player().getLocale();
            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.storage.ender_chest", l), Material.ENDER_CHEST, 1,
                I18n.iterable("gui_sbmenu.storage.ender_chest.lore"));
        });

        layout.slot(22, (s, c) -> {
            Locale l = c.player().getLocale();
            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.storage.backpacks", l), Material.CHEST, 1,
                I18n.iterable("gui_sbmenu.storage.backpacks.lore"));
        });

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        DatapointStorage.PlayerStorage storage = player.getSkyblockDataHandler().get(
                SkyBlockDataHandler.Data.STORAGE, DatapointStorage.class
        ).getValue();

        if (!storage.hasPage(1)) {
            storage.addPage(1);
            storage.addPage(2);
        }

        for (int enderSlot = 9; enderSlot < 18; enderSlot++) {
            int page = enderSlot - 8;

            layout.slot(enderSlot, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                Locale l = p.getLocale();
                DatapointStorage.PlayerStorage playerStorage = p.getSkyblockDataHandler().get(
                        SkyBlockDataHandler.Data.STORAGE, DatapointStorage.class
                ).getValue();

                if (!playerStorage.hasPage(page))
                    return ItemStackCreator.getStack(I18n.string("gui_sbmenu.storage.locked_page", l), Material.RED_STAINED_GLASS_PANE, 1,
                        I18n.iterable("gui_sbmenu.storage.locked_page.lore"));

                Material material = playerStorage.getPage(page).display;

                return ItemStackCreator.getStack(I18n.string("gui_sbmenu.storage.ender_chest_page", l, Component.text(String.valueOf(page))),
                        material, page,
                    I18n.iterable("gui_sbmenu.storage.ender_chest_page.lore"));
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

        DatapointBackpacks.PlayerBackpacks backpacks = player.getSkyblockDataHandler().get(
                SkyBlockDataHandler.Data.BACKPACKS, DatapointBackpacks.class
        ).getValue();

        Map<Integer, UnderstandableSkyBlockItem> backpackItems = backpacks.getBackpacks();

        for (int backpackSlot = 27; backpackSlot <= 44; backpackSlot++) {
            int slot = backpackSlot - 26;

            if (backpacks.getUnlockedSlots() < slot) {
                layout.slot(backpackSlot, (s, c) -> {
                    Locale l = c.player().getLocale();
                    return ItemStackCreator.getStack(I18n.string("gui_sbmenu.storage.locked_backpack", l, Component.text(String.valueOf(slot))),
                        Material.GRAY_DYE, 1,
                        I18n.iterable("gui_sbmenu.storage.locked_backpack.lore"));
                });
                continue;
            }

            if (!backpackItems.containsKey(slot)) {
                layout.slot(backpackSlot, (s, c) -> {
                            Locale l = c.player().getLocale();
                        return ItemStackCreator.getStack(I18n.string("gui_sbmenu.storage.empty_backpack", l, Component.text(String.valueOf(slot))),
                                Material.BROWN_STAINED_GLASS_PANE, slot,
                            I18n.iterable("gui_sbmenu.storage.empty_backpack.lore"));
                        },
                        (click, c) -> handleEmptyBackpackSlotClick(click, c, slot));
                continue;
            }

            SkyBlockItem item = new SkyBlockItem(backpackItems.get(slot));

            layout.slot(backpackSlot, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                Locale l = p.getLocale();
                String itemName = item.getAttributeHandler().getRarity().getColor() +
                        item.getAttributeHandler().getPotentialType().getDisplayName();
                String slots = String.valueOf(item.getComponent(BackpackComponent.class).getRows() * 9);
                return ItemStackCreator.getStackHead(I18n.string("gui_sbmenu.storage.backpack_slot", l, Component.text(String.valueOf(slot))),
                        item.getComponent(SkullHeadComponent.class).getSkullTexture(item), slot,
                    I18n.iterable("gui_sbmenu.storage.backpack_slot.lore", Component.text(itemName), Component.text(slots)));
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
                        p.sendMessage(I18n.string("gui_sbmenu.storage.msg.not_empty", p.getLocale(), Component.text(String.valueOf(slot))));
                        return;
                    }

                    p.sendMessage(I18n.string("gui_sbmenu.storage.msg.removed", p.getLocale(), Component.text(String.valueOf(slot))));
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

        Locale l = player.getLocale();
        player.sendMessage(I18n.string("gui_sbmenu.storage.msg.placing", l, Component.text(String.valueOf(slot))));
        player.sendMessage(I18n.string("gui_sbmenu.storage.msg.success", l));
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
