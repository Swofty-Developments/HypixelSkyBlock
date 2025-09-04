package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.stats;

import net.minestom.server.entity.PlayerSkin;
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

public class GUIGatheringStats extends HypixelInventoryGUI {

    private static final String[] multiplesMap = new String[]{
            "§adouble",
            "§6triple",
            "§5quadruple"
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
                ItemStatistic statistic = ItemStatistic.MINING_SPEED;
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
                List<String> lore = new ArrayList<>();

                lore.add("§7§cDisabled by: §fPrivate Island");
                lore.add(" ");
                lore.add("§7Mining Spread is the chance to");
                lore.add("§7automatically mine adjacent blocks");
                lore.add("§7Blocks, Ores, and");
                lore.add("§7Dwarven Metals.");
                lore.add(" ");

                if (value != 0D) {
                    lore.add("§7Flat: " + statistic.getDisplayColor() + "+" + StringUtility.commaify(value) + statistic.getSymbol());
                    lore.add("§7Stat Cap: " + statistic.getDisplayColor() + StringUtility.commaify(10000) + statistic.getSymbol());
                    lore.add(" ");
                }

                addFormateNumberLore(value, "block", statistic, lore);

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
                List<String> lore = new ArrayList<>(List.of(
                        "§7§cDisabled by: §fPrivate Island",
                        " ",
                        "§7Gemstone Spread is the chance to",
                        "§7automatically mine adjacent blocks",
                        "§7when mining Gemstones.",
                        " "
                ));

                addFormateNumberLore(value, "block", statistic, lore);

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
                                StringUtility.decimalify(value, 1),
                        "d886e0f41185b18a3afd89488d2ee4caa0735009247cccf039ced6aed752ff1a", 1, lore
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

                addFormateNumberLore(value, "drops", statistic, lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStackHead(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        "b73579575ca88b3a8afe1ed18907b3125fe0987b02a88ef0e8a01087c3d024c4", 1, lore
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

                addFormateNumberLore(value, "drops", statistic, lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStackHead(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        "b73579575ca88b3a8afe1ed18907b3125fe0987b02a88ef0e8a01087c3d024c4", 1, lore
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

                addFormateNumberLore(value, "drops", statistic, lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStackHead(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        new PlayerSkin(
                                "ewogICJ0aW1lc3RhbXAiIDogMTcwNjczNTY3MTQ2OSwKICAicHJvZmlsZUlkIiA6ICI2NTk0YzdiMTExOWE0Njc3ODc0Y2ZmOWNlMzM3NzYxOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYXJzaG1lbGxvMjIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjczNTc5NTc1Y2E4OGIzYThhZmUxZWQxODkwN2IzMTI1ZmUwOTg3YjAyYTg4ZWYwZThhMDEwODdjM2QwMjRjNCIKICAgIH0KICB9Cn0==",
                                "t/ckqd8i3jF6GLH6i7MxxNvCArtdJeTcVB+KNhGkK1saTDLXPWpRzMYD3yzsT5n2696Z3kki1V/89Acx/mP02EZvspjUj5ShDf5yKZA/rnKqkM9T6j06SVb2VOqWAV0AiOY/BiIY5iMXXP3KNO8cymZlRVMl4gN2bSt0KZ+1bjGIr6Sjd1ulTqMtCJIj9Zd3XJtWULu/XEC/oM2FJmJRFE1Uridei6VxjUhwy3KN+Sm4EqeEPX4afJk1X0cUhI/OrDuTj0vQQgFbHW8Rqr/S/Cg+AJTu6poStEnnzbQ0Vf+SpOoossIr2QJRc03K88xZBpUUDnyTqVCs5RHCAcmftGqKfq/KaCph4QHbgnbJf/iYJ2WzmIadgW/nxqnaanmhIdHhlEQV256vUl4MiFwpYSqlHJ7HgiaJ94fcnNaHa8U9K6sN5LHb10bZL4skf2Js/yobfX5xzxg9LIjDgt3digu61ouQJQIFLz9WkwRSwOALlWDar8MyPJJOSyX36AH3FpLAx88Ut4IlN6W4WQOdqQnTPtQHtO7/jRRmvYBz5VGGjdFB32zWNMaK1nLnC8Eflt3PzX9BY9QLaZp+qzYV6Mq4B2bovI5QNooitUAqSacFfhQWIs0aaE46V9g30lU4pKmMbuUEFh6D4wmPXIFDsqIzCT6A8b8A+EiCaOGXz3Q="
                        ), 1, lore
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

                addFormateNumberLore(value, "drops", statistic, lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStackHead(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        new PlayerSkin(
                                "ewogICJ0aW1lc3RhbXAiIDogMTcwNjczNTY3MTQ2OSwKICAicHJvZmlsZUlkIiA6ICI2NTk0YzdiMTExOWE0Njc3ODc0Y2ZmOWNlMzM3NzYxOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYXJzaG1lbGxvMjIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjczNTc5NTc1Y2E4OGIzYThhZmUxZWQxODkwN2IzMTI1ZmUwOTg3YjAyYTg4ZWYwZThhMDEwODdjM2QwMjRjNCIKICAgIH0KICB9Cn0==",
                                "t/ckqd8i3jF6GLH6i7MxxNvCArtdJeTcVB+KNhGkK1saTDLXPWpRzMYD3yzsT5n2696Z3kki1V/89Acx/mP02EZvspjUj5ShDf5yKZA/rnKqkM9T6j06SVb2VOqWAV0AiOY/BiIY5iMXXP3KNO8cymZlRVMl4gN2bSt0KZ+1bjGIr6Sjd1ulTqMtCJIj9Zd3XJtWULu/XEC/oM2FJmJRFE1Uridei6VxjUhwy3KN+Sm4EqeEPX4afJk1X0cUhI/OrDuTj0vQQgFbHW8Rqr/S/Cg+AJTu6poStEnnzbQ0Vf+SpOoossIr2QJRc03K88xZBpUUDnyTqVCs5RHCAcmftGqKfq/KaCph4QHbgnbJf/iYJ2WzmIadgW/nxqnaanmhIdHhlEQV256vUl4MiFwpYSqlHJ7HgiaJ94fcnNaHa8U9K6sN5LHb10bZL4skf2Js/yobfX5xzxg9LIjDgt3digu61ouQJQIFLz9WkwRSwOALlWDar8MyPJJOSyX36AH3FpLAx88Ut4IlN6W4WQOdqQnTPtQHtO7/jRRmvYBz5VGGjdFB32zWNMaK1nLnC8Eflt3PzX9BY9QLaZp+qzYV6Mq4B2bovI5QNooitUAqSacFfhQWIs0aaE46V9g30lU4pKmMbuUEFh6D4wmPXIFDsqIzCT6A8b8A+EiCaOGXz3Q="
                        ), 1, lore
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

                addFormateNumberLore(value, "drops", statistic, lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStackHead(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        "b73579575ca88b3a8afe1ed18907b3125fe0987b02a88ef0e8a01087c3d024c4", 1, lore
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

                addFormateNumberLore(value, "drops", statistic, lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStackHead(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        "4e44e2a8dff90f5b005e76e6f5db7c12ae59cbbc56d8bc8050f3e3dbf0c3b734", 1, lore
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

                addFormateNumberLore(value, "drops", statistic, lore);

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStackHead(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        "220ee7741ff1b958dbb9fa7cddad9c3cce93373f470f9b834da02da67c8202a4", 1, lore
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

                addFormateNumberLore(value, "drops", statistic, lore);

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
                        "§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘",
                        " "
                ));

                addFormateNumberLore(value, "drops", statistic, lore);

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
                        "§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘",
                        " "
                ));

                addFormateNumberLore(value, "drops", statistic, lore);

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
                        "§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘",
                        " "
                ));

                addFormateNumberLore(value, "drops", statistic, lore);

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
                        "§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘",
                        " "
                ));

                addFormateNumberLore(value, "drops", statistic, lore);

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
                        "§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘",
                        " "
                ));

                addFormateNumberLore(value, "drops", statistic, lore);

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
                        "§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘",
                        " "
                ));

                addFormateNumberLore(value, "drops", statistic, lore);

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
                        "§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘",
                        " "
                ));

                addFormateNumberLore(value, "drops", statistic, lore);

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
                        "§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘",
                        " "
                ));

                addFormateNumberLore(value, "drops", statistic, lore);

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
                        "§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘",
                        " "
                ));

                addFormateNumberLore(value, "drops", statistic, lore);

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
                        "§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘",
                        " "
                ));

                addFormateNumberLore(value, "drops", statistic, lore);

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
                        "§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘",
                        " "
                ));

                addFormateNumberLore(value, "drops", statistic, lore);

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
                        "§7Total Fortune: §6+" + StringUtility.decimalify(value, 2) + "☘",
                        " "
                ));

                addFormateNumberLore(value, "drops", statistic, lore);

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
