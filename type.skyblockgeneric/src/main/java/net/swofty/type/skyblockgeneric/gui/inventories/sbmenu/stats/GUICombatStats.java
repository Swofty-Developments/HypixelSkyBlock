package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.stats;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockProfile;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.statistics.PlayerStatistics;

import java.util.ArrayList;
import java.util.List;

public class GUICombatStats extends HypixelInventoryGUI {

    public GUICombatStats() {
        super("Your Stats Breakdown", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(48, new GUISkyBlockProfile()));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                PlayerStatistics statistics = player.getStatistics();
                List<String> lore = new ArrayList<>(List.of("§7Gives you a better chance at", "§7fighting strong monsters. "));
                List<ItemStatistic> stats = new ArrayList<>(List.of(ItemStatistic.HEALTH, ItemStatistic.DEFENSE, ItemStatistic.STRENGTH, ItemStatistic.INTELLIGENCE,
                        ItemStatistic.CRIT_CHANCE, ItemStatistic.CRIT_DAMAGE, ItemStatistic.BONUS_ATTACK_SPEED, ItemStatistic.ABILITY_DAMAGE, ItemStatistic.TRUE_DEFENSE,
                        ItemStatistic.FEROCITY, ItemStatistic.HEALTH_REGEN, ItemStatistic.VITALITY, ItemStatistic.MENDING, ItemStatistic.SWING_RANGE));

                statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                    if (stats.contains(statistic)) {
                        lore.add(" " + StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 2) + statistic.getSuffix());
                    }
                });

                return ItemStackCreator.getStack("§cCombat Stats", Material.DIAMOND_SWORD, 1,
                        lore
                );
            }
        });

        set(new GUIClickableItem(10) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.HEALTH;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Your Health stat increases your",
                        "§7maximum health.",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.GOLDEN_APPLE, 1, lore
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }
        });

        set(new GUIClickableItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.DEFENSE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                double maxHealth = player.getStatistics().allStatistics().getOverall(ItemStatistic.HEALTH);
                double damageReduction = value / (value + 100D);
                double effectiveHealth = maxHealth * (1D + value / 100D);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Your Defense stat reduces the",
                        "§7damage that you take from enemies.",
                        " ",
                        "§7Damage Reduction: " + statistic.getDisplayColor() + StringUtility.decimalify(damageReduction, 1) + "%",
                        "§7Effective Health: " + ItemStatistic.HEALTH.getDisplayColor() + StringUtility.commaify(effectiveHealth) + ItemStatistic.HEALTH.getSymbol(),
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.commaify(value),
                        Material.IRON_CHESTPLATE, 1, lore
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }
        });

        set(new GUIClickableItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.STRENGTH;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                double damageMultiplier = 1D + value / 100D;
                List<String> lore = new ArrayList<>(List.of(
                        "§7Strength increases the damage you",
                        "§7deal.",
                        " ",
                        "§7Damage Multiplier: " + statistic.getDisplayColor() + StringUtility.decimalify(damageMultiplier, 1) + "x",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.commaify(value),
                        Material.BLAZE_POWDER, 1, lore
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }
        });

        set(new GUIClickableItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.INTELLIGENCE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                double magicDamage = value;
                double manaPool = value + 100D;
                List<String> lore = new ArrayList<>(List.of(
                        "§7Intelligence increases the damage of",
                        "§7your magical items and your mana",
                        "§7pool.",
                        " ",
                        "§7Magic Damage: " + statistic.getDisplayColor() + "+" + StringUtility.decimalify(magicDamage, 2) + "%",
                        "§7Mana Pool: " + statistic.getDisplayColor() + StringUtility.decimalify(manaPool, 2),
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.enchant(ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.BOOK, 1, lore
                ));
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }
        });

        set(new GUIClickableItem(14) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.CRIT_CHANCE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Critical Chance is the percent",
                        "§7chance that you land a Critical Hit",
                        "§7when damaging an enemy.",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStackHead(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        "3e4f49535a276aacc4dc84133bfe81be5f2a4799a4c04d9a4ddb72d819ec2b2b", 1,
                        lore
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.CRIT_DAMAGE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                double damageMultiplier = 1D + value / 100D;
                double critChance = player.getStatistics().allStatistics().getOverall(ItemStatistic.CRIT_CHANCE);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Critical Damage multiplies the damage ",
                        "§7that you deal when you land a ",
                        "§7Critical Hit.",
                        " ",
                        "§7Damage Multiplier: " + statistic.getDisplayColor() + StringUtility.decimalify(damageMultiplier, 1) + "x",
                        "§7Critical Chance: " + statistic.getDisplayColor() + StringUtility.decimalify(critChance, 1) + "%",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStackHead(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        "ddafb23efc57f251878e5328d11cb0eef87b79c87b254a7ec72296f9363ef7c", 1,
                        lore
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }
        });

        set(new GUIClickableItem(16) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.BONUS_ATTACK_SPEED;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(
                        value == 0D ?
                                List.of(
                                        "§7Attack Speed decreases the time",
                                        "§7between hits on your opponent.",
                                        " ",
                                        "§7You attack at a normal rate.",
                                        " "
                                ) :
                                List.of(
                                        "§7Attack Speed decreases the time",
                                        "§7between hits on your opponent.",
                                        " ",
                                        "§7Flat: " + statistic.getDisplayColor() + "+" + StringUtility.commaify(value) + statistic.getSymbol(),
                                        "§7Stat Cap:" + statistic.getDisplayColor() + "100" + statistic.getSymbol() + " " + statistic.getDisplayName(),
                                        " ",
                                        "§7You now attack §a" + StringUtility.commaify(value) + "§7faster!",
                                        " "
                                )
                );

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.GOLDEN_AXE, 1, lore
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }
        });

        set(new GUIClickableItem(19) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.ABILITY_DAMAGE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                double damageMultiplier = 1D + value / 100D;
                List<String> lore = new ArrayList<>(List.of(
                        "§7Ability Damage increases the damage",
                        "§7applied by certain spells and item",
                        "§7abilities.",
                        " ",
                        "§7Damage Multiplier: " + statistic.getDisplayColor() + StringUtility.decimalify(damageMultiplier, 1) + "x",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.BEACON, 1, lore
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }
        });

        set(new GUIClickableItem(20) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.TRUE_DEFENSE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7True Defense is defense which",
                        "§7works against true damage.",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.BONE_MEAL, 1, lore
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }
        });

        set(new GUIClickableItem(21) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.FEROCITY;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                int extraStrikes = (int) (value / 100);
                double chanceForOneMore = value % 100;
                List<String> lore = new ArrayList<>(
                        value == 0D ?
                                List.of(
                                        "§7Ferocity grants percent chance to",
                                        "§7double-strike enemies.Increments of",
                                        "§7100 increases the base number of",
                                        "§7strikes.",
                                        " ",
                                        "§7Base extra strikes: " + statistic.getDisplayColor() + StringUtility.commaify(extraStrikes),
                                        "§7Chance for 1 more: " + statistic.getDisplayColor() + StringUtility.commaify(chanceForOneMore) + "%",
                                        " "
                                ) :
                                List.of(
                                        "§7Ferocity grants percent chance to",
                                        "§7double-strike enemies.Increments of",
                                        "§7100 increases the base number of",
                                        "§7strikes.",
                                        " ",
                                        "§7Flat: " + statistic.getDisplayColor() + "+" + StringUtility.commaify(value) + statistic.getSymbol(),
                                        "§7Stat Cap:" + statistic.getDisplayColor() + "500" + statistic.getSymbol() + statistic.getDisplayName(),
                                        " ",
                                        "§7Base extra strikes: " + statistic.getDisplayColor() + StringUtility.commaify(extraStrikes),
                                        "§7Chance for 1 more: " + statistic.getDisplayColor() + StringUtility.commaify(chanceForOneMore) + "%",
                                        " "
                                )
                );

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.commaify(value),
                        Material.RED_DYE, 1, lore
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }
        });

        set(new GUIClickableItem(22) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.HEALTH_REGEN;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                double maxHealth = player.getStatistics().allStatistics().getOverall(ItemStatistic.HEALTH);
                double healthRegen = value / 100D;
                double avgHpPerSecond = (1.5D + maxHealth / 100D) * (value / 100D) / 2D;
                List<String> lore = new ArrayList<>(List.of(
                        "§7Health Regen increases the amount",
                        "§7of health that you naturally",
                        "§7regenerate over time.",
                        " ",
                        "§7Base regen ticks: §c1% §7of Max §c❤ §7+ §c1.5❤",
                        "§7Regen interval: §aEvery 2 seconds",
                        "§7Health Regen: " + "§4" + StringUtility.decimalify(healthRegen, 1) + "x",
                        " ",
                        "§7Max Health: " + ItemStatistic.HEALTH.getDisplayColor() + StringUtility.decimalify(maxHealth, 1) + ItemStatistic.HEALTH.getSymbol(),
                        "§7Avg HP/s: " + "§a" + StringUtility.decimalify(avgHpPerSecond, 1) + "❤",
                        " "

                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.commaify(value),
                        Material.POTION, 1, lore
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }
        });

        set(new GUIClickableItem(23) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.VITALITY;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                double bonus = value / 100D;
                List<String> lore = new ArrayList<>(
                        value == statistic.getBaseAdditiveValue() ?
                                List.of(
                                        "§7Vitality increases your incoming",
                                        "§7healing, including health regen.",
                                        " ",
                                        "§8Heals you receive aren't modified.",
                                        " "
                                ) :
                                List.of(
                                        "§7Vitality increases your incoming",
                                        "§7healing, including health regen.",
                                        " ",
                                        "§7All heals applied to you are multiplied by " + statistic.getDisplayColor() + StringUtility.decimalify(bonus, 2) + "x§7.",
                                        " "
                                )

                );

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.commaify(value),
                        Material.GLISTERING_MELON_SLICE, 1, lore
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }
        });

        set(new GUIClickableItem(24) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.MENDING;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                double bonus = value / 100D;
                List<String> lore = new ArrayList<>(
                        value == statistic.getBaseAdditiveValue() ?
                                List.of(
                                        "§7Mending increases your outgoing",
                                        "§7healing.",
                                        " ",
                                        "§8Your heals aren't modified.",
                                        " "
                                ) :
                                List.of(
                                        "§7Mending increases your outgoing",
                                        "§7healing.",
                                        " ",
                                        "§7All heals applied to you are multiplied by " + statistic.getDisplayColor() + StringUtility.decimalify(bonus, 2) + "x§7."
                                )

                );

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.commaify(value),
                        Material.GHAST_TEAR, 1, lore
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }
        });

        set(new GUIClickableItem(25) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.SWING_RANGE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(
                        value == statistic.getBaseAdditiveValue() ?
                                List.of(
                                        "§7Increases your melee hit range.",
                                        " ",
                                        "§7Flat: " + statistic.getDisplayColor() + "+" + StringUtility.decimalify(value, 2) + statistic.getSymbol(),
                                        "§7Stat Cap: " + statistic.getDisplayColor() + "15" + statistic.getSymbol() + " " + statistic.getDisplayName(),
                                        " ",
                                        "§8Your swing range isn't modified.",
                                        " "
                                ) :
                                List.of(
                                        "§7Increases your melee hit range.",
                                        " ",
                                        "§7Flat: " + statistic.getDisplayColor() + "+" + StringUtility.decimalify(value, 2) + statistic.getSymbol(),
                                        "§7Stat Cap: " + statistic.getDisplayColor() + "15" + statistic.getSymbol() + " " + statistic.getDisplayName(),
                                        " ",
                                        "§7Your swing range is increased to " + StringUtility.decimalify(value, 2) + " §7blocks.",
                                        " "
                                )

                );

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.STONE_SWORD, 1, lore
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
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
