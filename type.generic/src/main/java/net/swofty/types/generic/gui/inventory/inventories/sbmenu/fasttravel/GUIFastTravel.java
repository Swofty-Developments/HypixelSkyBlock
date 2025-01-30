package net.swofty.types.generic.gui.inventory.inventories.sbmenu.fasttravel;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointStringList;
import net.swofty.types.generic.data.datapoints.DatapointToggles;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.warps.TravelScrollIslands;

import java.util.ArrayList;
import java.util.List;

public class GUIFastTravel extends SkyBlockAbstractInventory {
    private static final int[] SLOTS = new int[]{
            10, 11, 12, 13, 14, 15, 16,
            20, 21, 22, 23, 24,
            30, 32
    };
    private static final String STATE_PAPER_ICONS = "paper_icons";
    private static final String STATE_ISLAND_UNLOCKED_PREFIX = "island_unlocked_";
    private static final String STATE_SUBMENU_PREFIX = "has_submenu_";

    public GUIFastTravel() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Fast Travel")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Set initial states
        if (player.getToggles().get(DatapointToggles.Toggles.ToggleType.PAPER_ICONS)) {
            doAction(new AddStateAction(STATE_PAPER_ICONS));
        }

        // Back button
        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To SkyBlock Menu").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockMenu());
                    return true;
                })
                .build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Paper icons toggle
        setupPaperIconsToggle();

        // Island warps
        setupIslandWarps(player);
    }

    private void setupPaperIconsToggle() {
        attachItem(GUIItem.builder(53)
                .item(() -> ItemStackCreator.getStack(
                        "§aPaper Icons",
                        hasState(STATE_PAPER_ICONS) ? Material.FILLED_MAP : Material.MAP,
                        1,
                        "§7Use paper icons, which may load this menu",
                        "§7faster on your computer.",
                        " ",
                        "§7Enabled: " + (hasState(STATE_PAPER_ICONS) ? "§aON" : "§cOFF"),
                        " ",
                        "§eClick to toggle!").build())
                .onClick((ctx, item) -> {
                    ctx.player().getToggles().inverse(DatapointToggles.Toggles.ToggleType.PAPER_ICONS);
                    ctx.player().openInventory(new GUIFastTravel());
                    return true;
                })
                .build());
    }

    private void setupIslandWarps(SkyBlockPlayer player) {
        TravelScrollIslands[] values = TravelScrollIslands.values();
        for (int i = 0; i < values.length; i++) {
            TravelScrollIslands island = values[i];
            boolean hasSubMenu = !island.getAssociatedScrolls().isEmpty();
            boolean hasUnlockedIsland = player.getDataHandler()
                    .get(DataHandler.Data.VISITED_ISLANDS, DatapointStringList.class)
                    .getValue()
                    .contains(island.getInternalName());

            if (hasUnlockedIsland) {
                doAction(new AddStateAction(STATE_ISLAND_UNLOCKED_PREFIX + island.getInternalName()));
            }
            if (hasSubMenu) {
                doAction(new AddStateAction(STATE_SUBMENU_PREFIX + island.getInternalName()));
            }

            attachItem(createIslandWarpItem(SLOTS[i], island));
        }
    }

    private GUIItem createIslandWarpItem(int slot, TravelScrollIslands island) {
        String unlockedState = STATE_ISLAND_UNLOCKED_PREFIX + island.getInternalName();
        String submenuState = STATE_SUBMENU_PREFIX + island.getInternalName();

        return GUIItem.builder(slot)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§8/warp " + island.getInternalName());
                    lore.add(" ");

                    StringUtility.splitByWordAndLength(island.getDescription().apply(hasState(unlockedState)), 30)
                            .forEach(line -> lore.add("§7" + line));
                    lore.add(" ");

                    if (island.getAssociatedSkill() != null) {
                        lore.add("§7Main skill: §b" + island.getAssociatedSkill());
                        lore.add("§7Island tier: §e" + island.getIslandTier());
                        lore.add(" ");
                    }

                    if (!hasState(unlockedState)) {
                        lore.add("§cWarp not unlocked!");
                    } else {
                        if (hasState(submenuState)) {
                            lore.add("§8Right-Click to warp!");
                            lore.add("§eLeft-Click to open!");
                        } else {
                            lore.add("§eClick to warp!");
                        }
                    }

                    if (hasState(STATE_PAPER_ICONS)) {
                        return ItemStackCreator.getStack(
                                island.getDescriptiveName(),
                                hasState(unlockedState) ? Material.PAPER : Material.BEDROCK,
                                1, lore).build();
                    } else {
                        return ItemStackCreator.getStackHead(
                                island.getDescriptiveName(),
                                hasState(unlockedState) ? island.getTexture() :
                                        "da99b05b9a1db4d29b5e673d77ae54a77eab66818586035c8a2005aeb810602a",
                                1, lore).build();
                    }
                })
                .onClick((ctx, item) -> {
                    if (!hasState(unlockedState)) {
                        ctx.player().sendMessage("§cYou haven't unlocked this fast travel destination!");
                        return false;
                    }

                    if (!hasState(submenuState)) {
                        warpToIsland(ctx.player(), island);
                        return true;
                    }

                    if (ctx.clickType().equals(ClickType.RIGHT_CLICK)) {
                        warpToIsland(ctx.player(), island);
                    } else {
                        ctx.player().openInventory(new GUIFastTravelSubMenu(island));
                    }
                    return true;
                })
                .build();
    }

    private void warpToIsland(SkyBlockPlayer player, TravelScrollIslands island) {
        player.closeInventory();
        player.sendMessage("§7Warping you to " + island.getDescriptiveName() + "§7...");

        player.asProxyPlayer().transferToWithIndication(island.getServerType()).thenRun(() -> {
            player.asProxyPlayer().sendMessage("§7You have been warped to " + island.getDescriptiveName() + "§7!");
        });
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