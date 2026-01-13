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

public class GUIGatheringStats extends StatelessView {

    private static final String[] multiplesMap = new String[]{
            "§adouble",
            "§6triple",
            "§5quadruple"
    };

    private enum GatheringStat {
        MINING_SPEED(10, ItemStatistic.MINING_SPEED, new GUIMaterial(Material.DIAMOND_PICKAXE),
                player -> List.of(
                        "§7Increases the speed of breaking",
                        "§7mining blocks.",
                        " "
                )
        ),

        MINING_SPREAD(11, ItemStatistic.MINING_SPREAD, new GUIMaterial(Material.GOLDEN_PICKAXE),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.MINING_SPREAD);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island");
                    lore.add(" ");
                    lore.add("§7Mining Spread is the chance to");
                    lore.add("§7automatically mine adjacent blocks");
                    lore.add("§7Blocks, Ores, and");
                    lore.add("§7Dwarven Metals.");
                    lore.add(" ");

                    if (value != 0D) {
                        lore.add("§7Flat: " + ItemStatistic.MINING_SPREAD.getDisplayColor() + "+" + StringUtility.commaify(value) + ItemStatistic.MINING_SPREAD.getSymbol());
                        lore.add("§7Stat Cap: " + ItemStatistic.MINING_SPREAD.getDisplayColor() + StringUtility.commaify(10000) + ItemStatistic.MINING_SPREAD.getSymbol());
                        lore.add(" ");
                    }

