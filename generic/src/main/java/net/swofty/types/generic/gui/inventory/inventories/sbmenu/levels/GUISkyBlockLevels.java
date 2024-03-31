package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.text.DecimalFormat;

public class GUISkyBlockLevels extends SkyBlockInventoryGUI {
    public GUISkyBlockLevels() {
        super("SkyBlock Leveling", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUISkyBlockMenu()));

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                SkyBlockLevelRequirement level = player.getSkyBlockExperience().getLevel();
                int completedChallenges = player.getSkyBlockExperience().getCompletedExperienceCauses().size();
                int totalChallenges = SkyBlockLevelCause.getAmountOfCauses();

                return ItemStackCreator.getStack("§aYour SkyBlock Level Ranking",
                        Material.PAINTING, 1,
                        "§8Classic Mode",
                        " ",
                        "§7Your level: " + level.getColor() + level,
                        "§7You have: §b" + player.getSkyBlockExperience().getTotalXP() + " XP",
                        " ",
                        "§7You have completed §3" + (new DecimalFormat("##.##").format((double) completedChallenges / totalChallenges * 100)) + "% §7of the total",
                        "§7SkyBlock XP Tasks.");
            }
        });
        set(new GUIClickableItem(25) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUILevelsGuide().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aSkyBlock Guide", Material.FILLED_MAP, 1,
                        "§7Your §6SkyBlock Guide §7tracks the",
                        "§7progress you have made through",
                        "§7SkyBlock.",
                        " ",
                        "§7Complete tasks within your current",
                        "§7game stage to increase your",
                        "§bSkyBlock Level §7and become a §dMaster",
                        "§7of SkyBlock!",
                        " ",
                        "§eClick to view!");
            }
        });
        set(new GUIClickableItem(43) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIEmblems().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aPrefix Emblems", Material.NAME_TAG, 1,
                        "§7Add some spice by having an emblem",
                        "§7next to your name in chat and in tab!",
                        " ",
                        "§7Emblems are unlocked through various",
                        "§7activities such as leveling up",
                        "§7or completing achievements!",
                        " ",
                        "§7Emblems also show important data",
                        "§7associated with them in chat!",
                        " ",
                        "§eClick to view!");
            }
        });

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
