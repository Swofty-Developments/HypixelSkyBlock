package net.swofty.type.skywarslobby.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointSoulWellUpgrades;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skywarslobby.soulwell.SoulWellMessages;
import net.swofty.type.skywarslobby.soulwell.SoulWellUpgrade;
import net.swofty.type.skywarslobby.soulwell.SoulWellUpgradeRegistry;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GUISoulWell extends StatelessView {
    private static final int BASE_ROLL_COST = 2;

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(Component.text("Soul Well"), InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.allowHotkey(false);
        layout.filler(Components.FILLER);

        SkywarsDataHandler handler = SkywarsDataHandler.getUser(ctx.player());
        long coins = handler != null ? handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue() : 0;
        DatapointSoulWellUpgrades.SoulWellUpgrades playerUpgrades = handler != null
                ? handler.get(SkywarsDataHandler.Data.SOUL_WELL_UPGRADES, DatapointSoulWellUpgrades.class).getValue()
                : DatapointSoulWellUpgrades.SoulWellUpgrades.empty();

        int wheelCount = getWheelCount(handler);
        int rollCost = wheelCount * BASE_ROLL_COST;

        // Roll Soul Well button (slot 12)
        layout.slot(12,
                (_, _) -> ItemStackCreator.getStack(
                        "§aRoll Soul Well",
                        Material.END_PORTAL_FRAME,
                        1,
                        "§7Rolls for a random kit, perk, or coin",
                        "§7bonus.",
                        "",
                        "§7Cost: §b" + rollCost + " Souls",
                        "",
                        "§eClick to roll!"
                ),
                (_, c) -> {
                    if (handler == null) return;
                    long currentSouls = handler.get(SkywarsDataHandler.Data.SOULS, DatapointLong.class).getValue();
                    if (currentSouls < rollCost) {
                        c.player().sendMessage("§cYou don't have enough souls!");
                        return;
                    }

                    handler.get(SkywarsDataHandler.Data.SOULS, DatapointLong.class).setValue(currentSouls - rollCost);

                    c.push(new GUISoulWellRolling(wheelCount));
                }
        );

        // Soul Well Wheels setting (slot 14)
        layout.slot(14,
                (_, _) -> {
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
                },
                (click, c) -> {
                    if (handler == null) return;
                    int currentWheels = getWheelCount(handler);

                    if (click.click() instanceof Click.Left && currentWheels < 5) {
                        setWheelCount(handler, currentWheels + 1);
                    } else if (click.click() instanceof Click.Right && currentWheels > 1) {
                        setWheelCount(handler, currentWheels - 1);
                    }

                    c.session(Object.class).refresh();
                }
        );

        // Upgrade items
        layoutUpgradeItem(layout, 28, "xezbeth_luck", playerUpgrades, coins);
        layoutUpgradeItem(layout, 30, "harvesting_season", playerUpgrades, coins);
        layoutUpgradeItem(layout, 32, "angel_of_death", playerUpgrades, coins);

        // Head Collection placeholder (slot 34)
        layout.slot(34, (_, _) -> ItemStackCreator.getStack(
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
        ));

        // Total Coins display (slot 50)
        layout.slot(50, (_, _) -> {
            String formattedCoins = NumberFormat.getNumberInstance(Locale.US).format(coins);
            return ItemStackCreator.getStack(
                    "§7Total Coins: §6" + formattedCoins,
                    Material.EMERALD,
                    1,
                    "§6https://store.hypixel.net"
            );
        });

        // Close button (slot 49)
        Components.close(layout, 49);
    }

    private void layoutUpgradeItem(ViewLayout<DefaultState> layout,
                                   int slot,
                                   String upgradeId,
                                   DatapointSoulWellUpgrades.SoulWellUpgrades playerUpgrades,
                                   long coins) {
        SoulWellUpgrade upgrade = SoulWellUpgradeRegistry.getUpgrade(upgradeId);
        if (upgrade == null) return;

        int currentLevel = playerUpgrades.getUpgradeLevel(upgradeId);
        boolean isMaxed = upgrade.isMaxed(currentLevel);
        SoulWellUpgrade.SoulWellUpgradeTier nextTier = upgrade.getNextTier(currentLevel);

        layout.slot(slot,
                (_, _) -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§8Permanent Upgrade");
                    lore.add("");
                    lore.add("§7" + upgrade.baseDescription());
                    lore.add("");

                    String colorCode = upgrade.color();

                    if (isMaxed) {
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
                    }

                    if (nextTier != null) {
                        lore.add(nextTier.getEffectChangeLine());
                        lore.add("");

                        String formattedCost = NumberFormat.getNumberInstance(Locale.US).format(nextTier.cost());
                        boolean canAfford = coins >= nextTier.cost();

                        lore.add("§7Cost: §6" + formattedCost);
                        lore.add("");

                        if (canAfford) lore.add("§eClick to purchase!");
                        else lore.add("§cYou can't afford this!");

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
                },
                (_, c) -> {
                    if (isMaxed) {
                        c.player().sendMessage("§cThis upgrade is already maxed out!");
                        return;
                    }
                    if (nextTier == null) {
                        c.player().sendMessage("§cNo upgrade tier found!");
                        return;
                    }

                    c.push(new GUISoulWellConfirm(upgrade, nextTier, currentLevel + 1));
                }
        );
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
}
