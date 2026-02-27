package net.swofty.type.bedwarslobby.commands;

import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.orchestrator.ChooseGameProtocolObject;
import net.swofty.commons.protocol.objects.orchestrator.RejoinGameProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(
        description = "Rejoin an active BedWars game",
        usage = "/rejoin",
        permission = Rank.DEFAULT,
        allowsConsole = false
)
public class RejoinCommand extends HypixelCommand {
    private static final ProxyService ORCHESTRATOR = new ProxyService(ServiceType.ORCHESTRATOR);

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;

            // Query orchestrator for active game
            RejoinGameProtocolObject.RejoinGameRequest request =
                    new RejoinGameProtocolObject.RejoinGameRequest(player.getUuid());

            ORCHESTRATOR.handleRequest(request).thenAccept(response -> {
                if (response instanceof RejoinGameProtocolObject.RejoinGameResponse resp) {
                    if (resp.hasActiveGame() && resp.server() != null) {
                        // Send the player to the game
                        player.sendMessage("§aRejoining your game...");

                        // Notify the game server about this player
                        ChooseGameProtocolObject.ChooseGameMessage chooseMsg =
                                new ChooseGameProtocolObject.ChooseGameMessage(
                                        player.getUuid(),
                                        resp.server(),
                                        resp.gameId()
                                );
                        ORCHESTRATOR.handleRequest(chooseMsg);

                        // Transfer player to the game server
                        player.asProxyPlayer().transferToWithIndication(resp.server().uuid());
                        player.getAchievementHandler().completeAchievement("bedwars.rejoining_the_dream");
                    } else {
                        player.sendMessage("§cYou don't have an active game to rejoin!");
                    }
                } else {
                    player.sendMessage("§cFailed to check for active games. Please try again.");
                }
            }).exceptionally(throwable -> {
                player.sendMessage("§cFailed to rejoin: " + throwable.getMessage());
                return null;
            });
        });
    }
}
