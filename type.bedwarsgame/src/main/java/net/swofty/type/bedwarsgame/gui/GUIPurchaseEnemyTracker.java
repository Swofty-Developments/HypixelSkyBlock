package net.swofty.type.bedwarsgame.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.game.v2.BedWarsTeam;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.util.BedWarsInventoryManipulator;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GUIPurchaseEnemyTracker extends StatelessView {

    private static final int TRACKER_PRICE = 2;
    private static final int[] TEAM_SLOTS = {10, 11, 12, 13, 14, 15, 16, 19};
    // TODO: reset between games, move to bwgame
    public static final Tag<String> TRACKED_TEAM_TAG = Tag.String("bedwars_compass_tracked_team");

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Purchase Enemy Tracker", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.back(layout, 31, ctx);

        if (!(ctx.player() instanceof BedWarsPlayer player)) {
            return;
        }

        BedWarsGame game = player.getGame();
        TeamKey ownTeam = GUIQuickCommunications.resolveTeamKey(player);
        if (game == null || ownTeam == null) {
            layout.slot(13, ItemStackCreator.getStack(
                "§cUnavailable",
                Material.BARRIER,
                1,
                "§7You must be in an active game",
                "§7to purchase an enemy tracker."
            ));
            return;
        }

        List<TeamKey> enemyTeams = getEnemyTeams(game, ownTeam);
        if (enemyTeams.isEmpty()) {
            layout.slot(13, ItemStackCreator.getStack(
                "§aNo enemies left",
                Material.LIME_STAINED_GLASS_PANE,
                1,
                "§7There are no enemy teams",
                "§7left to track."
            ));
            return;
        }

        boolean trackersUnlocked = enemyTeams.stream().noneMatch(game::isBedAlive);
        int optionCount = Math.min(enemyTeams.size(), TEAM_SLOTS.length);
        for (int i = 0; i < optionCount; i++) {
            TeamKey targetTeam = enemyTeams.get(i);
            int slot = TEAM_SLOTS[i];

            layout.slot(slot,
                (s, c) -> buildTrackerItem(player, targetTeam, trackersUnlocked),
                (click, context) -> {
                    if (!(click.player() instanceof BedWarsPlayer clickPlayer)) {
                        return;
                    }

                    handleTrackerPurchase(clickPlayer, game, targetTeam, trackersUnlocked, context);
                }
            );
        }
    }

    private ItemStack.Builder buildTrackerItem(BedWarsPlayer player, TeamKey targetTeam, boolean trackersUnlocked) {
        boolean trackingThisTeam = isTrackingTeam(player, targetTeam);
        boolean canAfford = BedWarsInventoryManipulator.hasEnoughMaterial(player, Material.EMERALD, TRACKER_PRICE);

        String nameColor = trackersUnlocked && (canAfford || trackingThisTeam) ? "§a" : "§c";
        String teamName = targetTeam.chatColor() + targetTeam.getName();

        List<String> lore = new ArrayList<>();
        lore.add("§7Purchase tracking upgrade for your");
        lore.add("§7compass which will track each player");
        lore.add("§7on a specific team until you die.");
        lore.add("");
        lore.add("§7Cost: §2" + TRACKER_PRICE + " Emeralds");
        lore.add("");

        if (!trackersUnlocked) {
            lore.add("§cUnlocks when all enemy beds are destroyed!");
        } else if (trackingThisTeam) {
            lore.add("§aYou are already tracking this team!");
        } else if (canAfford) {
            lore.add("§eClick to purchase!");
        } else {
            lore.add("§cYou don't have enough Emeralds!");
        }

        return ItemStackCreator.getStack(
            nameColor + "Track Team " + teamName,
            getWool(targetTeam),
            1,
            lore
        );
    }

    private void handleTrackerPurchase(BedWarsPlayer player,
                                       BedWarsGame game,
                                       TeamKey targetTeam,
                                       boolean trackersUnlocked,
                                       ViewContext ctx) {
        if (!trackersUnlocked) {
            player.sendMessage("§cUnlocks when all enemy beds are destroyed!");
            GUIQuickCommunications.playClickSound(player);
            return;
        }

        if (isTrackingTeam(player, targetTeam)) {
            player.sendMessage("§cYou are already tracking this team!");
            GUIQuickCommunications.playClickSound(player);
            return;
        }

        if (!BedWarsInventoryManipulator.hasEnoughMaterial(player, Material.EMERALD, TRACKER_PRICE)) {
            player.sendMessage("§cYou don't have enough Emeralds!");
            GUIQuickCommunications.playClickSound(player);
            return;
        }

        BedWarsInventoryManipulator.removeItems(player, Material.EMERALD, TRACKER_PRICE);
        player.setTag(TRACKED_TEAM_TAG, targetTeam.name());
        player.sendMessage("§aYour compass is now tracking " + targetTeam.chatColor() + "Team " + targetTeam.getName() + "§a!");

        // TODO: implement
        GUIQuickCommunications.playBuySound(player);
        ctx.session(DefaultState.class).refresh();
    }

    private boolean isTrackingTeam(BedWarsPlayer player, TeamKey teamKey) {
        String trackedTeam = player.getTag(TRACKED_TEAM_TAG);
        return trackedTeam != null && trackedTeam.equals(teamKey.name());
    }

    private List<TeamKey> getEnemyTeams(BedWarsGame game, TeamKey ownTeam) {
        return game.getTeams().stream()
            .filter(BedWarsTeam::hasPlayers)
            .map(BedWarsTeam::getTeamKey)
            .filter(team -> team != ownTeam)
            .sorted(Comparator.comparingInt(Enum::ordinal))
            .toList();
    }

    @Deprecated
    private Material getWool(TeamKey teamKey) {
        return switch (teamKey) {
            case RED -> Material.RED_WOOL;
            case BLUE -> Material.BLUE_WOOL;
            case GREEN -> Material.GREEN_WOOL;
            case YELLOW -> Material.YELLOW_WOOL;
            case AQUA -> Material.LIGHT_BLUE_WOOL;
            case WHITE -> Material.WHITE_WOOL;
            case PINK -> Material.PINK_WOOL;
            case GRAY -> Material.GRAY_WOOL;
        };
    }
}
