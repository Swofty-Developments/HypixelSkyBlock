package net.swofty.types.generic.gui.inventory.inventories.sbmenu.fasttravel;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointStringList;
import net.swofty.types.generic.data.datapoints.DatapointToggles;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;
import net.swofty.types.generic.warps.TravelScrollIslands;

import java.util.ArrayList;
import java.util.List;

public class GUIFastTravel extends SkyBlockInventoryGUI {
    private static int[] SLOTS = new int[]{
            10, 11, 12, 13, 14, 15, 16,
                20, 21, 22, 23, 24,
                    30,     32
    };

    public GUIFastTravel() {
        super("Fast Travel", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(48, new GUISkyBlockMenu()));
        set(GUIClickableItem.getCloseItem(49));

        boolean shouldBePaper = e.player().getToggles().get(DatapointToggles.Toggles.ToggleType.PAPER_ICONS);

        set(new GUIClickableItem(53) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.getToggles().inverse(DatapointToggles.Toggles.ToggleType.PAPER_ICONS);
                new GUIFastTravel().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aPaper Icons", shouldBePaper ? Material.FILLED_MAP : Material.MAP,
                        1,
                        "§7Use paper icons, which may load this menu",
                        "§7faster on your computer.",
                        " ",
                        "§7Enabled: " + (shouldBePaper ? "§aON" : "§cOFF"),
                        " ",
                        "§eClick to toggle!");
            }
        });

        TravelScrollIslands[] values = TravelScrollIslands.values();
        for (int i = 0; i < values.length; i++) {
            TravelScrollIslands island = values[i];
            boolean hasSubMenu = !island.getAssociatedScrolls().isEmpty();
            boolean hasUnlockedIsland = e.player().getDataHandler()
                    .get(DataHandler.Data.VISITED_ISLANDS, DatapointStringList.class)
                    .getValue()
                    .contains(island.getInternalName());

            set(new GUIClickableItem(SLOTS[i]) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    if (!hasUnlockedIsland) {
                        player.sendMessage("§cYou haven't unlocked this fast travel destination!");
                        return;
                    }

                    if (!hasSubMenu) {
                        player.closeInventory();
                        player.sendMessage("§7Warping you to " + island.getDescriptiveName() + "§7...");

                        player.asProxyPlayer().transferToWithIndication(island.getServerType()).thenRun(() -> {
                            player.asProxyPlayer().sendMessage("§7You have been warped to " + island.getDescriptiveName() + "§7!");
                        });
                        return;
                    }

                    if (e.getClickType().equals(ClickType.RIGHT_CLICK)) {
                        player.closeInventory();
                        player.sendMessage("§7Warping you to " + island.getDescriptiveName() + "§7...");

                        player.asProxyPlayer().transferToWithIndication(island.getServerType()).thenRun(() -> {
                            player.asProxyPlayer().sendMessage("§7You have been warped to " + island.getDescriptiveName() + "§7!");
                        });
                    } else {
                        new GUIFastTravelSubMenu(island).open(player);
                    }
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
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

                    if (shouldBePaper) {
                        return ItemStackCreator.getStack(
                                island.getDescriptiveName(),
                                hasUnlockedIsland ? Material.PAPER : Material.BEDROCK,
                                1, lore
                        );
                    } else {
                        return ItemStackCreator.getStackHead(
                                island.getDescriptiveName(),
                                hasUnlockedIsland ? island.getTexture() : "da99b05b9a1db4d29b5e673d77ae54a77eab66818586035c8a2005aeb810602a",
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
