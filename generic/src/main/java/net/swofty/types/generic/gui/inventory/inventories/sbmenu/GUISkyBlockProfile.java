package net.swofty.types.generic.gui.inventory.inventories.sbmenu;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.user.statistics.PlayerStatistics;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public class GUISkyBlockProfile extends SkyBlockInventoryGUI {

    public GUISkyBlockProfile() {
        super("Your Equipment and Stats", InventoryType.CHEST_6_ROW);
    }
    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(48, new GUISkyBlockMenu()));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIItem(2) { //Held Item
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (!player.getItemInMainHand().isAir()) {
                    return ItemStackCreator.getFromStack(player.getItemInMainHand());
                } else {
                    return ItemStackCreator.getStack("§7Empty Held Item Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });
        set(new GUIClickableItem(11) { //Helmet
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                SkyBlockItem item = new SkyBlockItem(e.getCursorItem());
                if (!player.getHelmet().isAir() && e.getCursorItem().isAir()) {
                    player.addAndUpdateItem(player.getHelmet());
                    player.setHelmet(ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                } else if (item.getGenericInstance() instanceof StandardItem standardItem && standardItem.getStandardItemType() == StandardItem.StandardItemType.HELMET && player.getHelmet().isAir()) {
                    player.setHelmet(e.getCursorItem());
                    e.getInventory().setCursorItem(player, ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                }
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (!player.getHelmet().isAir()) {
                    return ItemStackCreator.getFromStack(player.getHelmet());
                } else {
                    return ItemStackCreator.getStack("§7Empty Helmet Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });
        set(new GUIClickableItem(20) { //Chestplate
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                SkyBlockItem item = new SkyBlockItem(e.getCursorItem());
                if (!player.getChestplate().isAir() && e.getCursorItem().isAir()) {
                    player.addAndUpdateItem(player.getChestplate());
                    player.setChestplate(ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                } else if (item.getGenericInstance() instanceof StandardItem standardItem && standardItem.getStandardItemType() == StandardItem.StandardItemType.CHESTPLATE && player.getChestplate().isAir()) {
                    player.setChestplate(e.getCursorItem());
                    e.getInventory().setCursorItem(player, ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                }
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (!player.getChestplate().isAir()) {
                    return ItemStackCreator.getFromStack(player.getChestplate());
                } else {
                    return ItemStackCreator.getStack("§7Empty Chestplate Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });
        set(new GUIClickableItem(29) { //Leggings
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                SkyBlockItem item = new SkyBlockItem(e.getCursorItem());
                if (!player.getLeggings().isAir() && e.getCursorItem().isAir()) {
                    player.addAndUpdateItem(player.getLeggings());
                    player.setLeggings(ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                }
                if (item.getGenericInstance() instanceof StandardItem standardItem && standardItem.getStandardItemType() == StandardItem.StandardItemType.LEGGINGS && player.getLeggings().isAir()) {
                    player.setLeggings(e.getCursorItem());
                    e.getInventory().setCursorItem(player, ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                }
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (!player.getLeggings().isAir()) {
                    return ItemStackCreator.getFromStack(player.getLeggings());
                } else {
                    return ItemStackCreator.getStack("§7Empty Leggings Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });
        set(new GUIClickableItem(38) { //Boots
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                SkyBlockItem item = new SkyBlockItem(e.getCursorItem());
                if (!player.getBoots().isAir() && e.getCursorItem().isAir()) {
                    player.addAndUpdateItem(player.getBoots());
                    player.setBoots(ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                }
                if (item.getGenericInstance() instanceof StandardItem standardItem && standardItem.getStandardItemType() == StandardItem.StandardItemType.BOOTS && player.getBoots().isAir()) {
                    player.setBoots(e.getCursorItem());
                    e.getInventory().setCursorItem(player, ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                }
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (!player.getBoots().isAir()) {
                    return ItemStackCreator.getFromStack(player.getBoots());
                } else {
                    return ItemStackCreator.getStack("§7Empty Boots Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });
        set(new GUIClickableItem(47) { //Pet
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIPets().open(player);
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (player.getPetData().getEnabledPet() != null && !player.getPetData().getEnabledPet().getItemStack().isAir()) {
                    SkyBlockItem pet = player.getPetData().getEnabledPet();
                    ItemStack.Builder itemStack = new NonPlayerItemUpdater(pet).getUpdatedItem();
                    return itemStack;
                } else {
                    return ItemStackCreator.getStack("§7Empty Pet Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                PlayerStatistics statistics = player.getStatistics();
                List<String> lore = new ArrayList<>(List.of("§7Gives you a better chance at", "§7fighting strong monsters. "));
                List<ItemStatistic> stats = new ArrayList<>(List.of(ItemStatistic.HEALTH, ItemStatistic.DEFENSE, ItemStatistic.STRENGTH, ItemStatistic.INTELLIGENCE,
                        ItemStatistic.CRIT_CHANCE, ItemStatistic.CRIT_DAMAGE, ItemStatistic.BONUS_ATTACK_SPEED, ItemStatistic.ABILITY_DAMAGE, ItemStatistic.TRUE_DEFENSE,
                        ItemStatistic.FEROCITY, ItemStatistic.HEALTH_REGEN, ItemStatistic.VITALITY, ItemStatistic.MENDING, ItemStatistic.SWING_RANGE));

                statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                    if (!value.equals(statistic.getBaseAdditiveValue()) && stats.contains(statistic)) {
                        lore.add(" " + statistic.getDisplayColor() + statistic.getSymbol() + " " +
                                StringUtility.toNormalCase(statistic.name()) + " §f" +
                                StringUtility.decimalify(value, 2) + statistic.getSuffix());
                    }
                });

                lore.add("");
                lore.add("§eClick for details!");
                return ItemStackCreator.getStack("§cCombat Stats", Material.DIAMOND_SWORD, 1,
                        lore
                );
            }
        });
        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                PlayerStatistics statistics = player.getStatistics();
                List<String> lore = new ArrayList<>(List.of("§7Lets you collect and harvest better", "§7items, or more of them. "));
                List<ItemStatistic> stats = new ArrayList<>(List.of(ItemStatistic.MINING_SPEED, ItemStatistic.MINING_FORTUNE, ItemStatistic.BREAKING_POWER,
                        ItemStatistic.PRISTINE, ItemStatistic.FORAGING_FORTUNE, ItemStatistic.FARMING_FORTUNE));

                statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                    if (!value.equals(statistic.getBaseAdditiveValue()) && stats.contains(statistic)) {
                        lore.add(" " + statistic.getDisplayColor() + statistic.getSymbol() + " " +
                                StringUtility.toNormalCase(statistic.name()) + " §f" +
                                StringUtility.decimalify(value, 2) + statistic.getSuffix());
                    }
                });

                lore.add("");
                lore.add("§eClick for details!");
                return ItemStackCreator.getStack("§eGathering Stats", Material.IRON_PICKAXE, 1,
                        lore
                );
            }
        });

        set(new GUIClickableItem(24) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                PlayerStatistics statistics = player.getStatistics();
                List<String> lore = new ArrayList<>(List.of("§7Increases the §3XP §7you gain on your", "§7skills "));
                List<ItemStatistic> stats = new ArrayList<>(List.of()); // WISDOM STATS
                statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                    if (!value.equals(statistic.getBaseAdditiveValue()) && stats.contains(statistic)) {
                        lore.add(" " + statistic.getDisplayColor() + statistic.getSymbol() + " " +
                                StringUtility.toNormalCase(statistic.name()) + " §f" +
                                StringUtility.decimalify(value, 2) + statistic.getSuffix());
                    }
                });

                lore.add("");
                lore.add("§eClick for details!");
                return ItemStackCreator.getStack("§3Wisdom Stats", Material.BOOK, 1,
                        lore
                );
            }
        });

        set(new GUIClickableItem(25) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                PlayerStatistics statistics = player.getStatistics();
                List<String> lore = new ArrayList<>(List.of("§7Augments various aspects of your", "§7gameplay! "));
                List<ItemStatistic> stats = new ArrayList<>(List.of(ItemStatistic.SPEED, ItemStatistic.MAGIC_FIND, ItemStatistic.PET_LUCK,
                        ItemStatistic.SEA_CREATURE_CHANCE, ItemStatistic.FISHING_SPEED, ItemStatistic.COLD_RESISTANCE, ItemStatistic.BONUS_PEST_CHANCE));

                statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                    if (!value.equals(statistic.getBaseAdditiveValue()) && stats.contains(statistic)) {
                        lore.add(" " + statistic.getDisplayColor() + statistic.getSymbol() + " " +
                                StringUtility.toNormalCase(statistic.name()) + " §f" +
                                StringUtility.decimalify(value, 2) + statistic.getSuffix());
                    }
                });

                lore.add("");
                lore.add("§eClick for details!");
                return ItemStackCreator.getStack("§dMisc Stats", Material.CLOCK, 1, lore);
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() { return false; }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {}

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(false);
    }
}
