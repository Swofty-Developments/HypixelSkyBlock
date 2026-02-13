package net.swofty.type.bedwarsgame.gui;

import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.data.datapoints.DatapointBedWarsHotbar;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.Layouts;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.util.HashMap;
import java.util.Map;

public class HotbarManagerView extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.withString((_, _) -> "Hotbar Manager", InventoryType.CHEST_6_ROW);
    }

    private final int[] SELECTABLE_SLOTS = new int[]{10, 11, 12, 13, 14, 15, 16, 17};
    private final int[] DROPPABLE_SLOTS = new int[]{27, 28, 29, 30, 31, 32, 33, 34, 35};

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.back(layout, 48, ctx);
        layout.slot(
            50,
            (s, c) -> ItemStackCreator.getStack("§cReset to Default", Material.BARRIER, 1, "§7Reset your hotbar to the default."),
            (clickContext, c) -> {
                BedWarsPlayer player = (BedWarsPlayer) clickContext.player();
                BedWarsDataHandler dataHandler = player.getBedWarsDataHandler();
                dataHandler.get(BedWarsDataHandler.Data.HOTBAR_LAYOUT, DatapointBedWarsHotbar.class).setValue(DatapointBedWarsHotbar.defaultHotbar);
                player.getInventory().setCursorItem(ItemStack.AIR);
                c.session(DefaultState.class).refresh();
            }
        );
        layout.slots(
            Layouts.rectangle(18, 26),
            (_, _) -> ItemStackCreator.getStack("§8⬆ §7Categories", Material.GRAY_STAINED_GLASS_PANE, 1, "§8⬇ §7Hotbar")
        );


        for (int slot : SELECTABLE_SLOTS) {
            DatapointBedWarsHotbar.HotbarItemType itemType = DatapointBedWarsHotbar.HotbarItemType.fromOrdinal(slot - 10);
            if (itemType == null) {
                itemType = DatapointBedWarsHotbar.HotbarItemType.BLOCKS;
            }
            DatapointBedWarsHotbar.HotbarItemType finalItemType = itemType;
            layout.slot(
                slot,
                (s, c) -> ItemStackCreator.getStack("§a" + finalItemType.pretty(), finalItemType.getMaterial(), 1, "§7Drag this to a hotbar slot below to",
                    "§7favor that slot when purchasing an",
                    "§7item in this category or on spawn.",
                    "",
                    "§eClick to drag!"), (clickContext, context) -> {
                    BedWarsPlayer player = (BedWarsPlayer) clickContext.player();
                    ItemStack cursorItem = player.getInventory().getCursorItem();
                    if (!cursorItem.isAir() && getItemTypeFromCursor(cursorItem) == null) {
                        return;
                    }

                    player.getInventory().setCursorItem(buildCursorItem(finalItemType).build());
                });
        }

        for (int slot : DROPPABLE_SLOTS) {
            final byte hotbarSlot = (byte) (slot - 27);
            layout.slot(slot,
                (_, c) -> {
                    BedWarsPlayer player = (BedWarsPlayer) c.player();
                    Map<Byte, DatapointBedWarsHotbar.HotbarItemType> currentLayout = getHotbarLayout(player);
                    DatapointBedWarsHotbar.HotbarItemType type = currentLayout.get(hotbarSlot);

                    if (type == null) {
                        return ItemStackCreator.getStack("§7Empty Hotbar Slot", Material.AIR, 1,
                            "§7Drag a category item here to",
                            "§7prioritize this hotbar slot.");
                    }

                    return ItemStackCreator.getStack("§a" + type.pretty(), type.getMaterial(), 1,
                        "§7" + type.pretty() + " items will prioritize this slot!",
                        "",
                        "§eClick to remove!");
                },
                (clickContext, c) -> {
                    BedWarsPlayer player = (BedWarsPlayer) clickContext.player();
                    Map<Byte, DatapointBedWarsHotbar.HotbarItemType> currentLayout = getHotbarLayout(player);
                    DatapointBedWarsHotbar.HotbarItemType cursorType = getItemTypeFromCursor(player.getInventory().getCursorItem());

                    if (cursorType != null) {
                        currentLayout.put(hotbarSlot, cursorType);
                        setHotbarLayout(player, currentLayout);
                        player.getInventory().setCursorItem(ItemStack.AIR);
                        c.session(DefaultState.class).refresh();
                        return;
                    }

                    DatapointBedWarsHotbar.HotbarItemType existingType = currentLayout.get(hotbarSlot);
                    if (existingType == null) {
                        return;
                    }

                    currentLayout.remove(hotbarSlot);
                    setHotbarLayout(player, currentLayout);
                    c.session(DefaultState.class).refresh();
                }
            );
        }
    }

    private Map<Byte, DatapointBedWarsHotbar.HotbarItemType> getHotbarLayout(BedWarsPlayer player) {
        return new HashMap<>(
            player.getBedWarsDataHandler()
                .get(BedWarsDataHandler.Data.HOTBAR_LAYOUT, DatapointBedWarsHotbar.class)
                .getValue()
        );
    }

    private void setHotbarLayout(BedWarsPlayer player, Map<Byte, DatapointBedWarsHotbar.HotbarItemType> layout) {
        player.getBedWarsDataHandler()
            .get(BedWarsDataHandler.Data.HOTBAR_LAYOUT, DatapointBedWarsHotbar.class)
            .setValue(new HashMap<>(layout));
    }

    private ItemStack.Builder buildCursorItem(DatapointBedWarsHotbar.HotbarItemType itemType) {
        return ItemStackCreator.getStack("§a" + itemType.pretty(), itemType.getMaterial(), 1,
            "§7Drag this to the hotbar slot you want to favor " + itemType.pretty() + " items in!");
    }

    private DatapointBedWarsHotbar.HotbarItemType getItemTypeFromCursor(ItemStack cursorItem) {
        if (cursorItem == null || cursorItem.isAir()) {
            return null;
        }

        var customName = cursorItem.get(DataComponents.CUSTOM_NAME);
        if (customName == null) {
            return null;
        }

        String plainName = StringUtility.stripColor(StringUtility.getTextFromComponent(customName));
        for (DatapointBedWarsHotbar.HotbarItemType itemType : DatapointBedWarsHotbar.HotbarItemType.values()) {
            if (itemType.pretty().equalsIgnoreCase(plainName)) {
                return itemType;
            }
        }

        return null;
    }

}
