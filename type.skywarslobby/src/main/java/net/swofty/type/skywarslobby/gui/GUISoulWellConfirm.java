package net.swofty.type.skywarslobby.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointSoulWellUpgrades;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skywarslobby.soulwell.SoulWellMessages;
import net.swofty.type.skywarslobby.soulwell.SoulWellUpgrade;

import java.text.NumberFormat;
import java.util.Locale;

public class GUISoulWellConfirm extends StatelessView {

    private final SoulWellUpgrade upgrade;
    private final SoulWellUpgrade.SoulWellUpgradeTier tier;
    private final int newLevel;

    public GUISoulWellConfirm(SoulWellUpgrade upgrade, SoulWellUpgrade.SoulWellUpgradeTier tier, int newLevel) {
        this.upgrade = upgrade;
        this.tier = tier;
        this.newLevel = newLevel;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(Component.text("Are you sure?"), InventoryType.CHEST_3_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.allowHotkey(false);
        layout.filler(Components.FILLER);

        SkywarsDataHandler handler = SkywarsDataHandler.getUser(ctx.player());
        long coins = handler != null ? handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue() : 0;

        String formattedCost = NumberFormat.getNumberInstance(Locale.US).format(tier.cost());
        boolean canAfford = coins >= tier.cost();

        layout.slot(13, (_, __) -> {
            String colorCode = upgrade.color();
            return ItemStackCreator.getStack(
                    "§" + colorCode + upgrade.name() + " " + SoulWellMessages.toRoman(newLevel),
                    upgrade.material(),
                    1,
                    "§8Permanent Upgrade",
                    "",
                    "§7" + upgrade.baseDescription(),
                    "",
                    tier.getEffectChangeLine(),
                    "",
                    "§7Cost: §6" + formattedCost
            );
        });

        layout.slot(11,
                (_, __) -> {
                    if (canAfford) {
                        return ItemStackCreator.getStack(
                                "§aConfirm",
                                Material.LIME_TERRACOTTA,
                                1,
                                "§7Click to purchase §" + upgrade.color() + upgrade.name(),
                                "§7for §6" + formattedCost + " Coins§7."
                        );
                    }
                    return ItemStackCreator.getStack(
                            "§cCannot Afford",
                            Material.GRAY_TERRACOTTA,
                            1,
                            "§7You need §6" + formattedCost + " Coins",
                            "§7to purchase this upgrade.",
                            "",
                            "§cYou don't have enough coins!"
                    );
                },
                (_, c) -> {
                    SkywarsDataHandler dataHandler = SkywarsDataHandler.getUser(c.player());
                    if (dataHandler == null) return;

                    DatapointLong coinsDatapoint = dataHandler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class);
                    long currentCoins = coinsDatapoint.getValue();
                    if (currentCoins < tier.cost()) {
                        c.player().sendMessage("§cYou don't have enough coins to purchase this upgrade!");
                        c.session(Object.class).refresh();
                        return;
                    }

                    coinsDatapoint.setValue(currentCoins - tier.cost());

                    DatapointSoulWellUpgrades upgradesDatapoint = dataHandler.get(
                            SkywarsDataHandler.Data.SOUL_WELL_UPGRADES, DatapointSoulWellUpgrades.class);
                    upgradesDatapoint.getValue().setUpgradeLevel(upgrade.id(), newLevel);

                    SoulWellMessages.sendPurchaseMessage(c.player(), upgrade, tier, newLevel);

                    c.replace(new GUISoulWell());
                }
        );

        layout.slot(15,
                (_, __) -> ItemStackCreator.getStack(
                        "§cCancel",
                        Material.RED_TERRACOTTA,
                        1,
                        "§7Click to go back."
                ),
                (_, c) -> c.replace(new GUISoulWell())
        );
    }
}
