package net.swofty.types.generic.gui.inventory.inventories.sbmenu;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.PlayerStatistics;

public class GUISkyBlockProfile extends SkyBlockInventoryGUI {

    public GUISkyBlockProfile() {
        super("Your Equipment and Stats", InventoryType.CHEST_6_ROW);
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(48));
        set(GUIClickableItem.getGoBackItem(49, new GUISkyBlockMenu()));

        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                final PlayerStatistics statistics = player.getStatistics();
                return ItemStackCreator.getStack("§cCombat Stats", Material.DIAMOND_SWORD, (short) 0, 1,
                        "§7Gives you a better chance at",
                        "§7fighting strong monsters.",
                        "",
                        "§c❤ Health §f" + statistics.allStatistics().get(ItemStatistic.HEALTH).intValue(),
                        "§a❈ Defense §f" + statistics.allStatistics().get(ItemStatistic.DEFENSE).intValue(),
                        "§c❁ Strength §f" + statistics.allStatistics().get(ItemStatistic.STRENGTH).intValue(),
                        "§b✎ Intelligence §f" + statistics.allStatistics().get(ItemStatistic.INTELLIGENCE).intValue(),
                        "§9☠ Crit Chance §f" + statistics.allStatistics().get(ItemStatistic.CRIT_CHANCE).intValue(),
                        "§9☣ Crit Damage §f" + statistics.allStatistics().get(ItemStatistic.CRIT_DAMAGE).intValue(),
                        // "§e⚔ Bonus Attack Speed §f" + statistics.allStatistics().get(ItemStatistic.BONUS_ATTACK_SPEED).intValue(),
                        "",
                        "§eClick for details!"
                );
            }
        });
        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                final PlayerStatistics statistics = player.getStatistics();
                return ItemStackCreator.getStack("§eGathering Stats", Material.IRON_PICKAXE, (short) 0, 1,
                        "§7Lets you collect and harvest better",
                        "§7items, or more of them.",
                        "",
                        "§6⸕ Mining Speed §f" + statistics.allStatistics().get(ItemStatistic.MINING_SPEED).intValue(),
                        "",
                        "§eClick for details!"
                );
            }
        });

        set(new GUIClickableItem(24) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                final PlayerStatistics statistics = player.getStatistics();
                return ItemStackCreator.getStack("§3Wisdom Stats", Material.IRON_PICKAXE, (short) 0, 1,
                        "§7Increases the §3XP §7you gain on your",
                        "§7skills",
                        "",
                        "§eClick for details!"
                );
            }
        });

        set(new GUIClickableItem(25) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                final PlayerStatistics statistics = player.getStatistics();
                return ItemStackCreator.getStack("§dMisc Stats", Material.IRON_PICKAXE, (short) 0, 1,
                        "§7Augments various aspects of your",
                        "§7gameplay!",
                        "",
                        "§f✦ Speed " + statistics.allStatistics().get(ItemStatistic.SPEED).intValue(),
                        "§b✯ Magic Find " + statistics.allStatistics().get(ItemStatistic.MAGIC_FIND).intValue(),
                        "",
                        "§eClick for details!"
                );
            }
        });
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
