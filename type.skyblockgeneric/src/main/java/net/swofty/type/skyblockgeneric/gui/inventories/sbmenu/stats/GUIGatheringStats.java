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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIGatheringStats extends HypixelInventoryGUI {

    private static final String[] multiplesMap = new String[] {
            "§adouble",
            "§6triple",
            "§5quadruple",
            "quintuple",
            "sextuple",
            "septuple",
            "octuple",
            "nonuple",
            "decuple"
    };

    public GUIGatheringStats() {
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
                List<String> lore = new ArrayList<>(List.of("§7Lets you collect and harvest better", "§7items, or more of them. "));
                List<ItemStatistic> stats = new ArrayList<>(List.of(ItemStatistic.MINING_SPEED, ItemStatistic.MINING_FORTUNE, ItemStatistic.BREAKING_POWER,
                        ItemStatistic.PRISTINE, ItemStatistic.FORAGING_FORTUNE, ItemStatistic.FARMING_FORTUNE));

                statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                    if (stats.contains(statistic)) {
                        lore.add(" " + StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 2) + statistic.getSuffix());
                    }
                });

                return ItemStackCreator.getStack("§eGathering Stats", Material.IRON_PICKAXE, 1, lore);
            }
        });

        set(new GUIClickableItem(10) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.MINING_SPREAD;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Increases the speed of breaking",
                        "§7mining blocks.",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.DIAMOND_PICKAXE, 1, lore
                );
            }
        });

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.MINING_SPREAD;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(
                        value == 0D ?
                                List.of(
                                        "§7§cDisabled by: §fPrivate Island",
                                        " ",
                                        "§7Mining Spread is the chance to",
                                        "§7automatically mine adjacent blocks",
                                        "§7Blocks, Ores, and",
                                        "§7Dwarven Metals.",
                                        " "
                                ) :
                                List.of(
                                        "§7§cDisabled by: §fPrivate Island",
                                        " ",
                                        "§7Mining Spread is the chance to",
                                        "§7automatically mine adjacent blocks",
                                        "§7Blocks, Ores, and",
                                        "§7Dwarven Metals.",
                                        " ",
                                        "§7Flat: " + statistic.getDisplayColor() + "+" + StringUtility.commaify(value) + statistic.getSymbol(),
                                        "§7Stat Cap: " + statistic.getDisplayColor() + StringUtility.commaify(10000) + statistic.getSymbol(),
                                        " "
                                )
                );

                formateNumber(value, lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.GOLDEN_PICKAXE, 1, lore
                );
            }
        });

        set(new GUIClickableItem(12) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.GEMSTONE_SPREAD;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(
                        value == 0D ?
                                List.of(
                                        "§7§cDisabled by: §fPrivate Island",
                                        " ",
                                        "§7Gemstone Spread is the chance to",
                                        "§7automatically mine adjacent blocks",
                                        "§7when mining Gemstones.",
                                        " "
                                ) :
                                List.of(
                                        "§7§cDisabled by: §fPrivate Island",
                                        " ",
                                        "§7Gemstone Spread is the chance to",
                                        "§7automatically mine adjacent blocks",
                                        "§7when mining Gemstones.",
                                        " ",
                                        "§7Flat: " + statistic.getDisplayColor() + "+" + StringUtility.commaify(value) + statistic.getSymbol(),
                                        "§7Stat Cap: " + statistic.getDisplayColor() + StringUtility.commaify(10000) + statistic.getSymbol(),
                                        " "
                                )
                );

                formateNumber(value, lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.GOLDEN_PICKAXE, 1, lore
                );
            }
        });

        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.PRISTINE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Pristine is the chance to increase",
                        "§7the quality of a Gemstone when it's",
                        "§7dropped.",
                        " ",
                        "§7Chance: " + "§a" + StringUtility.decimalify(value, 1) + "%",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStackHead(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1), "ewogICJ0aW1lc3RhbXAiIDogMTYyNDk3OTI3NDk3OSwKICAicHJvZmlsZUlkIiA6ICJiNjM2OWQ0MzMwNTU0NGIzOWE5OTBhODYyNWY5MmEwNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJCb2JpbmhvXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kODg2ZTBmNDExODViMThhM2FmZDg5NDg4ZDJlZTRjYWEwNzM1MDA5MjQ3Y2NjZjAzOWNlZDZhZWQ3NTJmZjFhIgogICAgfQogIH0KfQ==", 1,
                        lore
                );
            }
        });

        set(new GUIClickableItem(14) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.MINING_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Mining Fortune is the chance to get",
                        "§7multiple drops from Blocks, Ores,",
                        "§7Dwarven Metals, and Gemstones.",
                        " "
                ));

                formateNumber(value, lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStackHead(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1), "ewogICJ0aW1lc3RhbXAiIDogMTcwNjczNTY3MTQ2OSwKICAicHJvZmlsZUlkIiA6ICI2NTk0YzdiMTExOWE0Njc3ODc0Y2ZmOWNlMzM3NzYxOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYXJzaG1lbGxvMjIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjczNTc5NTc1Y2E4OGIzYThhZmUxZWQxODkwN2IzMTI1ZmUwOTg3YjAyYTg4ZWYwZThhMDEwODdjM2QwMjRjNCIKICAgIH0KICB9Cn0=", 1,
                        lore
                );
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.ORE_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Ore Fortune is the chance to get",
                        "§7multiple drops from §6Ores§7. This",
                        "§7chance is added on top of your §6☘",
                        "§6Mining Fortune§7.",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.BARRIER, 1, lore
                );
            }
        });

        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.BLOCK_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Block Fortune is the chance to get",
                        "§7multiple drops from §9Blocks§7. This",
                        "§7chance is added on top of your §6☘",
                        "§6Mining Fortune§7.",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.BARRIER, 1, lore
                );
            }
        });

        set(new GUIClickableItem(19) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.DWARVEN_METAL_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Dwarven Metal Fortune is the chance",
                        "§7to get multiple drops from §8Dwarven",
                        "§8Metals§7. This chance is added on top",
                        "§7of your §6☘ Mining Fortune§7.",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.BARRIER, 1, lore
                );
            }
        });

        set(new GUIClickableItem(20) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.GEMSTONE_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Gemstone Fortune is the chance to",
                        "§7get multiple drops from §dGemstones§7.",
                        "§7This chance is added on top of your",
                        "§7§6☘ Mining Fortune§7.",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.BARRIER, 1, lore
                );
            }
        });

        set(new GUIClickableItem(21) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.FORAGING_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Foraging Fortune is the chance to",
                        "§7gain multiple drops from logs.",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.BARRIER, 1, lore
                );
            }
        });

        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.FARMING_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Farming Fortune is the chance to",
                        "§7gain multiple drops from crops.",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.BARRIER, 1, lore
                );
            }
        });

        set(new GUIClickableItem(23) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.HUNTER_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Hunting Fortune is the chance to get",
                        "§7multiple shards from a Hunt, such as",
                        "§7charming a monster, and using",
                        "§7Hunting Tools.",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.PRISMARINE_SHARD, 1, lore
                );
            }
        });

        set(new GUIClickableItem(24) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.SWEEP;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fNot Foraging Island",
                        "§f§7(Hub, The Park or Galatea)",
                        " ",
                        "§7Sweep is the ability to cut multiple",
                        "§7logs at once.",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.DIAMOND_AXE, 1, lore
                );
            }
        });

        set(new GUIClickableItem(25) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.WHEAT_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Wheat Fortune is the chance to gain",
                        "§7multiple drops from §aWheat§7. This",
                        "§7chance is added on top of your §6☘",
                        "§6Farming Fortune§7.",
                        " ",
                        "§7Total Fortune: §6+"+StringUtility.decimalify(value, 2)+"☘",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.WHEAT, 1, lore
                );
            }
        });

        set(new GUIClickableItem(28) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.CARROT_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Carrot Fortune is the chance to gain",
                        "§7multiple drops from §aCarrot§7. This",
                        "§7chance is added on top of your §6☘",
                        "§6Farming Fortune§7.",
                        " ",
                        "§7Total Fortune: §6+"+StringUtility.decimalify(value, 2)+"☘",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.CARROT, 1, lore
                );
            }
        });

        set(new GUIClickableItem(29) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.POTATO_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Potato Fortune is the chance to gain",
                        "§7multiple drops from §aPotato§7. This",
                        "§7chance is added on top of your §6☘",
                        "§6Farming Fortune§7.",
                        " ",
                        "§7Total Fortune: §6+"+StringUtility.decimalify(value, 2)+"☘",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.POTATO, 1, lore
                );
            }
        });

        set(new GUIClickableItem(30) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.PUMPKIN_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Pumpkin Fortune is the chance to",
                        "§7gain multiple drops from §aPumpkin§7. This",
                        "§7chance is added on top of your §6☘",
                        "§6Farming Fortune§7.",
                        " ",
                        "§7Total Fortune: §6+"+StringUtility.decimalify(value, 2)+"☘",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.CARVED_PUMPKIN, 1, lore
                );
            }
        });

        set(new GUIClickableItem(31) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.MELON_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Melon Fortune is the chance to gain",
                        "§7multiple drops from §aMelon§7. This",
                        "§7chance is added on top of your §6☘",
                        "§6Farming Fortune§7.",
                        " ",
                        "§7Total Fortune: §6+"+StringUtility.decimalify(value, 2)+"☘",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.MELON_SLICE, 1, lore
                );
            }
        });

        set(new GUIClickableItem(32) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.MUSHROOM_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Mushroom Fortune is the chance to",
                        "§7gain multiple drops from §aMushroom§7.",
                        "§7This chance is added on top of your",
                        "§7§6☘ Farming Fortune§7.",
                        " ",
                        "§7Total Fortune: §6+"+StringUtility.decimalify(value, 2)+"☘",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.RED_MUSHROOM, 1, lore
                );
            }
        });

        set(new GUIClickableItem(33) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.CACTUS_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Cactus Fortune is the chance to gain",
                        "§7multiple drops from §aCactus§7. This",
                        "§7chance is added on top of your §6☘",
                        "§6Farming Fortune§7.",
                        " ",
                        "§7Total Fortune: §6+"+StringUtility.decimalify(value, 2)+"☘",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.CACTUS, 1, lore
                );
            }
        });

        set(new GUIClickableItem(34) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.SUGAR_CANE_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Sugar Cane Fortune is the chance to",
                        "§7gain multiple drops from §aSugar Cane§7.",
                        "§7This chance is added on top of your",
                        "§7§6☘ Farming Fortune§7.",
                        " ",
                        "§7Total Fortune: §6+"+StringUtility.decimalify(value, 2)+"☘",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.SUGAR_CANE, 1, lore
                );
            }
        });

        set(new GUIClickableItem(37) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.NETHER_WART_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Nether Wart Fortune is the chance",
                        "§7to gain multiple drops from §aNether",
                        "§aWart§7. This chance is added on top of",
                        "§7your §6☘ Farming Fortune§7.",
                        " ",
                        "§7Total Fortune: §6+"+StringUtility.decimalify(value, 2)+"☘",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.NETHER_WART, 1, lore
                );
            }
        });

        set(new GUIClickableItem(38) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.COCOA_BEANS_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Cocoa Beans Fortune is the chance",
                        "§7to gain multiple drops from §aCocoa",
                        "§aBeans§7. This chance is added on top",
                        "§7of your §6☘ Farming Fortune§7.",
                        " ",
                        "§7Total Fortune: §6+"+StringUtility.decimalify(value, 2)+"☘",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.COCOA_BEANS, 1, lore
                );
            }
        });

        set(new GUIClickableItem(39) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.FIG_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Fig Fortune is the chance to gain",
                        "§7multiple drops from §aFig Trees§7. This",
                        "§7chance is added on top of your §6☘",
                        "§6Foraging Fortune§7.",
                        " ",
                        "§7Total Fortune: §6+"+StringUtility.decimalify(value, 2)+"☘",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.STRIPPED_SPRUCE_LOG, 1, lore
                );
            }
        });

        set(new GUIClickableItem(40) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage("§aUnder construction!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStatistic statistic = ItemStatistic.MANGROVE_FORTUNE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island §7(Use the",
                        "§7Public Islands or The Garden)",
                        " ",
                        "§7Mangrove Fortune is the chance to",
                        "§7gain multiple drops from §aMangrove",
                        "§aTrees§7. This chance is added on top",
                        "§7of your §6☘ Foraging Fortune§7.",
                        " ",
                        "§7Total Fortune: §6+"+StringUtility.decimalify(value, 2)+"☘",
                        " "
                ));

                formateNumber(value,lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.MANGROVE_LOG, 1, lore
                );
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

    private static void formateNumber(double value, List<String> lore) {
        if (value >= 100D) {
            lore.add("§7Chance for " + multiplesMap[(int) value / 100 - 1] + " §7blocks: §a100%");
        }
        lore.add("§7Chance for " + multiplesMap[(int) value / 100] + " §7blocks: §a" + ((int) value % 100) + "%");
        lore.add(" ");
    }
}
