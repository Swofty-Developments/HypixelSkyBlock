package net.swofty.types.generic.gui.inventory.inventories.sbmenu.fasttravel;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointStringList;
import net.swofty.types.generic.data.datapoints.DatapointToggles;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;
import net.swofty.types.generic.warps.ScrollUnlockReason;
import net.swofty.types.generic.warps.TravelScrollIslands;
import net.swofty.types.generic.warps.TravelScrollType;

import java.util.ArrayList;
import java.util.List;

public class GUIFastTravelSubMenu extends SkyBlockInventoryGUI {
    private static final int[] SLOTS = new int[]{
            20, 21, 22, 23, 24
    };
    private TravelScrollIslands island;

    public GUIFastTravelSubMenu(TravelScrollIslands island) {
        super(StringUtility.toNormalCase(island.getInternalName()) + " Warps", InventoryType.CHEST_5_ROW);

        this.island = island;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(39, new GUIFastTravel()));
        set(GUIClickableItem.getCloseItem(40));

        boolean shouldBePaper = e.player().getToggles().get(DatapointToggles.Toggles.ToggleType.PAPER_ICONS);

        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.closeInventory();
                player.sendMessage("§7Warping you to " + island.getDescriptiveName() + "§7...");

                player.asProxyPlayer().transferToWithIndication(island.getServerType()).thenRun(() -> {
                    player.asProxyPlayer().sendMessage("§7You have been warped to " + island.getDescriptiveName() + "§7!");
                });
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
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

                if (shouldBePaper) {
                    return ItemStackCreator.getStack(
                            island.getDescriptiveName(),
                            Material.PAPER,
                            1, lore
                    );
                } else {
                    return ItemStackCreator.getStackHead(
                            island.getDescriptiveName(),
                            island.getTexture(),
                            1, lore
                    );
                }
            }
        });

        List<TravelScrollType> scrolls = island.getAssociatedScrolls();
        for (TravelScrollType scroll : scrolls) {
            int slot = SLOTS[scrolls.indexOf(scroll)];
            boolean isUnlocked = getPlayer().getDataHandler()
                    .get(DataHandler.Data.USED_SCROLLS, DatapointStringList.class)
                    .getValue()
                    .contains(scroll.getInternalName());

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    if (!isUnlocked) {
                        player.sendMessage("§cYou haven't unlocked this fast travel destination!");
                        return;
                    }

                    player.closeInventory();
                    player.sendMessage("§7Warping you to " + scroll.getDescription() + "§7...");

                    player.asProxyPlayer().transferToWithIndication(island.getServerType()).thenRun(() -> {
                        player.asProxyPlayer().sendMessage("§7You have been warped to " + scroll.getDisplayName() + "§7!");
                        player.asProxyPlayer().teleport(scroll.getLocation());
                    });
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    List<String> lore = new ArrayList<>();

                    lore.add("§8/warp " + scroll.getInternalName());
                    lore.add(" ");

                    StringUtility.splitByWordAndLength(scroll.getDescription(), 30).forEach(line -> {
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

                    if (shouldBePaper) {
                        return ItemStackCreator.getStack(
                                scroll.getDisplayName(),
                                isUnlocked ? Material.PAPER : Material.BEDROCK,
                                1, lore
                        );
                    } else {
                        return ItemStackCreator.getStackHead(
                                scroll.getDisplayName(),
                                isUnlocked ? scroll.getHeadTexture() : "da99b05b9a1db4d29b5e673d77ae54a77eab66818586035c8a2005aeb810602a",
                                1, lore
                        );
                    }
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
