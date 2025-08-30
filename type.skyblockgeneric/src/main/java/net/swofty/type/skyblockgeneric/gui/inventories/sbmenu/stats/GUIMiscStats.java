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

public class GUIMiscStats extends HypixelInventoryGUI {
    public GUIMiscStats() {
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
                List<String> lore = new ArrayList<>(List.of("§7Augments various aspects of your", "§7gameplay! ", " "));
                List<ItemStatistic> stats = new ArrayList<>(List.of(ItemStatistic.SPEED, ItemStatistic.MAGIC_FIND, ItemStatistic.PET_LUCK,
                        ItemStatistic.COLD_RESISTANCE, ItemStatistic.BONUS_PEST_CHANCE, ItemStatistic.HEAT_RESISTANCE, ItemStatistic.FEAR,
                        ItemStatistic.PULL, ItemStatistic.RESPIRATION, ItemStatistic.PRESSURE_RESISTANCE
                ));

                statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                    if (stats.contains(statistic)) {
                        lore.add(" " + StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 2) + statistic.getSuffix());
                    }
                });

                return ItemStackCreator.getStack("§dMisc Stats", Material.CLOCK, 1, lore);
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
                ItemStatistic statistic = ItemStatistic.SPEED;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Your Speed stat increases how fast",
                        "§7you can walk.",
                        " ",
                        "§7Flat: " + statistic.getDisplayColor() + "+" + StringUtility.commaify(value) + statistic.getSymbol(),
                        "§7Stat Cap: " + statistic.getDisplayColor() + 400 + statistic.getSymbol() + " " + statistic.getDisplayName(),
                        " ",
                        value == statistic.getBaseAdditiveValue() ? "§7You walk at a regular walking speed." : "§7 You are §a" + ((int) (value / 100D) - 1) + "% §7faster!",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.SUGAR, 1, lore
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
                ItemStatistic statistic = ItemStatistic.MAGIC_FIND;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>();

                lore.add("§7Magic Find increases how many rare");
                lore.add("§7items you find.");
                lore.add(" ");

                if (value != 0D) {
                    lore.add("§7Flat: " + statistic.getDisplayColor() + "+" + StringUtility.commaify(value) + statistic.getSymbol());
                    lore.add("§7Stat Cap: " + statistic.getDisplayColor() + 900 + statistic.getSymbol() + " " + statistic.getDisplayName());
                    lore.add(" ");
                }

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.STICK, 1, lore
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
                ItemStatistic statistic = ItemStatistic.PET_LUCK;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Pet Luck increases how many pets",
                        "§7you find and gives you better luck",
                        "§7when crafting pets.",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStackHead(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        "bc78314255d8864a753fe95622564046f0dee2a82c6e4e2e7f452fcb95af318c", 1, lore
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
                ItemStatistic statistic = ItemStatistic.BONUS_PEST_CHANCE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Chance to spawn bonus " + statistic.getDisplayColor() + statistic.getSymbol() + " Pests",
                        "§7while on §aThe Garden§7.",
                        " ",
                        "§7Chance for §a" + ((int) value / 100 + 1) + " §7bonus pest: §a" + ((int) value % 100) + "%",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.DIRT, 1, lore
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
                ItemStatistic statistic = ItemStatistic.HEAT_RESISTANCE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Heat Resistances increases the",
                        "§7amount of time you can spend in hot",
                        "§7environments.",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.LAVA_BUCKET, 1, lore
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
                ItemStatistic statistic = ItemStatistic.COLD_RESISTANCE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Cold Resistance increases the",
                        "§7amount of time you can spend in cold",
                        "§7environments.",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.ICE, 1, lore
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
                ItemStatistic statistic = ItemStatistic.FEAR;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Makes Primal Fears spawn more",
                        "§7often and reduces damage taken",
                        "§7from Primal Fears.",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.CAULDRON, 1, lore
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
                ItemStatistic statistic = ItemStatistic.PULL;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Pull dictates both which fish you're",
                        "§7able to grab and how long it takes",
                        "§7using a fishing net.",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.COBWEB, 1, lore
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
                ItemStatistic statistic = ItemStatistic.RESPIRATION;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Extends underwater breathing time.",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.GLASS_BOTTLE, 1, lore
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
                ItemStatistic statistic = ItemStatistic.PRESSURE_RESISTANCE;
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                List<String> lore = new ArrayList<>(List.of(
                        "§7Pressure Resistance reduces the",
                        "§7effects of Pressure when diving.",
                        " ",
                        "§7§7You will start feeling the effects of",
                        "§7pressure only §90 §7blocks under water.",
                        "§7Pressure will also build up §90% §7slower.",
                        " "
                ));

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(StringUtility.getFormatedStatistic(statistic) + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.GLASS_BOTTLE, 1, lore
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
}
