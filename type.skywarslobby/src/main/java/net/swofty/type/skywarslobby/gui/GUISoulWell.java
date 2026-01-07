package net.swofty.type.skywarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointSoulWellUpgrades;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarslobby.soulwell.SoulWellMessages;
import net.swofty.type.skywarslobby.soulwell.SoulWellUpgrade;
import net.swofty.type.skywarslobby.soulwell.SoulWellUpgradeRegistry;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GUISoulWell extends HypixelInventoryGUI {
    private static final int BASE_ROLL_COST = 2; // Cost per wheel

    public GUISoulWell() {
        super("Soul Well", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
        long souls = handler != null ? handler.get(SkywarsDataHandler.Data.SOULS, DatapointLong.class).getValue() : 0;
        long coins = handler != null ? handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue() : 0;
        DatapointSoulWellUpgrades.SoulWellUpgrades playerUpgrades = handler != null
                ? handler.get(SkywarsDataHandler.Data.SOUL_WELL_UPGRADES, DatapointSoulWellUpgrades.class).getValue()
                : DatapointSoulWellUpgrades.SoulWellUpgrades.empty();

        int wheelCount = getWheelCount(handler);
        int rollCost = wheelCount * BASE_ROLL_COST;

        // Roll Soul Well button (slot 12)
        set(new GUIClickableItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aRoll Soul Well",
                        Material.END_PORTAL_FRAME,
                        1,
                        "§7Rolls for a random kit, perk, or coin",
                        "§7bonus.",
                        "",
                        "§7Cost: §b" + rollCost + " Souls",
                        "",
                        "§eClick to roll!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (handler == null) return;
                long currentSouls = handler.get(SkywarsDataHandler.Data.SOULS, DatapointLong.class).getValue();
                if (currentSouls < rollCost) {
                    player.sendMessage("§cYou don't have enough souls!");
                    return;
                }

                // Deduct souls
                handler.get(SkywarsDataHandler.Data.SOULS, DatapointLong.class).setValue(currentSouls - rollCost);

                // Open rolling GUI
                new GUISoulWellRolling(wheelCount).open(player);
            }
        });

        // Soul Well Wheels setting (slot 14)
        set(new GUIClickableItem(14) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                int wheels = getWheelCount(handler);
                int cost = wheels * BASE_ROLL_COST;
                String decreaseText = wheels > 1 ? "§eRight-click to decrease!" : "";
                String increaseText = wheels < 5 ? "§eLeft-click to increase!" : "";

                List<String> lore = new ArrayList<>();
                lore.add("§8Setting");
                lore.add("");
                lore.add("§7Change the number of wheels your");
                lore.add("§bSoul Well §7will spin each roll. §8(max 5)");
                lore.add("");
                lore.add("§7# of Wheels: §a" + wheels + " §8(" + cost + " Souls)");
                lore.add("");
                if (!increaseText.isEmpty()) lore.add(increaseText);
                if (!decreaseText.isEmpty()) lore.add(decreaseText);

                return ItemStackCreator.getStack("§6Soul Well Wheels", Material.ENCHANTING_TABLE, 1, lore);
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (handler == null) return;
                int currentWheels = getWheelCount(handler);

                if (e.getClick() instanceof Click.Left && currentWheels < 5) {
                    setWheelCount(handler, currentWheels + 1);
                } else if (e.getClick() instanceof Click.Right && currentWheels > 1) {
                    setWheelCount(handler, currentWheels - 1);
                }

                // Refresh GUI
                new GUISoulWell().open(player);
            }
        });

        // Xezbeth Luck (slot 28)
        setUpgradeItem(28, "xezbeth_luck", playerUpgrades, coins, handler);

        // Harvesting Season (slot 30)
        setUpgradeItem(30, "harvesting_season", playerUpgrades, coins, handler);

        // Angel of Death (slot 32)
        setUpgradeItem(32, "angel_of_death", playerUpgrades, coins, handler);

        // Head Collection (slot 34) - Skipped per user request
        set(new GUIItem(34) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§cHead Collection",
                        Material.CHEST,
                        1,
                        "§7View your collection of §cHeads§7.",
                        "",
                        "§7Players drop their §cHeads §7when killed",
                        "§7in §5Corrupted Games§7!",
                        "",
                        "§7Total Heads: §a0",
                        "",
                        "§8Coming soon..."
                );
            }
        });

        // Total Coins display (slot 50)
        set(new GUIItem(50) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                String formattedCoins = NumberFormat.getNumberInstance(Locale.US).format(coins);
                return ItemStackCreator.getStack(
                        "§7Total Coins: §6" + formattedCoins,
                        Material.EMERALD,
                        1,
                        "§6https://store.hypixel.net"
                );
            }
        });

        // Close button (slot 49)
        set(GUIClickableItem.getCloseItem(49));

        updateItemStacks(getInventory(), getPlayer());
    }

    private void setUpgradeItem(int slot, String upgradeId, DatapointSoulWellUpgrades.SoulWellUpgrades playerUpgrades,
                                 long coins, SkywarsDataHandler handler) {
        SoulWellUpgrade upgrade = SoulWellUpgradeRegistry.getUpgrade(upgradeId);
        if (upgrade == null) return;

        int currentLevel = playerUpgrades.getUpgradeLevel(upgradeId);
        boolean isMaxed = upgrade.isMaxed(currentLevel);
        SoulWellUpgrade.SoulWellUpgradeTier nextTier = upgrade.getNextTier(currentLevel);

        set(new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                List<String> lore = new ArrayList<>();
                lore.add("§8Permanent Upgrade");
                lore.add("");
                lore.add("§7" + upgrade.baseDescription());
                lore.add("");

                String colorCode = upgrade.color();

                if (isMaxed) {
                    // Maxed out
                    SoulWellUpgrade.SoulWellUpgradeTier currentTier = upgrade.getTier(currentLevel);
                    if (currentTier != null) {
                        lore.add("§7Current: §" + colorCode + currentTier.newEffect() + " §7" + currentTier.effectDescription());
                    }
                    lore.add("");
                    lore.add("§aMAXED OUT!");

                    return ItemStackCreator.getStack(
                            "§" + colorCode + upgrade.name() + " " + SoulWellMessages.toRoman(currentLevel),
                            upgrade.material(),
                            1,
                            lore
                    );
                } else if (nextTier != null) {
                    // Can upgrade
                    lore.add(nextTier.getEffectChangeLine());
                    lore.add("");

                    String formattedCost = NumberFormat.getNumberInstance(Locale.US).format(nextTier.cost());
                    boolean canAfford = coins >= nextTier.cost();

                    lore.add("§7Cost: §6" + formattedCost);
                    lore.add("");

                    if (canAfford) {
                        lore.add("§eClick to purchase!");
                    } else {
                        lore.add("§cYou can't afford this!");
                    }

                    String displayName;
                    if (currentLevel == 0) {
                        displayName = "§" + colorCode + upgrade.name();
                    } else {
                        displayName = "§" + colorCode + upgrade.name() + " " + SoulWellMessages.toRoman(currentLevel)
                                + " §l→ §" + colorCode + SoulWellMessages.toRoman(currentLevel + 1);
                    }

                    return ItemStackCreator.getStack(displayName, upgrade.material(), 1, lore);
                }

                return ItemStackCreator.getStack("§7" + upgrade.name(), upgrade.material(), 1, lore);
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (isMaxed) {
                    player.sendMessage("§cThis upgrade is already maxed out!");
                    return;
                }

                if (nextTier == null) {
                    player.sendMessage("§cNo upgrade tier found!");
                    return;
                }

                // Open confirmation GUI
                new GUISoulWellConfirm(upgrade, nextTier, currentLevel + 1).open(player);
            }
        });
    }

    private int getWheelCount(SkywarsDataHandler handler) {
        if (handler == null) return 3;
        DatapointSoulWellUpgrades.SoulWellUpgrades upgrades = handler.get(
                SkywarsDataHandler.Data.SOUL_WELL_UPGRADES, DatapointSoulWellUpgrades.class).getValue();
        return upgrades.getWheelCount();
    }

    private void setWheelCount(SkywarsDataHandler handler, int count) {
        if (handler == null) return;
        DatapointSoulWellUpgrades.SoulWellUpgrades upgrades = handler.get(
                SkywarsDataHandler.Data.SOUL_WELL_UPGRADES, DatapointSoulWellUpgrades.class).getValue();
        upgrades.setWheelCount(count);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
