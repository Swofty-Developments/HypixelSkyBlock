package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.fasttravel;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.data.datapoints.DatapointStringList;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.warps.ScrollUnlockReason;
import net.swofty.type.skyblockgeneric.warps.TravelScrollIslands;
import net.swofty.type.skyblockgeneric.warps.TravelScrollType;

import java.util.ArrayList;
import java.util.List;

public class GUIFastTravelSubMenu extends StatelessView {
    private static final int[] SLOTS = new int[]{20, 21, 22, 23, 24};
    private final TravelScrollIslands island;

    public GUIFastTravelSubMenu(TravelScrollIslands island) {
        this.island = island;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(StringUtility.toNormalCase(island.getInternalName()) + " Warps", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 40);
        Components.back(layout, 39, ctx);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        boolean shouldBePaper = player.getToggles().get(DatapointToggles.Toggles.ToggleType.PAPER_ICONS);

        // Main island warp
        layout.slot(13, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            boolean isPaper = p.getToggles().get(DatapointToggles.Toggles.ToggleType.PAPER_ICONS);

            List<String> lore = new ArrayList<>();
            lore.add("§8/warp " + island.getInternalName());
            lore.add(" ");

            StringUtility.splitByWordAndLength(island.getDescription().apply(true), 30).forEach(line -> {
                lore.add("§7" + line);
            });
            lore.add(" ");

            if (island.getAssociatedSkill() != null) {
                lore.add("§7Main skill: §b" + island.getAssociatedSkill());
                lore.add("§7Island tier: §e" + island.getIslandTier());
                lore.add(" ");
            }

            lore.add("§eClick to warp!");

            if (isPaper) {
                return ItemStackCreator.getStack(island.getDescriptiveName(), Material.PAPER, 1, lore);
            } else {
                return ItemStackCreator.getStackHead(island.getDescriptiveName(), island.getTexture(), 1, lore);
            }
        }, (click, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            p.closeInventory();
            p.sendMessage("§7Warping you to " + island.getDescriptiveName() + "§7...");
            p.asProxyPlayer().transferToWithIndication(island.getServerType()).thenRun(() -> {
                p.asProxyPlayer().sendMessage("§7You have been warped to " + island.getDescriptiveName() + "§7!");
            });
        });

        List<TravelScrollType> scrolls = island.getAssociatedScrolls();
        for (int i = 0; i < scrolls.size() && i < SLOTS.length; i++) {
            TravelScrollType scroll = scrolls.get(i);
            int slot = SLOTS[i];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                boolean isUnlocked = p.getSkyblockDataHandler()
                        .get(SkyBlockDataHandler.Data.USED_SCROLLS, DatapointStringList.class)
                        .getValue()
                        .contains(scroll.getInternalName());
                boolean isPaper = p.getToggles().get(DatapointToggles.Toggles.ToggleType.PAPER_ICONS);

                List<String> lore = new ArrayList<>();
                lore.add("§8/warp " + scroll.getInternalName());
                lore.add(" ");

                StringUtility.splitByWordAndLength(scroll.getDescription(), 50).forEach(line -> {
                    lore.add("§7" + line);
                });
                lore.add(" ");

                if (!isUnlocked) {
                    ScrollUnlockReason unlockReason = scroll.getUnlockReason();
                    lore.add(unlockReason.getTitleReason());
                    lore.add(unlockReason.getSubReason());
                    lore.add(" ");
                    lore.add("§cWarp not unlocked!");
                } else {
                    lore.add("§eClick to warp!");
                }

                if (isPaper) {
                    return ItemStackCreator.getStack(scroll.getDisplayName(),
                            isUnlocked ? Material.PAPER : Material.BEDROCK, 1, lore);
                } else {
                    return ItemStackCreator.getStackHead(scroll.getDisplayName(),
                            isUnlocked ? scroll.getHeadTexture() : "da99b05b9a1db4d29b5e673d77ae54a77eab66818586035c8a2005aeb810602a",
                            1, lore);
                }
            }, (click, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                boolean isUnlocked = p.getSkyblockDataHandler()
                        .get(SkyBlockDataHandler.Data.USED_SCROLLS, DatapointStringList.class)
                        .getValue()
                        .contains(scroll.getInternalName());

                if (!isUnlocked) {
                    p.sendMessage("§cYou haven't unlocked this fast travel destination!");
                    return;
                }

                p.closeInventory();
                p.sendMessage("§7Warping you to " + scroll.getDescription() + "§7...");
                p.asProxyPlayer().transferToWithIndication(island.getServerType()).thenRun(() -> {
                    p.asProxyPlayer().sendMessage("§7You have been warped to " + scroll.getDisplayName() + "§7!");
                    p.asProxyPlayer().teleport(scroll.getLocation());
                });
            });
        }
    }
}
