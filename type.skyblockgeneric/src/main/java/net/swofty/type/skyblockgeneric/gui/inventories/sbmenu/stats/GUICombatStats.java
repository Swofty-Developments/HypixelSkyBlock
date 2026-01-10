package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.stats;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIMaterial;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.statistics.PlayerStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GUICombatStats extends StatelessView {

    private enum CombatStat {
        HEALTH(10, ItemStatistic.HEALTH, new GUIMaterial(Material.GOLDEN_APPLE),
                player -> List.of(
                        "§7Your Health stat increases your",
                        "§7maximum health.",
                        " "
                )
        ),

        DEFENSE(11, ItemStatistic.DEFENSE, new GUIMaterial(Material.IRON_CHESTPLATE),
                player -> {
                    double defense = player.getStatistics().allStatistics().getOverall(ItemStatistic.DEFENSE);
                    double maxHealth = player.getStatistics().allStatistics().getOverall(ItemStatistic.HEALTH);
                    double damageReduction = defense / (defense + 100D);
                    double effectiveHealth = maxHealth * (1D + defense / 100D);
                    return List.of(
                            "§7Your Defense stat reduces the",
                            "§7damage that you take from enemies.",
                            " ",
                            "§7Damage Reduction: " + ItemStatistic.DEFENSE.getDisplayColor() + StringUtility.decimalify(damageReduction, 1) + "%",
                            "§7Effective Health: " + ItemStatistic.HEALTH.getDisplayColor() + StringUtility.commaify(effectiveHealth) + ItemStatistic.HEALTH.getSymbol(),
                            " "
                    );
                }
        ),

        STRENGTH(12, ItemStatistic.STRENGTH, new GUIMaterial(Material.BLAZE_POWDER),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.STRENGTH);
                    double damageMultiplier = 1D + value / 100D;
                    return List.of(
                            "§7Strength increases the damage you",
                            "§7deal.",
                            " ",
                            "§7Damage Multiplier: " + ItemStatistic.STRENGTH.getDisplayColor() + StringUtility.decimalify(damageMultiplier, 1) + "x",
                            " "
                    );
                }
        ),

        INTELLIGENCE(13, ItemStatistic.INTELLIGENCE, new GUIMaterial(Material.ENCHANTED_BOOK),
                player -> {
                    double magicDamage = player.getStatistics().allStatistics().getOverall(ItemStatistic.INTELLIGENCE);
                    double manaPool = magicDamage + 100D;
                    return List.of(
                            "§7Intelligence increases the damage of",
                            "§7your magical items and your mana",
                            "§7pool.",
                            " ",
                            "§7Magic Damage: " + ItemStatistic.INTELLIGENCE.getDisplayColor() + "+" + StringUtility.decimalify(magicDamage, 2) + "%",
                            "§7Mana Pool: " + ItemStatistic.INTELLIGENCE.getDisplayColor() + StringUtility.decimalify(manaPool, 2),
                            " "
                    );
                }
        ),

        CRIT_CHANCE(14, ItemStatistic.CRITICAL_CHANCE, new GUIMaterial("3e4f49535a276aacc4dc84133bfe81be5f2a4799a4c04d9a4ddb72d819ec2b2b"),
                player -> List.of(
                        "§7Critical Chance is the percent",
                        "§7chance that you land a Critical Hit",
                        "§7when damaging an enemy.",
                        " "
                )
        ),

        CRIT_DAMAGE(15, ItemStatistic.CRITICAL_DAMAGE, new GUIMaterial("ddafb23efc57f251878e5328d11cb0eef87b79c87b254a7ec72296f9363ef7c"),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.CRITICAL_DAMAGE);
                    double damageMultiplier = 1D + value / 100D;
                    double critChance = player.getStatistics().allStatistics().getOverall(ItemStatistic.CRITICAL_CHANCE);
                    return List.of(
                            "§7Critical Damage multiplies the damage ",
                            "§7that you deal when you land a ",
                            "§7Critical Hit.",
                            " ",
                            "§7Damage Multiplier: " + ItemStatistic.CRITICAL_DAMAGE.getDisplayColor() + StringUtility.decimalify(damageMultiplier, 1) + "x",
                            "§7Critical Chance: " + ItemStatistic.CRITICAL_DAMAGE.getDisplayColor() + StringUtility.decimalify(critChance, 1) + "%",
                            " "
                    );
                }
        ),

        BONUS_ATTACK_SPEED(16, ItemStatistic.BONUS_ATTACK_SPEED, new GUIMaterial(Material.GOLDEN_AXE),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.BONUS_ATTACK_SPEED);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7Attack Speed decreases the time");
                    lore.add("§7between hits on your opponent.");
                    lore.add(" ");

                    if (value == 0D) {
                        lore.add("§7You attack at a normal rate.");
                        lore.add(" ");
                    } else {
                        lore.add("§7Flat: " + ItemStatistic.BONUS_ATTACK_SPEED.getDisplayColor() + "+" + StringUtility.commaify(value) + ItemStatistic.BONUS_ATTACK_SPEED.getSymbol());
                        lore.add("§7Stat Cap:" + ItemStatistic.BONUS_ATTACK_SPEED.getDisplayColor() + "100" + ItemStatistic.BONUS_ATTACK_SPEED.getSymbol() + " " + ItemStatistic.BONUS_ATTACK_SPEED.getDisplayName());
                        lore.add(" ");
                        lore.add("§7You now attack §a" + StringUtility.commaify(value) + "§7 faster!");
                        lore.add(" ");
                    }
                    return lore;
                }
        ),

        ABILITY_DAMAGE(19, ItemStatistic.ABILITY_DAMAGE, new GUIMaterial(Material.BEACON),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.ABILITY_DAMAGE);
                    double damageMultiplier = 1D + value / 100D;
                    return List.of(
                            "§7Ability Damage increases the damage",
                            "§7applied by certain spells and item",
                            "§7abilities.",
                            " ",
                            "§7Damage Multiplier: " + ItemStatistic.ABILITY_DAMAGE.getDisplayColor() + StringUtility.decimalify(damageMultiplier, 1) + "x",
                            " "
                    );
                }
        ),

        TRUE_DEFENSE(20, ItemStatistic.TRUE_DEFENSE, new GUIMaterial(Material.BONE_MEAL),
                player -> List.of(
                        "§7True Defense is defense which",
                        "§7works against true damage.",
                        " "
                )
        ),

        FEROCITY(21, ItemStatistic.FEROCITY, new GUIMaterial(Material.RED_DYE),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.FEROCITY);
                    int extraStrikes = (int) (value / 100);
                    double chanceForOneMore = value % 100;
                    List<String> lore = new ArrayList<>();

                    lore.add("§7Ferocity grants percent chance to");
                    lore.add("§7double-strike enemies. Increments of");
                    lore.add("§7100 increases the base number of");
                    lore.add("§7strikes.");
                    lore.add(" ");

                    if (value != 0D) {
                        lore.add("§7Flat: " + ItemStatistic.FEROCITY.getDisplayColor() + "+" + StringUtility.commaify(value) + ItemStatistic.FEROCITY.getSymbol());
                        lore.add("§7Stat Cap:" + ItemStatistic.FEROCITY.getDisplayColor() + "500" + ItemStatistic.FEROCITY.getSymbol() + ItemStatistic.FEROCITY.getDisplayName());
                        lore.add(" ");
                    }

                    lore.add("§7Base extra strikes: " + ItemStatistic.FEROCITY.getDisplayColor() + StringUtility.commaify(extraStrikes));
                    lore.add("§7Chance for 1 more: " + ItemStatistic.FEROCITY.getDisplayColor() + StringUtility.commaify(chanceForOneMore) + "%");
                    lore.add(" ");
                    return lore;
                }
        ),

        HEALTH_REGEN(22, ItemStatistic.HEALTH_REGENERATION, new GUIMaterial(Material.POTION),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.HEALTH_REGENERATION);
                    double maxHealth = player.getStatistics().allStatistics().getOverall(ItemStatistic.HEALTH);
                    double healthRegen = value / 100D;
                    double avgHpPerSecond = (1.5D + maxHealth / 100D) * (value / 100D) / 2D;
                    return List.of(
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
                    );
                }
        ),

        VITALITY(23, ItemStatistic.VITALITY, new GUIMaterial(Material.GLISTERING_MELON_SLICE),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.VITALITY);
                    double bonus = value / 100D;
                    List<String> lore = new ArrayList<>();

                    lore.add("§7Vitality increases your incoming");
                    lore.add("§7healing, including health regen.");
                    lore.add(" ");

                    if (value == ItemStatistic.VITALITY.getBaseAdditiveValue()) {
                        lore.add("§8Heals you receive aren't modified.");
                    } else {
                        lore.add("§7All heals applied to you are multiplied by "
                                + ItemStatistic.VITALITY.getDisplayColor()
                                + StringUtility.decimalify(bonus, 2) + "x§7.");
                    }

                    lore.add(" ");
                    return lore;
                }
        ),

        MENDING(24, ItemStatistic.MENDING, new GUIMaterial(Material.GHAST_TEAR),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.MENDING);
                    double bonus = value / 100D;
                    List<String> lore = new ArrayList<>();

                    lore.add("§7Mending increases your outgoing");
                    lore.add("§7healing.");
                    lore.add(" ");

                    if (value == ItemStatistic.MENDING.getBaseAdditiveValue()) {
                        lore.add("§8Your heals aren't modified.");
                        lore.add(" ");
                    } else {
                        lore.add("§7All heals applied to you are multiplied by "
                                + ItemStatistic.MENDING.getDisplayColor()
                                + StringUtility.decimalify(bonus, 2) + "x§7.");
                    }
                    return lore;
                }
        ),

        SWING_RANGE(25, ItemStatistic.SWING_RANGE, new GUIMaterial(Material.STONE_SWORD),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.SWING_RANGE);
                    List<String> lore = new ArrayList<>();

                    lore.add("§7Increases your melee hit range.");
                    lore.add(" ");
                    lore.add("§7Flat: " + ItemStatistic.SWING_RANGE.getDisplayColor() + "+" + StringUtility.decimalify(value, 2) + ItemStatistic.SWING_RANGE.getSymbol());
                    lore.add("§7Stat Cap: " + ItemStatistic.SWING_RANGE.getDisplayColor() + "15" + ItemStatistic.SWING_RANGE.getSymbol() + " " + ItemStatistic.SWING_RANGE.getDisplayName());
                    lore.add(" ");

                    if (value == ItemStatistic.SWING_RANGE.getBaseAdditiveValue()) {
                        lore.add("§8Your swing range isn't modified.");
                    } else {
                        lore.add("§7Your swing range is increased to "
                                + StringUtility.decimalify(value, 2) + " §7blocks.");
                    }

                    lore.add(" ");
                    return lore;
                }
        );

        private final int slot;
        private final ItemStatistic statistic;
        private final GUIMaterial guiMaterial;
        private final Function<SkyBlockPlayer, List<String>> baseLoreProvider;

        CombatStat(int slot, ItemStatistic statistic, GUIMaterial guiMaterial,
                   Function<SkyBlockPlayer, List<String>> baseLoreProvider) {
            this.slot = slot;
            this.statistic = statistic;
            this.guiMaterial = guiMaterial;
            this.baseLoreProvider = baseLoreProvider;
        }

        public List<String> buildLore(SkyBlockPlayer player) {
            List<String> lore = new ArrayList<>(baseLoreProvider.apply(player));
            double value = player.getStatistics().allStatistics().getOverall(statistic);
            if (value == 0D) {
                lore.add("§8You have none of this stat!");
            }
            lore.add("§eClick to view!");
            return lore;
        }
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Your Stats Breakdown", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        // Title item
        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            PlayerStatistics statistics = player.getStatistics();
            List<String> lore = new ArrayList<>(List.of("§7Gives you a better chance at", "§7fighting strong monsters. ", " "));
            List<ItemStatistic> stats = new ArrayList<>(List.of(ItemStatistic.HEALTH, ItemStatistic.DEFENSE, ItemStatistic.STRENGTH, ItemStatistic.INTELLIGENCE,
                    ItemStatistic.CRITICAL_CHANCE, ItemStatistic.CRITICAL_DAMAGE, ItemStatistic.BONUS_ATTACK_SPEED, ItemStatistic.ABILITY_DAMAGE, ItemStatistic.TRUE_DEFENSE,
                    ItemStatistic.FEROCITY, ItemStatistic.HEALTH_REGENERATION, ItemStatistic.VITALITY, ItemStatistic.MENDING, ItemStatistic.SWING_RANGE));

            statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                if (stats.contains(statistic)) {
                    lore.add(" " + statistic.getFullDisplayName() + " §f" +
                            StringUtility.decimalify(value, 2) + statistic.getSuffix());
                }
            });

            return ItemStackCreator.getStack("§cCombat Stats", Material.DIAMOND_SWORD, 1, lore);
        });

        // Combat stat items
        for (CombatStat stat : CombatStat.values()) {
            layout.slot(stat.slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                double value = player.getStatistics().allStatistics().getOverall(stat.statistic);
                String title = stat.statistic.getFullDisplayName() + " §f" + StringUtility.decimalify(value, 1);
                List<String> lore = stat.buildLore(player);
                return ItemStackCreator.getUsingGUIMaterial(title, stat.guiMaterial, 1, lore);
            }, (click, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                player.sendMessage("§aUnder construction!");
            });
        }
    }
}