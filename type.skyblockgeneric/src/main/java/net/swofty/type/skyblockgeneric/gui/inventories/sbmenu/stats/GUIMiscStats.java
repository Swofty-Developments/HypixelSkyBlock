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

public class GUIMiscStats extends StatelessView {

    private enum MiscStat {
        SPEED(10, ItemStatistic.SPEED, new GUIMaterial(Material.SUGAR),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.SPEED);
                    List<String> lore = new ArrayList<>(List.of(
                            "§7Your Speed stat increases how fast",
                            "§7you can walk.",
                            " ",
                            "§7Flat: " + ItemStatistic.SPEED.getDisplayColor() + "+" +
                                    StringUtility.commaify(value) + ItemStatistic.SPEED.getSymbol(),
                            "§7Stat Cap: " + ItemStatistic.SPEED.getDisplayColor() + "400" +
                                    ItemStatistic.SPEED.getSymbol() + " " + ItemStatistic.SPEED.getDisplayName(),
                            " "
                    ));

                    if (value == ItemStatistic.SPEED.getBaseAdditiveValue()) {
                        lore.add("§7You walk at a regular walking speed.");
                    } else {
                        lore.add("§7 You are §a" + ((int) (value / 100D) - 1) + "% §7faster!");
                    }
                    return lore;
                }
        ),

        MAGIC_FIND(11, ItemStatistic.MAGIC_FIND, new GUIMaterial(Material.STICK),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.MAGIC_FIND);
                    List<String> lore = new ArrayList<>(List.of(
                            "§7Magic Find increases how many rare",
                            "§7items you find.",
                            " "
                    ));

                    if (value != 0D) {
                        lore.add("§7Flat: " + ItemStatistic.MAGIC_FIND.getDisplayColor() + "+" +
                                StringUtility.commaify(value) + ItemStatistic.MAGIC_FIND.getSymbol());
                        lore.add("§7Stat Cap: " + ItemStatistic.MAGIC_FIND.getDisplayColor() + "900" +
                                ItemStatistic.MAGIC_FIND.getSymbol() + " " + ItemStatistic.MAGIC_FIND.getDisplayName());
                        lore.add(" ");
                    }
                    return lore;
                }
        ),

        PET_LUCK(12, ItemStatistic.PET_LUCK,  new GUIMaterial("bc78314255d8864a753fe95622564046f0dee2a82c6e4e2e7f452fcb95af318c"),
                player -> List.of(
                        "§7Pet Luck increases how many pets",
                        "§7you find and gives you better luck",
                        "§7when crafting pets.",
                        " "
                )
        ),

        BONUS_PEST_CHANCE(13, ItemStatistic.BONUS_PEST_CHANCE,  new GUIMaterial(Material.DIRT),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.BONUS_PEST_CHANCE);
                    return List.of(
                            "§7Chance to spawn bonus " + ItemStatistic.BONUS_PEST_CHANCE.getDisplayColor() +
                                    ItemStatistic.BONUS_PEST_CHANCE.getSymbol() + " Pests",
                            "§7while on §aThe Garden§7.",
                            " ",
                            "§7Chance for §a" + ((int) value / 100 + 1) + " §7bonus pest: §a" + ((int) value % 100) + "%",
                            " "
                    );
                }
        ),

        HEAT_RESISTANCE(14, ItemStatistic.HEAT_RESISTANCE,  new GUIMaterial(Material.LAVA_BUCKET),
                player -> List.of(
                        "§7Heat Resistances increases the",
                        "§7amount of time you can spend in hot",
                        "§7environments.",
                        " "
                )
        ),

        COLD_RESISTANCE(15, ItemStatistic.COLD_RESISTANCE,  new GUIMaterial(Material.ICE),
                player -> List.of(
                        "§7Cold Resistance increases the",
                        "§7amount of time you can spend in cold",
                        "§7environments.",
                        " "
                )
        ),

        FEAR(16, ItemStatistic.FEAR,  new GUIMaterial(Material.CAULDRON),
                player -> List.of(
                        "§7Makes Primal Fears spawn more",
                        "§7often and reduces damage taken",
                        "§7from Primal Fears.",
                        " "
                )
        ),

        PULL(19, ItemStatistic.PULL,  new GUIMaterial(Material.COBWEB),
                player -> List.of(
                        "§7Pull dictates both which fish you're",
                        "§7able to grab and how long it takes",
                        "§7using a fishing net.",
                        " "
                )
        ),

        RESPIRATION(20, ItemStatistic.RESPIRATION,  new GUIMaterial(Material.GLASS_BOTTLE),
                player -> List.of(
                        "§7Extends underwater breathing time.",
                        " "
                )
        ),

        PRESSURE_RESISTANCE(21, ItemStatistic.PRESSURE_RESISTANCE, new GUIMaterial(Material.GLASS_BOTTLE),
                player -> List.of(
                        "§7Pressure Resistance reduces the",
                        "§7effects of Pressure when diving.",
                        " ",
                        "§7§7You will start feeling the effects of",
                        "§7pressure only §90 §7blocks under water.",
                        "§7Pressure will also build up §90% §7slower.",
                        " "
                )
        );

        private final int slot;
        private final ItemStatistic statistic;
        private final GUIMaterial guiMaterial;
        private final Function<SkyBlockPlayer, List<String>> loreProvider;

        MiscStat(int slot, ItemStatistic statistic, GUIMaterial guiMaterial,
                 Function<SkyBlockPlayer, List<String>> loreProvider) {
            this.slot = slot;
            this.statistic = statistic;
            this.guiMaterial = guiMaterial;
            this.loreProvider = loreProvider;
        }

        public List<String> buildLore(SkyBlockPlayer player) {
            List<String> lore = new ArrayList<>(loreProvider.apply(player));

            double value = player.getStatistics().allStatistics().getOverall(statistic);
            if (value == 0D) {
                lore.add("§8You have none of this stat!");
            }
            lore.add("§eClick to view!");

            return lore;
        }

        public net.minestom.server.item.ItemStack.Builder buildItemStack(SkyBlockPlayer player) {
            double value = player.getStatistics().allStatistics().getOverall(statistic);
            String title = statistic.getFullDisplayName() + " §f" +
                    StringUtility.decimalify(value, 1);
            List<String> lore = buildLore(player);

            return ItemStackCreator.getUsingGUIMaterial(title, guiMaterial, 1, lore);
        }
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Your Stats Breakdown", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 48, ctx);
        Components.close(layout, 49);

        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            PlayerStatistics statistics = player.getStatistics();
            List<String> lore = new ArrayList<>(List.of("§7Augments various aspects of your", "§7gameplay! ", " "));
            List<ItemStatistic> stats = new ArrayList<>(List.of(
                    ItemStatistic.SPEED, ItemStatistic.MAGIC_FIND, ItemStatistic.PET_LUCK,
                    ItemStatistic.COLD_RESISTANCE, ItemStatistic.BONUS_PEST_CHANCE,
                    ItemStatistic.HEAT_RESISTANCE, ItemStatistic.FEAR, ItemStatistic.PULL,
                    ItemStatistic.RESPIRATION, ItemStatistic.PRESSURE_RESISTANCE
            ));

            statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                if (stats.contains(statistic)) {
                    lore.add(" " + statistic.getFullDisplayName() + " §f" +
                            StringUtility.decimalify(value, 2) + statistic.getSuffix());
                }
            });

            return ItemStackCreator.getStack("§dMisc Stats", Material.CLOCK, 1, lore);
        });

        for (MiscStat stat : MiscStat.values()) {
            layout.slot(stat.slot, (s, c) -> stat.buildItemStack((SkyBlockPlayer) c.player()),
                    (click, c) -> ((SkyBlockPlayer) c.player()).sendMessage("§aUnder construction!"));
        }
    }

    @Override
    public boolean onBottomClick(net.swofty.type.generic.gui.v2.context.ClickContext<DefaultState> click, ViewContext ctx) {
        return true;
    }
}