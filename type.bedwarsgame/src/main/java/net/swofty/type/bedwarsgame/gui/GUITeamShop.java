package net.swofty.type.bedwarsgame.gui;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.shop.TeamShopManager;
import net.swofty.type.bedwarsgame.shop.TeamUpgrade;
import net.swofty.type.bedwarsgame.shop.TeamUpgradeTier;
import net.swofty.type.bedwarsgame.shop.Trap;
import net.swofty.type.bedwarsgame.shop.TrapManager;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.util.BedWarsInventoryManipulator;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.swofty.type.bedwarsgame.util.ComponentManipulator.noItalic;

public class GUITeamShop extends StatelessView {

    private static final int[] UPGRADE_SLOTS = {10, 11, 12, 19, 20, 21};
    private static final int[] TRAP_SHOP_SLOTS = {14, 15, 16, 23, 24, 25};
    private static final int[] TRAP_QUEUE_SLOTS = {39, 40, 41};
    private static final int[] SEPARATOR_SLOTS = {27, 28, 29, 30, 31, 32, 33, 34, 35};

    private final TeamShopManager teamShopService = TypeBedWarsGameLoader.getTeamShopManager();
    private final TrapManager trapManager = TypeBedWarsGameLoader.getTrapManager();

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Team Shop", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        for (int slot : SEPARATOR_SLOTS) {
            layout.slot(slot, ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(noItalic(Component.text(" "))));
        }

        BedWarsPlayer player = (BedWarsPlayer) ctx.player();
        BedWarsGame game = player.getGame();
        BedWarsMapsConfig.TeamKey teamKey = player.getTeamKey();
        if (game == null || teamKey == null) {
            for (int slot : UPGRADE_SLOTS) {
                layout.slot(slot, ItemStack.builder(Material.BARRIER)
                    .customName(noItalic(Component.text("No Game/Team").color(NamedTextColor.RED))));
            }
            for (int slot : TRAP_SHOP_SLOTS) {
                layout.slot(slot, ItemStack.builder(Material.BARRIER)
                    .customName(noItalic(Component.text("No Game/Team").color(NamedTextColor.RED))));
            }
            return;
        }

        List<TeamUpgrade> upgrades = teamShopService.getUpgrades();
        List<Trap> traps = trapManager.getTraps();

        for (int i = 0; i < UPGRADE_SLOTS.length; i++) {
            int slot = UPGRADE_SLOTS[i];
            int index = i;
            layout.slot(slot,
                (s, c) -> renderUpgradeItem((BedWarsPlayer) c.player(), upgrades, index),
                (click, c) -> handleUpgradeClick(click, c, upgrades, index)
            );
        }

        for (int i = 0; i < TRAP_SHOP_SLOTS.length; i++) {
            int slot = TRAP_SHOP_SLOTS[i];
            int index = i;
            layout.slot(slot,
                (s, c) -> renderTrapItem((BedWarsPlayer) c.player(), traps, index),
                (click, c) -> handleTrapClick(c, traps, index)
            );
        }

        for (int i = 0; i < TRAP_QUEUE_SLOTS.length; i++) {
            int slot = TRAP_QUEUE_SLOTS[i];
            int index = i;
            layout.slot(slot, (s, c) -> renderTrapQueueItem((BedWarsPlayer) c.player(), traps, index));
        }
    }

    @Override
    public boolean onBottomClick(ClickContext<DefaultState> click, ViewContext ctx) {
        return false;
    }

    private void handleUpgradeClick(ClickContext<DefaultState> click, ViewContext ctx, List<TeamUpgrade> upgrades, int index) {
        if (index >= upgrades.size()) return;

        BedWarsPlayer player = (BedWarsPlayer) click.player();
        BedWarsGame game = player.getGame();
        BedWarsMapsConfig.TeamKey teamKey = player.getTeamKey();
        if (game == null || teamKey == null) return;

        TeamUpgrade upgrade = upgrades.get(index);
        TeamUpgradeTier nextTier = upgrade.getNextTier(game, teamKey);
        if (nextTier == null) {
            player.sendMessage("§cYour team has already bought this upgrade!");
            playClickSound(player);
            return;
        }
        if (!upgrade.hasEnoughCurrency(player, nextTier)) {
            player.sendMessage("§cYou don't have enough " + nextTier.getCurrency().getName() + "!");
            playClickSound(player);
            return;
        }

        upgrade.purchase(game, player);
        playBuySound(player);
        ctx.session(DefaultState.class).refresh();
    }

    private ItemStack.Builder renderUpgradeItem(BedWarsPlayer player, List<TeamUpgrade> upgrades, int index) {
        if (index >= upgrades.size()) return ItemStack.builder(Material.AIR);

        BedWarsGame game = player.getGame();
        BedWarsMapsConfig.TeamKey teamKey = player.getTeamKey();
        if (game == null || teamKey == null) {
            return ItemStack.builder(Material.BARRIER)
                .customName(noItalic(Component.text("No Game").color(NamedTextColor.RED)));
        }

        TeamUpgrade upgrade = upgrades.get(index);
        TeamUpgradeTier nextTier = upgrade.getNextTier(game, teamKey);
        boolean isMaxed = nextTier == null;
        boolean canAfford = !isMaxed && upgrade.hasEnoughCurrency(player, nextTier);

        List<Component> lore = new ArrayList<>();
        lore.add(noItalic(Component.text(upgrade.getDescription()).color(NamedTextColor.GRAY)));
        lore.add(Component.empty().decoration(ITALIC, TextDecoration.State.FALSE));

        int currentLevel = upgrade.getCurrentLevel(game, teamKey);
        for (TeamUpgradeTier tier : upgrade.getTiers()) {
            boolean owned = tier.getLevel() <= currentLevel;
            boolean next = !owned && !isMaxed && tier.getLevel() == nextTier.getLevel();
            TextColor color = owned ? NamedTextColor.GREEN : (next ? NamedTextColor.YELLOW : NamedTextColor.GRAY);
            lore.add(noItalic(Component.text("Tier " + tier.getLevel() + ": " + tier.getDescription() + ", ")
                .color(color)
                .append(Component.text(tier.getPrice() + " " + tier.getCurrency().getName() + (tier.getPrice() != 1 ? "s" : ""))
                    .color(NamedTextColor.AQUA))));
        }

        lore.add(Component.empty().decoration(ITALIC, TextDecoration.State.FALSE));
        if (isMaxed) {
            lore.add(noItalic(Component.text("UNLOCKED").color(NamedTextColor.GREEN)));
        } else if (canAfford) {
            lore.add(noItalic(Component.text("Click to purchase!").color(NamedTextColor.YELLOW)));
        } else {
            lore.add(noItalic(Component.text("You don't have enough " + nextTier.getCurrency().getName() + "!").color(NamedTextColor.RED)));
        }

        return upgrade.getDisplayItem().builder()
            .customName(noItalic(Component.text(upgrade.getName())
                .color(isMaxed || canAfford ? NamedTextColor.GREEN : NamedTextColor.RED)))
            .lore(lore);
    }

    private void handleTrapClick(ViewContext ctx, List<Trap> traps, int index) {
        if (index >= traps.size()) return;

        BedWarsPlayer player = (BedWarsPlayer) ctx.player();
        BedWarsGame game = player.getGame();
        BedWarsMapsConfig.TeamKey teamKey = player.getTeamKey();
        if (game == null || teamKey == null) return;

        int trapSize = game.getTeamTraps(teamKey).size();
        if (trapSize >= 3) {
            player.sendMessage("§cYou can't have more traps than 3");
            playClickSound(player);
        }

        Trap trap = traps.get(index);
        int price = trap.getPrice(game, teamKey);
        int owned = Arrays.stream(player.getInventory().getItemStacks())
            .filter(s -> s.material() == trap.getCurrency().getMaterial())
            .mapToInt(ItemStack::amount)
            .sum();
        if (owned < price) {
            player.sendMessage("§cYou don't have enough " + trap.getCurrency().getName() + "!");
            playClickSound(player);
            return;
        }

        BedWarsInventoryManipulator.removeItems(player, trap.getCurrency().getMaterial(), price);
        game.addTeamTrap(teamKey, trap.getKey());
        broadcastTeamPurchase(game, teamKey, player, trap.getName());
        playBuySound(player);
        ctx.session(DefaultState.class).refresh();

        if (trapSize == 2) {
            for (BedWarsPlayer teamPlayer : game.getPlayersOnTeam(player.getTeamKey())) {
                teamPlayer.getAchievementHandler().completeAchievement("bedwars.minefield");
            }
        }
    }

    private ItemStack.Builder renderTrapItem(BedWarsPlayer player, List<Trap> traps, int index) {
        if (index >= traps.size()) return ItemStack.builder(Material.AIR);

        BedWarsMapsConfig.TeamKey teamKey = player.getTeamKey();
        BedWarsGame game = player.getGame();
        if (game == null || teamKey == null) {
            return ItemStack.builder(Material.BARRIER)
                .customName(noItalic(Component.text("No Game").color(NamedTextColor.RED)));
        }

        Trap trap = traps.get(index);
        int price = trap.getPrice(game, teamKey);
        int owned = Arrays.stream(player.getInventory().getItemStacks())
            .filter(s -> s.material() == trap.getCurrency().getMaterial())
            .mapToInt(ItemStack::amount)
            .sum();
        boolean canAfford = owned >= price;

        return trap.getDisplayItem().builder()
            .customName(noItalic(Component.text(trap.getName())
                .color(canAfford ? NamedTextColor.GREEN : NamedTextColor.RED)))
            .lore(List.of(
                noItalic(Component.text(trap.getDescription()).color(NamedTextColor.GRAY)),
                Component.empty().decoration(ITALIC, TextDecoration.State.FALSE),
                noItalic(Component.text("Cost: ").color(NamedTextColor.GRAY)
                    .append(Component.text(price + " " + trap.getCurrency().getName() + (price != 1 ? "s" : ""))
                        .color(NamedTextColor.AQUA))),
                Component.empty().decoration(ITALIC, TextDecoration.State.FALSE),
                canAfford
                    ? noItalic(Component.text("Click to purchase!").color(NamedTextColor.YELLOW))
                    : noItalic(Component.text("You don't have enough " + trap.getCurrency().getName() + "!").color(NamedTextColor.RED))
            ));
    }

    private ItemStack.Builder renderTrapQueueItem(BedWarsPlayer player, List<Trap> traps, int index) {
        BedWarsGame game = player.getGame();
        BedWarsMapsConfig.TeamKey teamKey = player.getTeamKey();
        if (game == null || teamKey == null) {
            return ItemStack.builder(Material.BARRIER)
                .customName(noItalic(Component.text("No Game").color(NamedTextColor.RED)));
        }

        List<String> queued = game.getTeamTraps(teamKey);
        if (index < queued.size()) {
            Trap trap = traps.stream()
                .filter(tr -> tr.getKey().equals(queued.get(index)))
                .findFirst()
                .orElse(null);
            if (trap != null) {
                return ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                    .customName(noItalic(Component.text("Trap #" + (index + 1) + ": " + trap.getName())
                        .color(NamedTextColor.AQUA)))
                    .amount(index + 1);
            }
        }

        return ItemStack.builder(Material.GRAY_STAINED_GLASS)
            .customName(noItalic(Component.text("Trap #" + (index + 1) + ": No Trap")
                .color(NamedTextColor.RED)))
            .lore(List.of(
                noItalic(Component.text("The first enemy to walk into your").color(NamedTextColor.GRAY)),
                noItalic(Component.text("base will trigger this trap!").color(NamedTextColor.GRAY)),
                Component.empty().decoration(ITALIC, TextDecoration.State.FALSE),
                noItalic(Component.text("Purchasing a trap will queue it here.").color(NamedTextColor.GRAY)),
                noItalic(Component.text("Its cost scales with traps queued.").color(NamedTextColor.GRAY))
            ))
            .amount(index + 1);
    }

    private void broadcastTeamPurchase(BedWarsGame game, BedWarsMapsConfig.TeamKey teamName, BedWarsPlayer buyer, String name) {
        for (BedWarsPlayer pl : game.getPlayers()) {
            if (teamName.equals(pl.getTeamKey())) {
                pl.sendMessage(buyer.getTeamKey().chatColor() + " §apurchased §6" + name + "!");
            }
        }
    }

    private void playClickSound(HypixelPlayer player) {
        player.playSound(Sound.sound(Key.key("minecraft:ui.button.click"), Sound.Source.MASTER, 1.0f, 1.0f));
    }

    private void playBuySound(HypixelPlayer player) {
        player.playSound(Sound.sound(Key.key("minecraft:entity.experience_orb.pickup"), Sound.Source.MASTER, 1.0f, 1.0f));
    }
}