                    addFormateNumberLore(value, "block", ItemStatistic.MINING_SPREAD, lore);
                    return lore;
                }
        ),

        GEMSTONE_SPREAD(12, ItemStatistic.GEMSTONE_SPREAD, new GUIMaterial(Material.GOLDEN_PICKAXE),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.GEMSTONE_SPREAD);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island");
                    lore.add(" ");
                    lore.add("§7Gemstone Spread is the chance to");
                    lore.add("§7automatically mine adjacent blocks");
                    lore.add("§7when mining Gemstones.");
                    lore.add(" ");
                    addFormateNumberLore(value, "block", ItemStatistic.GEMSTONE_SPREAD, lore);
                    return lore;
                }
        ),

        PRISTINE(13, ItemStatistic.PRISTINE, new GUIMaterial("d886e0f41185b18a3afd89488d2ee4caa0735009247cccf039ced6aed752ff1a"),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.PRISTINE);
                    return List.of(
                            "§7Pristine is the chance to increase",
                            "§7the quality of a Gemstone when it's",
                            "§7dropped.",
                            " ",
                            "§7Chance: " + "§a" + StringUtility.decimalify(value, 1) + "%",
                            " "
                    );
                }
        ),

        MINING_FORTUNE(14, ItemStatistic.MINING_FORTUNE, new GUIMaterial("b73579575ca88b3a8afe1ed18907b3125fe0987b02a88ef0e8a01087c3d024c4"),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.MINING_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Mining Fortune is the chance to get");
                    lore.add("§7multiple drops from Blocks, Ores,");
                    lore.add("§7Dwarven Metals, and Gemstones.");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.MINING_FORTUNE, lore);
                    return lore;
                }
        ),

        ORE_FORTUNE(15, ItemStatistic.ORE_FORTUNE, new GUIMaterial("b73579575ca88b3a8afe1ed18907b3125fe0987b02a88ef0e8a01087c3d024c4"),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.ORE_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Ore Fortune is the chance to get");
                    lore.add("§7multiple drops from §6Ores§7. This");
                    lore.add("§7chance is added on top of your §6☘");
                    lore.add("§6Mining Fortune§7.");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.ORE_FORTUNE, lore);
                    return lore;
                }
        ),

        BLOCK_FORTUNE(16, ItemStatistic.BLOCK_FORTUNE, new GUIMaterial("b73579575ca88b3a8afe1ed18907b3125fe0987b02a88ef0e8a01087c3d024c4"),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.BLOCK_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Block Fortune is the chance to get");
                    lore.add("§7multiple drops from §9Blocks§7. This");
                    lore.add("§7chance is added on top of your §6☘");
                    lore.add("§6Mining Fortune§7.");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.BLOCK_FORTUNE, lore);
                    return lore;
                }
        ),

        DWARVEN_METAL_FORTUNE(19, ItemStatistic.DWARVEN_METAL_FORTUNE, new GUIMaterial("b73579575ca88b3a8afe1ed18907b3125fe0987b02a88ef0e8a01087c3d024c4"),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.DWARVEN_METAL_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Dwarven Metal Fortune is the chance");
                    lore.add("§7to get multiple drops from §8Dwarven");
                    lore.add("§8Metals§7. This chance is added on top");
                    lore.add("§7of your §6☘ Mining Fortune§7.");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.DWARVEN_METAL_FORTUNE, lore);
                    return lore;
                }
        ),

        GEMSTONE_FORTUNE(20, ItemStatistic.GEMSTONE_FORTUNE, new GUIMaterial("b73579575ca88b3a8afe1ed18907b3125fe0987b02a88ef0e8a01087c3d024c4"),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.GEMSTONE_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Gemstone Fortune is the chance to");
                    lore.add("§7get multiple drops from §dGemstones§7.");
                    lore.add("§7This chance is added on top of your");
                    lore.add("§7§6☘ Mining Fortune§7.");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.GEMSTONE_FORTUNE, lore);
                    return lore;
                }
        ),

        FORAGING_FORTUNE(21, ItemStatistic.FORAGING_FORTUNE, new GUIMaterial("4e44e2a8dff90f5b005e76e6f5db7c12ae59cbbc56d8bc8050f3e3dbf0c3b734"),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.FORAGING_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Foraging Fortune is the chance to");
                    lore.add("§7gain multiple drops from logs.");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.FORAGING_FORTUNE, lore);
                    return lore;
                }
        ),

        FARMING_FORTUNE(22, ItemStatistic.FARMING_FORTUNE, new GUIMaterial("220ee7741ff1b958dbb9fa7cddad9c3cce93373f470f9b834da02da67c8202a4"),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.FARMING_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Farming Fortune is the chance to");
                    lore.add("§7gain multiple drops from crops.");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.FARMING_FORTUNE, lore);
                    return lore;
                }
        ),

        HUNTER_FORTUNE(23, ItemStatistic.HUNTER_FORTUNE, new GUIMaterial(Material.PRISMARINE_SHARD),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.HUNTER_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7Hunting Fortune is the chance to get");
                    lore.add("§7multiple shards from a Hunt, such as");
                    lore.add("§7charming a monster, and using");
                    lore.add("§7Hunting Tools.");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.HUNTER_FORTUNE, lore);
                    return lore;
                }
        ),

        SWEEP(24, ItemStatistic.SWEEP, new GUIMaterial(Material.DIAMOND_AXE),
                player -> List.of(
                        "§7§cDisabled by: §fNot Foraging Island",
                        "§f§7(Hub, The Park or Galatea)",
                        " ",
                        "§7Sweep is the ability to cut multiple",
                        "§7logs at once.",
                        " "
                )
        ),

        WHEAT_FORTUNE(25, ItemStatistic.WHEAT_FORTUNE, new GUIMaterial(Material.WHEAT),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.WHEAT_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Wheat Fortune is the chance to gain");
                    lore.add("§7multiple drops from §aWheat§7. This");
                    lore.add("§7chance is added on top of your §6☘");
                    lore.add("§6Farming Fortune§7.");
                    lore.add(" ");
                    lore.add("§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.WHEAT_FORTUNE, lore);
                    return lore;
                }
        ),

        CARROT_FORTUNE(28, ItemStatistic.CARROT_FORTUNE, new GUIMaterial(Material.CARROT),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.CARROT_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Carrot Fortune is the chance to gain");
                    lore.add("§7multiple drops from §aCarrot§7. This");
                    lore.add("§7chance is added on top of your §6☘");
                    lore.add("§6Farming Fortune§7.");
                    lore.add(" ");
                    lore.add("§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.CARROT_FORTUNE, lore);
                    return lore;
                }
        ),

        POTATO_FORTUNE(29, ItemStatistic.POTATO_FORTUNE, new GUIMaterial(Material.POTATO),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.POTATO_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Potato Fortune is the chance to gain");
                    lore.add("§7multiple drops from §aPotato§7. This");
                    lore.add("§7chance is added on top of your §6☘");
                    lore.add("§6Farming Fortune§7.");
                    lore.add(" ");
                    lore.add("§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.POTATO_FORTUNE, lore);
                    return lore;
                }
        ),

        PUMPKIN_FORTUNE(30, ItemStatistic.PUMPKIN_FORTUNE, new GUIMaterial(Material.CARVED_PUMPKIN),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.PUMPKIN_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Pumpkin Fortune is the chance to");
                    lore.add("§7gain multiple drops from §aPumpkin§7. This");
                    lore.add("§7chance is added on top of your §6☘");
                    lore.add("§6Farming Fortune§7.");
                    lore.add(" ");
                    lore.add("§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.PUMPKIN_FORTUNE, lore);
                    return lore;
                }
        ),

        MELON_FORTUNE(31, ItemStatistic.MELON_FORTUNE, new GUIMaterial(Material.MELON_SLICE),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.MELON_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Melon Fortune is the chance to gain");
                    lore.add("§7multiple drops from §aMelon§7. This");
                    lore.add("§7chance is added on top of your §6☘");
                    lore.add("§6Farming Fortune§7.");
                    lore.add(" ");
                    lore.add("§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.MELON_FORTUNE, lore);
                    return lore;
                }
        ),

        MUSHROOM_FORTUNE(32, ItemStatistic.MUSHROOM_FORTUNE, new GUIMaterial(Material.RED_MUSHROOM),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.MUSHROOM_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Mushroom Fortune is the chance to");
                    lore.add("§7gain multiple drops from §aMushroom§7.");
                    lore.add("§7This chance is added on top of your");
                    lore.add("§7§6☘ Farming Fortune§7.");
                    lore.add(" ");
                    lore.add("§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.MUSHROOM_FORTUNE, lore);
                    return lore;
                }
        ),

        CACTUS_FORTUNE(33, ItemStatistic.CACTUS_FORTUNE, new GUIMaterial(Material.CACTUS),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.CACTUS_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Cactus Fortune is the chance to gain");
                    lore.add("§7multiple drops from §aCactus§7. This");
                    lore.add("§7chance is added on top of your §6☘");
                    lore.add("§6Farming Fortune§7.");
                    lore.add(" ");
                    lore.add("§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.CACTUS_FORTUNE, lore);
                    return lore;
                }
        ),

        SUGAR_CANE_FORTUNE(34, ItemStatistic.SUGAR_CANE_FORTUNE, new GUIMaterial(Material.SUGAR_CANE),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.SUGAR_CANE_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Sugar Cane Fortune is the chance to");
                    lore.add("§7gain multiple drops from §aSugar Cane§7.");
                    lore.add("§7This chance is added on top of your");
                    lore.add("§7§6☘ Farming Fortune§7.");
                    lore.add(" ");
                    lore.add("§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.SUGAR_CANE_FORTUNE, lore);
                    return lore;
                }
        ),

        NETHER_WART_FORTUNE(37, ItemStatistic.NETHER_WART_FORTUNE, new GUIMaterial(Material.NETHER_WART),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.NETHER_WART_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Nether Wart Fortune is the chance");
                    lore.add("§7to gain multiple drops from §aNether");
                    lore.add("§aWart§7. This chance is added on top of");
                    lore.add("§7your §6☘ Farming Fortune§7.");
                    lore.add(" ");
                    lore.add("§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.NETHER_WART_FORTUNE, lore);
                    return lore;
                }
        ),

        COCOA_BEANS_FORTUNE(38, ItemStatistic.COCOA_BEANS_FORTUNE, new GUIMaterial(Material.COCOA_BEANS),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.COCOA_BEANS_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Cocoa Beans Fortune is the chance");
                    lore.add("§7to gain multiple drops from §aCocoa");
                    lore.add("§aBeans§7. This chance is added on top");
                    lore.add("§7of your §6☘ Farming Fortune§7.");
                    lore.add(" ");
                    lore.add("§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.COCOA_BEANS_FORTUNE, lore);
                    return lore;
                }
        ),

        FIG_FORTUNE(39, ItemStatistic.FIG_FORTUNE, new GUIMaterial(Material.STRIPPED_SPRUCE_LOG),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.FIG_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Fig Fortune is the chance to gain");
                    lore.add("§7multiple drops from §aFig Trees§7. This");
                    lore.add("§7chance is added on top of your §6☘");
                    lore.add("§6Foraging Fortune§7.");
                    lore.add(" ");
                    lore.add("§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.FIG_FORTUNE, lore);
                    return lore;
                }
        ),

        MANGROVE_FORTUNE(40, ItemStatistic.MANGROVE_FORTUNE, new GUIMaterial(Material.MANGROVE_LOG),
                player -> {
                    double value = player.getStatistics().allStatistics().getOverall(ItemStatistic.MANGROVE_FORTUNE);
                    List<String> lore = new ArrayList<>();
                    lore.add("§7§cDisabled by: §fPrivate Island §7(Use the");
                    lore.add("§7Public Islands or The Garden)");
                    lore.add(" ");
                    lore.add("§7Mangrove Fortune is the chance to");
                    lore.add("§7gain multiple drops from §aMangrove");
                    lore.add("§aTrees§7. This chance is added on top");
                    lore.add("§7of your §6☘ Foraging Fortune§7.");
                    lore.add(" ");
                    lore.add("§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘");
                    lore.add(" ");
                    addFormateNumberLore(value, "drops", ItemStatistic.MANGROVE_FORTUNE, lore);
                    return lore;
                }
        );

        private final int slot;
        private final ItemStatistic statistic;
        private final GUIMaterial guiMaterial;
        private final Function<SkyBlockPlayer, List<String>> baseLoreProvider;

        GatheringStat(int slot, ItemStatistic statistic, GUIMaterial guiMaterial,
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
                    lore.add(" " + statistic.getFullDisplayName() + " §f" +
                            StringUtility.decimalify(value, 2) + statistic.getSuffix());
                }
            });

            return ItemStackCreator.getStack("§eGathering Stats", Material.IRON_PICKAXE, 1, lore);
        });

        for (GatheringStat stat : GatheringStat.values()) {
            layout.slot(stat.slot, (s, c) -> stat.buildItemStack((SkyBlockPlayer) c.player()),
                    (click, c) -> ((SkyBlockPlayer) c.player()).sendMessage("§aUnder construction!"));
        }
    }

    @Override
    public boolean onBottomClick(net.swofty.type.generic.gui.v2.context.ClickContext<DefaultState> click, ViewContext ctx) {
        return true;
    }

    private static void addFormateNumberLore(double value, String type, ItemStatistic statistic, List<String> lore) {
        if (value < 300D) {
            if (value >= 100D) {
                lore.add("§7Chance for " + multiplesMap[(int) value / 100 - 1] + " §7" + type + ": §a100%");
            }
            lore.add("§7Chance for " + multiplesMap[(int) value / 100] + " §7" + type + ": §a" + ((int) value % 100) + "%");
        } else {
            lore.add("§7Bonus drops: " + statistic.getDisplayColor() + "+" + StringUtility.commaify((int) (value / 100)) + "!");
            lore.add("§7Chance for 1 more: " + statistic.getDisplayColor() + StringUtility.commaify(value % 100) + "%");
        }
        lore.add(" ");
    }
}