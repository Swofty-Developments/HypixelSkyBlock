package net.swofty.types.generic.gui.inventory.inventories.sbmenu.fasttravel;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
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
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.warps.ScrollUnlockReason;
import net.swofty.types.generic.warps.TravelScrollIslands;
import net.swofty.types.generic.warps.TravelScrollType;

import java.util.ArrayList;
import java.util.List;

public class GUIFastTravelSubMenu extends SkyBlockAbstractInventory {
    private static final int[] SLOTS = new int[]{
            20, 21, 22, 23, 24
    };
    private static final String STATE_UNLOCKED = "unlocked";
    private static final String STATE_PAPER_ICONS = "paper_icons";

    private final TravelScrollIslands island;

    public GUIFastTravelSubMenu(TravelScrollIslands island) {
        super(InventoryType.CHEST_5_ROW);
        this.island = island;
        doAction(new SetTitleAction(Component.text(StringUtility.toNormalCase(island.getInternalName()) + " Warps")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Set initial states
        if (player.getToggles().get(DatapointToggles.Toggles.ToggleType.PAPER_ICONS)) {
            doAction(new AddStateAction(STATE_PAPER_ICONS));
        }

        // Back button
        attachItem(GUIItem.builder(39)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Fast Travel").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIFastTravel());
                    return true;
                })
                .build());

        // Close button
        attachItem(GUIItem.builder(40)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Main island warp
        setupIslandWarp();

        // Scroll warps
        setupScrollWarps(player);
    }

    private void setupIslandWarp() {
        attachItem(GUIItem.builder(13)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§8/warp " + island.getInternalName());
                    lore.add(" ");

                    StringUtility.splitByWordAndLength(island.getDescription().apply(true), 30)
                            .forEach(line -> lore.add("§7" + line));
                    lore.add(" ");

                    if (island.getAssociatedSkill() != null) {
                        lore.add("§7Main skill: §b" + island.getAssociatedSkill());
                        lore.add("§7Island tier: §e" + island.getIslandTier());
                        lore.add(" ");
                    }

                    lore.add("§eClick to warp!");

                    if (hasState(STATE_PAPER_ICONS)) {
                        return ItemStackCreator.getStack(
                                island.getDescriptiveName(),
                                Material.PAPER,
                                1, lore).build();
                    } else {
                        return ItemStackCreator.getStackHead(
                                island.getDescriptiveName(),
                                island.getTexture(),
                                1, lore).build();
                    }
                })
                .onClick((ctx, item) -> {
                    SkyBlockPlayer player = ctx.player();
                    player.closeInventory();
                    player.sendMessage("§7Warping you to " + island.getDescriptiveName() + "§7...");

                    player.asProxyPlayer().transferToWithIndication(island.getServerType()).thenRun(() -> {
                        player.asProxyPlayer().sendMessage("§7You have been warped to " + island.getDescriptiveName() + "§7!");
                    });
                    return true;
                })
                .build());
    }

    private void setupScrollWarps(SkyBlockPlayer player) {
        List<TravelScrollType> scrolls = island.getAssociatedScrolls();
        for (TravelScrollType scroll : scrolls) {
            int slot = SLOTS[scrolls.indexOf(scroll)];
            String unlockState = STATE_UNLOCKED + "_" + scroll.getInternalName();

            boolean isUnlocked = player.getDataHandler()
                    .get(DataHandler.Data.USED_SCROLLS, DatapointStringList.class)
                    .getValue()
                    .contains(scroll.getInternalName());

            if (isUnlocked) {
                doAction(new AddStateAction(unlockState));
            }

            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        List<String> lore = new ArrayList<>();
                        lore.add("§8/warp " + scroll.getInternalName());
                        lore.add(" ");

                        StringUtility.splitByWordAndLength(scroll.getDescription(), 30)
                                .forEach(line -> lore.add("§7" + line));
                        lore.add(" ");

                        if (!hasState(unlockState)) {
                            ScrollUnlockReason unlockReason = scroll.getUnlockReason();
                            lore.add(unlockReason.getTitleReason());
                            lore.add(unlockReason.getSubReason());
                            lore.add(" ");
                            lore.add("§cWarp not unlocked!");
                        } else {
                            lore.add("§eClick to warp!");
                        }

                        if (hasState(STATE_PAPER_ICONS)) {
                            return ItemStackCreator.getStack(
                                    scroll.getDisplayName(),
                                    hasState(unlockState) ? Material.PAPER : Material.BEDROCK,
                                    1, lore).build();
                        } else {
                            return ItemStackCreator.getStackHead(
                                    scroll.getDisplayName(),
                                    hasState(unlockState) ? scroll.getHeadTexture() :
                                            "da99b05b9a1db4d29b5e673d77ae54a77eab66818586035c8a2005aeb810602a",
                                    1, lore).build();
                        }
                    })
                    .onClick((ctx, clickedItem) -> {
                        if (!hasState(unlockState)) {
                            ctx.player().sendMessage("§cYou haven't unlocked this fast travel destination!");
                            return false;
                        }

                        ctx.player().closeInventory();
                        ctx.player().sendMessage("§7Warping you to " + scroll.getDescription() + "§7...");

                        ctx.player().asProxyPlayer().transferToWithIndication(island.getServerType()).thenRun(() -> {
                            ctx.player().asProxyPlayer().sendMessage("§7You have been warped to " + scroll.getDisplayName() + "§7!");
                            ctx.player().asProxyPlayer().teleport(scroll.getLocation());
                        });
                        return true;
                    })
                    .build());
        }
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