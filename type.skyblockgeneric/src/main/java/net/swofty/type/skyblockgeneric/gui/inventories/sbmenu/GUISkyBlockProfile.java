package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
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

public class GUISkyBlockProfile extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Your Equipment and Stats", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        // Held Item
        layout.slot(2, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            if (!player.getItemInMainHand().isAir()) {
                return ItemStackCreator.getFromStack(player.getItemInMainHand());
            } else {
                return ItemStackCreator.getStack("§7Empty Held Item Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
            }
        });

        // Helmet
        layout.slot(11, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            if (!player.getHelmet().isAir()) {
                return ItemStackCreator.getFromStack(player.getHelmet());
            } else {
                return ItemStackCreator.getStack("§7Empty Helmet Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
            }
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockItem cursorItem = new SkyBlockItem(player.getInventory().getCursorItem());
            if (!player.getHelmet().isAir() && player.getInventory().getCursorItem().isAir()) {
                player.addAndUpdateItem(player.getHelmet());
                player.setHelmet(ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            } else if (cursorItem.hasComponent(StandardItemComponent.class)
                    && cursorItem.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.HELMET
                    && player.getHelmet().isAir()) {
                player.setHelmet(player.getInventory().getCursorItem());
                c.inventory().setCursorItem(player, ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            }
        });

        // Chestplate
        layout.slot(20, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            if (!player.getChestplate().isAir()) {
                return ItemStackCreator.getFromStack(player.getChestplate());
            } else {
                return ItemStackCreator.getStack("§7Empty Chestplate Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
            }
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockItem cursorItem = new SkyBlockItem(player.getInventory().getCursorItem());
            if (!player.getChestplate().isAir() && player.getInventory().getCursorItem().isAir()) {
                player.addAndUpdateItem(player.getChestplate());
                player.setChestplate(ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            } else if (cursorItem.hasComponent(StandardItemComponent.class)
                    && cursorItem.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.CHESTPLATE
                    && player.getChestplate().isAir()) {
                player.setChestplate(player.getInventory().getCursorItem());
                c.inventory().setCursorItem(player, ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            }
        });

        // Leggings
        layout.slot(29, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            if (!player.getLeggings().isAir()) {
                return ItemStackCreator.getFromStack(player.getLeggings());
            } else {
                return ItemStackCreator.getStack("§7Empty Leggings Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
            }
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockItem cursorItem = new SkyBlockItem(player.getInventory().getCursorItem());
            if (!player.getLeggings().isAir() && player.getInventory().getCursorItem().isAir()) {
                player.addAndUpdateItem(player.getLeggings());
                player.setLeggings(ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            } else if (cursorItem.hasComponent(StandardItemComponent.class)
                    && cursorItem.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.LEGGINGS
                    && player.getLeggings().isAir()) {
                player.setLeggings(player.getInventory().getCursorItem());
                c.inventory().setCursorItem(player, ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            }
        });

        // Boots
        layout.slot(38, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            if (!player.getBoots().isAir()) {
                return ItemStackCreator.getFromStack(player.getBoots());
            } else {
                return ItemStackCreator.getStack("§7Empty Boots Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
            }
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockItem cursorItem = new SkyBlockItem(player.getInventory().getCursorItem());
            if (!player.getBoots().isAir() && player.getInventory().getCursorItem().isAir()) {
                player.addAndUpdateItem(player.getBoots());
                player.setBoots(ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            } else if (cursorItem.hasComponent(StandardItemComponent.class)
                    && cursorItem.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.BOOTS
                    && player.getBoots().isAir()) {
                player.setBoots(player.getInventory().getCursorItem());
                c.inventory().setCursorItem(player, ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            }
        });

        // Pet
        layout.slot(47, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            if (player.getPetData().getEnabledPet() != null && !player.getPetData().getEnabledPet().getItemStack().isAir()) {
                SkyBlockItem pet = player.getPetData().getEnabledPet();
                return new NonPlayerItemUpdater(pet).getUpdatedItem();
            } else {
                return ItemStackCreator.getStack("§7Empty Pet Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
            }
        }, (click, c) -> {
            //c.player().openView(new GUIPets())
        });

        // Combat Stats
        layout.slot(15, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            PlayerStatistics statistics = player.getStatistics();
            List<String> lore = new ArrayList<>(List.of("§7Gives you a better chance at", "§7fighting strong monsters. ", " "));
            List<ItemStatistic> stats = List.of(ItemStatistic.HEALTH, ItemStatistic.DEFENSE, ItemStatistic.STRENGTH, ItemStatistic.INTELLIGENCE,
                    ItemStatistic.CRITICAL_CHANCE, ItemStatistic.CRITICAL_DAMAGE, ItemStatistic.BONUS_ATTACK_SPEED, ItemStatistic.ABILITY_DAMAGE, ItemStatistic.TRUE_DEFENSE,
                    ItemStatistic.FEROCITY, ItemStatistic.HEALTH_REGENERATION, ItemStatistic.VITALITY, ItemStatistic.MENDING, ItemStatistic.SWING_RANGE);

            statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                if (stats.contains(statistic)) {
                    lore.add(" " + statistic.getFullDisplayName() + " §f" +
                            StringUtility.decimalify(value, 2) + statistic.getSuffix());
                }
            });

            lore.add("");
            lore.add("§eClick for details!");
            return ItemStackCreator.getStack("§cCombat Stats", Material.DIAMOND_SWORD, 1, lore);
        }, (click, c) -> {
            //c.player().openView(new GUICombatStats()))
        });

        // Gathering Stats
        layout.slot(16, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            PlayerStatistics statistics = player.getStatistics();
            List<String> lore = new ArrayList<>(List.of("§7Lets you collect and harvest better", "§7items, or more of them. ", " "));
            List<ItemStatistic> stats = List.of(ItemStatistic.MINING_SPEED, ItemStatistic.MINING_FORTUNE, ItemStatistic.BREAKING_POWER,
                    ItemStatistic.PRISTINE, ItemStatistic.FORAGING_FORTUNE, ItemStatistic.FARMING_FORTUNE, ItemStatistic.MINING_SPREAD, ItemStatistic.GEMSTONE_SPREAD,
                    ItemStatistic.HUNTER_FORTUNE, ItemStatistic.SWEEP, ItemStatistic.ORE_FORTUNE, ItemStatistic.BLOCK_FORTUNE, ItemStatistic.DWARVEN_METAL_FORTUNE,
                    ItemStatistic.GEMSTONE_FORTUNE, ItemStatistic.WHEAT_FORTUNE, ItemStatistic.POTATO_FORTUNE, ItemStatistic.CARROT_FORTUNE, ItemStatistic.PUMPKIN_FORTUNE,
                    ItemStatistic.MELON_FORTUNE, ItemStatistic.CACTUS_FORTUNE, ItemStatistic.NETHER_WART_FORTUNE, ItemStatistic.COCOA_BEANS_FORTUNE, ItemStatistic.MUSHROOM_FORTUNE,
                    ItemStatistic.SUGAR_CANE_FORTUNE, ItemStatistic.FIG_FORTUNE, ItemStatistic.MANGROVE_FORTUNE);

            statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                if (stats.contains(statistic)) {
                    lore.add(" " + statistic.getFullDisplayName() + " §f" +
                            StringUtility.decimalify(value, 2) + statistic.getSuffix());
                }
            });

            lore.add("");
            lore.add("§eClick for details!");
            return ItemStackCreator.getStack("§eGathering Stats", Material.IRON_PICKAXE, 1, lore);
        }, (click, c) -> {
            // c.player().openView(new GUIGatheringStats());
        });

        // Wisdom Stats
        layout.slot(24, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            PlayerStatistics statistics = player.getStatistics();
            List<String> lore = new ArrayList<>(List.of("§7Increases the §3XP §7you gain on your", "§7skills ", " "));
            List<ItemStatistic> stats = List.of(ItemStatistic.COMBAT_WISDOM, ItemStatistic.MINING_WISDOM, ItemStatistic.FARMING_WISDOM, ItemStatistic.FORAGING_WISDOM,
                    ItemStatistic.FISHING_WISDOM, ItemStatistic.ENCHANTING_WISDOM, ItemStatistic.ALCHEMY_WISDOM, ItemStatistic.CARPENTRY_WISDOM, ItemStatistic.RUNE_CRAFTING_WISDOM,
                    ItemStatistic.SOCIAL_WISDOM, ItemStatistic.TAMING_WISDOM, ItemStatistic.HUNTING_WISDOM);

            statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                if (stats.contains(statistic)) {
                    lore.add(" " + statistic.getFullDisplayName() + " §f" +
                            StringUtility.decimalify(value, 2) + statistic.getSuffix());
                }
            });

            lore.add("");
            lore.add("§eClick for details!");
            return ItemStackCreator.getStack("§3Wisdom Stats", Material.BOOK, 1, lore);
        }, (click, c) -> {
            //c.player().openView(new GUIWisdomStats())
        });

        // Misc Stats
        layout.slot(25, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            PlayerStatistics statistics = player.getStatistics();
            List<String> lore = new ArrayList<>(List.of("§7Augments various aspects of your", "§7gameplay! ", " "));
            List<ItemStatistic> stats = List.of(ItemStatistic.SPEED, ItemStatistic.MAGIC_FIND, ItemStatistic.PET_LUCK,
                    ItemStatistic.COLD_RESISTANCE, ItemStatistic.BONUS_PEST_CHANCE, ItemStatistic.HEAT_RESISTANCE, ItemStatistic.FEAR,
                    ItemStatistic.PULL, ItemStatistic.RESPIRATION, ItemStatistic.PRESSURE_RESISTANCE);

            statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                if (stats.contains(statistic)) {
                    lore.add(" " + statistic.getFullDisplayName() + " §f" +
                            StringUtility.decimalify(value, 2) + statistic.getSuffix());
                }
            });

            lore.add("");
            lore.add("§eClick for details!");
            return ItemStackCreator.getStack("§dMisc Stats", Material.CLOCK, 1, lore);
        }, (click, c) -> {
            //c.player().openView(new GUIMiscStats())
        });
    }
}
