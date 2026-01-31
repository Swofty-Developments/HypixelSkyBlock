package net.swofty.type.bedwarsgame.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.events.custom.BedWarsAdminActionEvent;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.game.v2.BedWarsTeam;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.user.categories.Rank;

/**
 * Admin commands for BedWars game management.
 */
@CommandParameters(aliases = "bwadmin",
        description = "Admin commands for BedWars game management.",
        usage = "/bwadmin <breakbed|respawnbed|endgame|info> [team]",
        permission = Rank.STAFF,
        allowsConsole = false)
public class BedWarsAdminCommand extends HypixelCommand {

    @Override
    public void registerUsage(HypixelCommand.MinestomCommand command) {
        // /bwadmin breakbed <team>
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof BedWarsPlayer player)) return;

            String teamName = context.get("team");
            BedWarsGame game = player.getGame();

            if (game == null) {
                player.sendMessage(Component.text("You are not in a game!", NamedTextColor.RED));
                return;
            }

            TeamKey teamKey;
            try {
                teamKey = TeamKey.valueOf(teamName.toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(Component.text("Invalid team: " + teamName, NamedTextColor.RED));
                return;
            }

            if (!game.isBedAlive(teamKey)) {
                player.sendMessage(Component.text("That team's bed is already destroyed!", NamedTextColor.RED));
                return;
            }

            HypixelEventHandler.callCustomEvent(new BedWarsAdminActionEvent(
                    game, player.getUsername(), BedWarsAdminActionEvent.ActionType.DESTROY_BED, teamKey
            ));

            game.onBedDestroyed(teamKey, null);
            game.broadcastAdminMessage(player.getUsername() + " destroyed " + teamKey.getName() + "'s bed");

        }, ArgumentType.Literal("breakbed"), ArgumentType.String("team"));

        // /bwadmin respawnbed <team>
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof BedWarsPlayer player)) return;

            String teamName = context.get("team");
            BedWarsGame game = player.getGame();

            if (game == null) {
                player.sendMessage(Component.text("You are not in a game!", NamedTextColor.RED));
                return;
            }

            TeamKey teamKey;
            try {
                teamKey = TeamKey.valueOf(teamName.toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(Component.text("Invalid team: " + teamName, NamedTextColor.RED));
                return;
            }

            if (game.isBedAlive(teamKey)) {
                player.sendMessage(Component.text("That team's bed is already alive!", NamedTextColor.RED));
                return;
            }

            HypixelEventHandler.callCustomEvent(new BedWarsAdminActionEvent(
                    game, player.getUsername(), BedWarsAdminActionEvent.ActionType.RESPAWN_BED, teamKey
            ));

            game.respawnBed(teamKey);
            game.broadcastAdminMessage(player.getUsername() + " respawned " + teamKey.getName() + "'s bed");

        }, ArgumentType.Literal("respawnbed"), ArgumentType.String("team"));

        // /bwadmin endgame [winner]
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof BedWarsPlayer player)) return;

            BedWarsGame game = player.getGame();

            if (game == null) {
                player.sendMessage(Component.text("You are not in a game!", NamedTextColor.RED));
                return;
            }

            String winnerTeam = context.get("winner");
            TeamKey winnerKey = null;

            if (winnerTeam != null && !winnerTeam.isEmpty()) {
                try {
                    winnerKey = TeamKey.valueOf(winnerTeam.toUpperCase());
                } catch (IllegalArgumentException e) {
                    player.sendMessage(Component.text("Invalid team: " + winnerTeam, NamedTextColor.RED));
                    return;
                }
            }

            HypixelEventHandler.callCustomEvent(new BedWarsAdminActionEvent(
                    game, player.getUsername(), BedWarsAdminActionEvent.ActionType.FORCE_END_GAME, winnerKey
            ));

            game.broadcastAdminMessage(player.getUsername() + " ended the game");

            if (winnerKey != null) {
                BedWarsTeam team = game.getTeam(winnerKey.name()).orElse(null);
                game.handleGameWin(team);
            } else {
                game.handleGameWin(null);
            }

        }, ArgumentType.Literal("endgame"), ArgumentType.String("winner").setDefaultValue(""));

        // /bwadmin endgame (no args version)
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof BedWarsPlayer player)) return;

            BedWarsGame game = player.getGame();

            if (game == null) {
                player.sendMessage(Component.text("You are not in a game!", NamedTextColor.RED));
                return;
            }

            HypixelEventHandler.callCustomEvent(new BedWarsAdminActionEvent(
                    game, player.getUsername(), BedWarsAdminActionEvent.ActionType.FORCE_END_GAME, null
            ));

            game.broadcastAdminMessage(player.getUsername() + " ended the game (no winner)");
            game.handleGameWin(null);

        }, ArgumentType.Literal("endgame"));

        // /bwadmin info
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof BedWarsPlayer player)) return;

            BedWarsGame game = player.getGame();

            if (game == null) {
                player.sendMessage(Component.text("You are not in a game!", NamedTextColor.RED));
                return;
            }

            player.sendMessage(Component.text("=== Game Info ===", NamedTextColor.GOLD));
            player.sendMessage(Component.text("Game ID: " + game.getGameId(), NamedTextColor.YELLOW));
            player.sendMessage(Component.text("State: " + game.getState(), NamedTextColor.YELLOW));
            player.sendMessage(Component.text("Map: " + game.getMapEntry().getName(), NamedTextColor.YELLOW));
            player.sendMessage(Component.text("Players: " + game.getPlayers().size() + "/" + game.getMaxPlayers(), NamedTextColor.YELLOW));
            player.sendMessage(Component.text("Recording: " + game.getReplayManager().isRecording(), NamedTextColor.YELLOW));

            player.sendMessage(Component.text("=== Teams ===", NamedTextColor.GOLD));
            for (BedWarsTeam team : game.getTeams()) {
                String bedStatus = team.isBedAlive() ? "§a✔" : "§c✖";
                player.sendMessage(Component.text(team.getColorCode() + team.getName() + " " + bedStatus +
                        " §7(" + team.getPlayerCount() + " players)", NamedTextColor.WHITE));
            }

        }, ArgumentType.Literal("info"));
    }
}
