package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.stats.GUICombatStats;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.stats.GUIGatheringStats;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.stats.GUIMiscStats;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.stats.GUIWisdomStats;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.StandardItemComponent;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.statistics.PlayerStatistics;

import java.util.ArrayList;
import java.util.List;

public class GUISkyBlockProfile extends HypixelInventoryGUI {

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
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (!player.getItemInMainHand().isAir()) {
                    return ItemStackCreator.getFromStack(player.getItemInMainHand());
                } else {
                    return ItemStackCreator.getStack("§7Empty Held Item Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });

        set(new GUIClickableItem(11) { //Helmet
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                SkyBlockItem item = new SkyBlockItem(p.getInventory().getCursorItem());
                if (!player.getHelmet().isAir() && p.getInventory().getCursorItem().isAir()) {
                    player.addAndUpdateItem(player.getHelmet());
                    player.setHelmet(ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                } else if (item.hasComponent(StandardItemComponent.class)
                        && item.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.HELMET
                        && player.getHelmet().isAir()) {
                    player.setHelmet(p.getInventory().getCursorItem());
                    ((Inventory) e.getInventory()).setCursorItem(player, ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (!player.getHelmet().isAir()) {
                    return ItemStackCreator.getFromStack(player.getHelmet());
                } else {
                    return ItemStackCreator.getStack("§7Empty Helmet Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });

        set(new GUIClickableItem(20) { //Chestplate
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                SkyBlockItem item = new SkyBlockItem(p.getInventory().getCursorItem());
                if (!player.getChestplate().isAir() && p.getInventory().getCursorItem().isAir()) {
                    player.addAndUpdateItem(player.getChestplate());
                    player.setChestplate(ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                } else if (item.hasComponent(StandardItemComponent.class)
                        && item.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.CHESTPLATE
                        && player.getChestplate().isAir()) {
                    player.setChestplate(p.getInventory().getCursorItem());
                    ((Inventory) e.getInventory()).setCursorItem(player, ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (!player.getChestplate().isAir()) {
                    return ItemStackCreator.getFromStack(player.getChestplate());
                } else {
                    return ItemStackCreator.getStack("§7Empty Chestplate Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });

        set(new GUIClickableItem(29) { //Leggings
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                SkyBlockItem item = new SkyBlockItem(p.getInventory().getCursorItem());
                if (!player.getLeggings().isAir() && p.getInventory().getCursorItem().isAir()) {
                    player.addAndUpdateItem(player.getLeggings());
                    player.setLeggings(ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                }

                if (item.hasComponent(StandardItemComponent.class)
                        && item.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.LEGGINGS
                        && player.getLeggings().isAir()) {
                    player.setLeggings(p.getInventory().getCursorItem());
                    ((Inventory) e.getInventory()).setCursorItem(player, ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (!player.getLeggings().isAir()) {
                    return ItemStackCreator.getFromStack(player.getLeggings());
                } else {
                    return ItemStackCreator.getStack("§7Empty Leggings Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });

        set(new GUIClickableItem(38) { //Boots
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                SkyBlockItem item = new SkyBlockItem(p.getInventory().getCursorItem());
                if (!player.getBoots().isAir() && p.getInventory().getCursorItem().isAir()) {
                    player.addAndUpdateItem(player.getBoots());
                    player.setBoots(ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                }
                if (item.hasComponent(StandardItemComponent.class)
                        && item.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.BOOTS
                        && player.getBoots().isAir()) {
                    player.setBoots(p.getInventory().getCursorItem());
                    ((Inventory) e.getInventory()).setCursorItem(player, ItemStack.AIR);
                    GUISkyBlockProfile guiSkyBlockProfile = new GUISkyBlockProfile();
                    guiSkyBlockProfile.open(player);
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (!player.getBoots().isAir()) {
                    return ItemStackCreator.getFromStack(player.getBoots());
                } else {
                    return ItemStackCreator.getStack("§7Empty Boots Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });

        set(new GUIClickableItem(47) { //Pet
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIPets().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (player.getPetData().getEnabledPet() != null && !player.getPetData().getEnabledPet().getItemStack().isAir()) {
                    SkyBlockItem pet = player.getPetData().getEnabledPet();
                    return new NonPlayerItemUpdater(pet).getUpdatedItem();
                } else {
                    return ItemStackCreator.getStack("§7Empty Pet Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
                }
            }
        });

        set(new GUIClickableItem(15) { //Combat Stats
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUICombatStats().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                PlayerStatistics statistics = player.getStatistics();
                List<String> lore = new ArrayList<>(List.of("§7Gives you a better chance at", "§7fighting strong monsters. ", " "));
                List<ItemStatistic> stats = new ArrayList<>(List.of(ItemStatistic.HEALTH, ItemStatistic.DEFENSE, ItemStatistic.STRENGTH, ItemStatistic.INTELLIGENCE,
                        ItemStatistic.CRIT_CHANCE, ItemStatistic.CRIT_DAMAGE, ItemStatistic.BONUS_ATTACK_SPEED, ItemStatistic.ABILITY_DAMAGE, ItemStatistic.TRUE_DEFENSE,
                        ItemStatistic.FEROCITY, ItemStatistic.HEALTH_REGENERATION, ItemStatistic.VITALITY, ItemStatistic.MENDING, ItemStatistic.SWING_RANGE));

                statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                    if (stats.contains(statistic)) {
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

        set(new GUIClickableItem(16) {  //Gathering Stats
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIGatheringStats().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                PlayerStatistics statistics = player.getStatistics();
                List<String> lore = new ArrayList<>(List.of("§7Lets you collect and harvest better", "§7items, or more of them. ", " "));
                List<ItemStatistic> stats = new ArrayList<>(List.of(ItemStatistic.MINING_SPEED, ItemStatistic.MINING_FORTUNE, ItemStatistic.BREAKING_POWER,
                        ItemStatistic.PRISTINE, ItemStatistic.FORAGING_FORTUNE, ItemStatistic.FARMING_FORTUNE, ItemStatistic.MINING_SPREAD, ItemStatistic.GEMSTONE_SPREAD,
                        ItemStatistic.HUNTER_FORTUNE, ItemStatistic.SWEEP, ItemStatistic.ORE_FORTUNE, ItemStatistic.BLOCK_FORTUNE, ItemStatistic.DWARVEN_METAL_FORTUNE,
                        ItemStatistic.GEMSTONE_FORTUNE, ItemStatistic.WHEAT_FORTUNE, ItemStatistic.POTATO_FORTUNE, ItemStatistic.CARROT_FORTUNE, ItemStatistic.PUMPKIN_FORTUNE,
                        ItemStatistic.MELON_FORTUNE, ItemStatistic.CACTUS_FORTUNE, ItemStatistic.NETHER_WART_FORTUNE, ItemStatistic.COCOA_BEANS_FORTUNE, ItemStatistic.MUSHROOM_FORTUNE,
                        ItemStatistic.SUGAR_CANE_FORTUNE, ItemStatistic.FIG_FORTUNE, ItemStatistic.MANGROVE_FORTUNE
                ));

                statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                    if (stats.contains(statistic)) {
                        lore.add(" " + statistic.getDisplayColor() + statistic.getSymbol() + " " +
                                StringUtility.toNormalCase(statistic.name()) + " §f" +
                                StringUtility.decimalify(value, 2) + statistic.getSuffix());
                    }
                });

                lore.add("");
                lore.add("§eClick for details!");

                return ItemStackCreator.getStack("§eGathering Stats", Material.IRON_PICKAXE, 1, lore);
            }
        });

        set(new GUIClickableItem(24) { //Wisdom Stats
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIWisdomStats().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                PlayerStatistics statistics = player.getStatistics();
                List<String> lore = new ArrayList<>(List.of("§7Increases the §3XP §7you gain on your", "§7skills ", " "));
                List<ItemStatistic> stats = new ArrayList<>(List.of(ItemStatistic.COMBAT_WISDOM, ItemStatistic.MINING_WISDOM, ItemStatistic.FARMING_WISDOM, ItemStatistic.FORAGING_WISDOM,
                        ItemStatistic.FISHING_WISDOM, ItemStatistic.ENCHANTING_WISDOM, ItemStatistic.ALCHEMY_WISDOM, ItemStatistic.CARPENTRY_WISDOM, ItemStatistic.RUNE_CRAFTING_WISDOM,
                        ItemStatistic.SOCIAL_WISDOM, ItemStatistic.TAMING_WISDOM, ItemStatistic.HUNTING_WISDOM
                )); // WISDOM STATS
                statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                    if (stats.contains(statistic)) {
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

        set(new GUIClickableItem(25) { //Misc Stats
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIMiscStats().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                PlayerStatistics statistics = player.getStatistics();
                List<String> lore = new ArrayList<>(List.of("§7Augments various aspects of your", "§7gameplay! ", " "));
                List<ItemStatistic> stats = new ArrayList<>(List.of(ItemStatistic.SPEED, ItemStatistic.MAGIC_FIND, ItemStatistic.PET_LUCK,
                        ItemStatistic.COLD_RESISTANCE, ItemStatistic.BONUS_PEST_CHANCE, ItemStatistic.HEAT_RESISTANCE, ItemStatistic.FEAR,
                        ItemStatistic.PULL, ItemStatistic.RESPIRATION, ItemStatistic.PRESSURE_RESISTANCE
                ));

                statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                    if (stats.contains(statistic)) {
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
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(false);
    }
}
