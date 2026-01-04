package net.swofty.type.skywarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
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

import java.text.NumberFormat;
import java.util.Locale;

public class GUISoulWellConfirm extends HypixelInventoryGUI {
    private final SoulWellUpgrade upgrade;
    private final SoulWellUpgrade.SoulWellUpgradeTier tier;
    private final int newLevel;

    public GUISoulWellConfirm(SoulWellUpgrade upgrade, SoulWellUpgrade.SoulWellUpgradeTier tier, int newLevel) {
        super("Are you sure?", InventoryType.CHEST_3_ROW);
        this.upgrade = upgrade;
        this.tier = tier;
        this.newLevel = newLevel;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " "));

        HypixelPlayer player = e.player();
        SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
        long coins = handler != null ? handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue() : 0;

        String formattedCost = NumberFormat.getNumberInstance(Locale.US).format(tier.cost());
        boolean canAfford = coins >= tier.cost();

        // Display item in the middle (slot 13)
        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
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
            }
        });

        // Confirm button (slot 11)
        set(new GUIClickableItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                if (canAfford) {
                    return ItemStackCreator.getStack(
                            "§aConfirm",
                            Material.LIME_TERRACOTTA,
                            1,
                            "§7Click to purchase §" + upgrade.color() + upgrade.name(),
                            "§7for §6" + formattedCost + " Coins§7."
                    );
                } else {
                    return ItemStackCreator.getStack(
                            "§cCannot Afford",
                            Material.GRAY_TERRACOTTA,
                            1,
                            "§7You need §6" + formattedCost + " Coins",
                            "§7to purchase this upgrade.",
                            "",
                            "§cYou don't have enough coins!"
                    );
                }
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (!canAfford) {
                    player.sendMessage("§cYou don't have enough coins to purchase this upgrade!");
                    return;
                }

                SkywarsDataHandler dataHandler = SkywarsDataHandler.getUser(player);
                if (dataHandler == null) return;

                // Deduct coins
                DatapointLong coinsDatapoint = dataHandler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class);
                coinsDatapoint.setValue(coinsDatapoint.getValue() - tier.cost());

                // Upgrade the player's upgrade level
                DatapointSoulWellUpgrades upgradesDatapoint = dataHandler.get(
                        SkywarsDataHandler.Data.SOUL_WELL_UPGRADES, DatapointSoulWellUpgrades.class);
                upgradesDatapoint.getValue().setUpgradeLevel(upgrade.id(), newLevel);

                // Send centered purchase message
                SoulWellMessages.sendPurchaseMessage(player, upgrade, tier, newLevel);

                // Return to Soul Well GUI
                new GUISoulWell().open(player);
            }
        });

        // Cancel button (slot 15)
        set(new GUIClickableItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§cCancel",
                        Material.RED_TERRACOTTA,
                        1,
                        "§7Click to go back."
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUISoulWell().open(player);
            }
        });

        updateItemStacks(e.inventory(), getPlayer());
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
