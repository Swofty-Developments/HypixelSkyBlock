package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.fasttravel;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.data.datapoints.DatapointStringList;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.warps.TravelScrollIslands;

import java.util.ArrayList;
import java.util.List;

public class GUIFastTravel extends StatelessView {
    private static final int[] SLOTS = new int[]{
            10, 11, 12, 13, 14, 15, 16,
            20, 21, 22, 23, 24,
            30, 32
    };

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Fast Travel", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        boolean shouldBePaper = player.getToggles().get(DatapointToggles.Toggles.ToggleType.PAPER_ICONS);

        layout.slot(53, (s, c) -> {
            boolean isPaper = ((SkyBlockPlayer) c.player()).getToggles().get(DatapointToggles.Toggles.ToggleType.PAPER_ICONS);
            return ItemStackCreator.getStack("§aPaper Icons", isPaper ? Material.FILLED_MAP : Material.MAP, 1,
                    "§7Use paper icons, which may load this menu",
                    "§7faster on your computer.",
                    " ",
                    "§7Enabled: " + (isPaper ? "§aON" : "§cOFF"),
                    " ",
                    "§eClick to toggle!");
        }, (click, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            p.getToggles().inverse(DatapointToggles.Toggles.ToggleType.PAPER_ICONS);
            c.replace(new GUIFastTravel());
        });

        TravelScrollIslands[] values = TravelScrollIslands.values();
        for (int i = 0; i < values.length && i < SLOTS.length; i++) {
            TravelScrollIslands island = values[i];
            int slot = SLOTS[i];
            boolean hasSubMenu = !island.getAssociatedScrolls().isEmpty();

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                boolean hasUnlockedIsland = p.getSkyblockDataHandler()
                        .get(SkyBlockDataHandler.Data.VISITED_ISLANDS, DatapointStringList.class)
                        .getValue()
                        .contains(island.getInternalName());
                boolean isPaper = p.getToggles().get(DatapointToggles.Toggles.ToggleType.PAPER_ICONS);

                List<String> lore = new ArrayList<>();
                lore.add("§8/warp " + island.getInternalName());
                lore.add(" ");

                StringUtility.splitByWordAndLength(island.getDescription().apply(hasUnlockedIsland), 30).forEach(line -> {
                    lore.add("§7" + line);
                });
                lore.add(" ");

                if (island.getAssociatedSkill() != null) {
                    lore.add("§7Main skill: §b" + island.getAssociatedSkill());
                    lore.add("§7Island tier: §e" + island.getIslandTier());
                    lore.add(" ");
                }

                if (!hasUnlockedIsland) {
                    lore.add("§cWarp not unlocked!");
                } else {
                    if (hasSubMenu) {
                        lore.add("§8Right-Click to warp!");
                        lore.add("§eLeft-Click to open!");
                    } else {
                        lore.add("§eClick to warp!");
                    }
                }

                if (isPaper) {
                    return ItemStackCreator.getStack(island.getDescriptiveName(),
                            hasUnlockedIsland ? Material.PAPER : Material.BEDROCK, 1, lore);
                } else {
                    return ItemStackCreator.getStackHead(island.getDescriptiveName(),
                            hasUnlockedIsland ? island.getTexture() : "da99b05b9a1db4d29b5e673d77ae54a77eab66818586035c8a2005aeb810602a",
                            1, lore);
                }
            }, (click, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                boolean hasUnlockedIsland = p.getSkyblockDataHandler()
                        .get(SkyBlockDataHandler.Data.VISITED_ISLANDS, DatapointStringList.class)
                        .getValue()
                        .contains(island.getInternalName());

                if (!hasUnlockedIsland) {
                    p.sendMessage("§cYou haven't unlocked this fast travel destination!");
                    return;
                }

                if (!hasSubMenu) {
                    p.closeInventory();
                    p.sendMessage("§7Warping you to " + island.getDescriptiveName() + "§7...");
                    p.asProxyPlayer().transferToWithIndication(island.getServerType()).thenRun(() -> {
                        p.asProxyPlayer().sendMessage("§7You have been warped to " + island.getDescriptiveName() + "§7!");
                    });
                    return;
                }

                if (click.click() instanceof Click.Right) {
                    p.closeInventory();
                    p.sendMessage("§7Warping you to " + island.getDescriptiveName() + "§7...");
                    p.asProxyPlayer().transferToWithIndication(island.getServerType()).thenRun(() -> {
                        p.asProxyPlayer().sendMessage("§7You have been warped to " + island.getDescriptiveName() + "§7!");
                    });
                } else {
                    c.push(new GUIFastTravelSubMenu(island));
                }
            });
        }
    }
}
