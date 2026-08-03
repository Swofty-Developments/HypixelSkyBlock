package net.swofty.type.ravengardgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.objects.orchestrator.ChooseGameProtocol;
import net.swofty.commons.protocol.objects.orchestrator.GetServerForMapProtocol;
import net.swofty.commons.protocol.objects.orchestrator.ListGamesProtocol;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

import java.util.concurrent.ThreadLocalRandom;

@CommandParameters(labels = "dungeon",
        description = "Ravengard dungeon queueing and administration",
        usage = "/dungeon <join|list|admin generate [seed] [rooms]>",
        permission = Rank.DEFAULT, allowsConsole = false)
public class DungeonCommand extends HypixelCommand {
    private static final ProxyService ORCHESTRATOR = new ProxyService(ServiceType.ORCHESTRATOR);
    private static final int DEFAULT_ADMIN_ROOMS = 24;

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            RavengardPlayer player = (RavengardPlayer) sender;
            player.sendMessage("§e/dungeon join §7- queue into a dungeon");
            if (player.getRank().isStaff()) {
                player.sendMessage("§e/dungeon list §7- every dungeon server and its instances");
                player.sendMessage("§e/dungeon admin generate [seed] [rooms] §7- dedicated instance");
                player.sendMessage("§e/dungeon admin join <instance> §7- fly over an instance");
            }
        });

        command.addSyntax((sender, context) -> {
            queue((RavengardPlayer) sender, "STANDARD");
        }, ArgumentType.Literal("join"));

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender, Rank.STAFF)) return;
            list((RavengardPlayer) sender);
        }, ArgumentType.Literal("list"));

        ArgumentNumber<Long> seedArg = ArgumentType.Long("seed");
        ArgumentNumber<Integer> roomsArg = ArgumentType.Integer("rooms").min(3).max(120);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender, Rank.STAFF)) return;
            adminGenerate((RavengardPlayer) sender,
                    "ADMIN:" + ThreadLocalRandom.current().nextLong() + ":" + DEFAULT_ADMIN_ROOMS);
        }, ArgumentType.Literal("admin"), ArgumentType.Literal("generate"));

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender, Rank.STAFF)) return;
            adminGenerate((RavengardPlayer) sender,
                    "ADMIN:" + context.get(seedArg) + ":" + DEFAULT_ADMIN_ROOMS);
        }, ArgumentType.Literal("admin"), ArgumentType.Literal("generate"), seedArg);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender, Rank.STAFF)) return;
            adminGenerate((RavengardPlayer) sender,
                    "ADMIN:" + context.get(seedArg) + ":" + context.get(roomsArg));
        }, ArgumentType.Literal("admin"), ArgumentType.Literal("generate"), seedArg, roomsArg);

        var instanceArg = ArgumentType.Word("instance");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender, Rank.STAFF)) return;
            adminJoin((RavengardPlayer) sender, context.get(instanceArg));
        }, ArgumentType.Literal("admin"), ArgumentType.Literal("join"), instanceArg);
    }

    private static void adminGenerate(RavengardPlayer player, String mode) {
        player.sendMessage("§7Allocating a dungeon instance...");
        GetServerForMapProtocol.GetServerForMapMessage request =
                new GetServerForMapProtocol.GetServerForMapMessage(
                        ServerType.RAVENGARD_DUNGEON, null, mode, 1);
        ORCHESTRATOR.handleRequest(request).thenAccept(response -> {
            if (!(response instanceof GetServerForMapProtocol.GetServerForMapResponse(
                    UnderstandableProxyServer server, String gameId, boolean success, String error))
                    || server == null || gameId == null) {
                String reason = response instanceof GetServerForMapProtocol.GetServerForMapResponse r
                        && r.error() != null ? r.error() : "no servers available";
                player.sendMessage("§cCould not allocate an instance: " + reason);
                return;
            }
            player.sendMessage("§aInstance §f" + gameId + "§a created on §f" + server.shortName()
                    + "§a. It self-destructs after 30s with nobody inside.");
            player.sendMessage(net.kyori.adventure.text.Component
                    .text("§e§l[CLICK TO JOIN] §7or run /dungeon admin join " + gameId.substring(0, 8))
                    .clickEvent(net.kyori.adventure.text.event.ClickEvent
                            .runCommand("/dungeon admin join " + gameId)));
        }).exceptionally(throwable -> {
            player.sendMessage("§cAllocation failed: " + throwable.getMessage());
            return null;
        });
    }

    private static void adminJoin(RavengardPlayer player, String instanceId) {
        ORCHESTRATOR.handleRequest(new ListGamesProtocol.ListGamesMessage(ServerType.RAVENGARD_DUNGEON))
                .thenAccept(response -> {
                    if (!(response instanceof ListGamesProtocol.ListGamesResponse listing)
                            || !listing.success()) {
                        player.sendMessage("§cFailed to look the instance up.");
                        return;
                    }
                    for (ListGamesProtocol.ServerGames server : listing.servers()) {
                        for (ListGamesProtocol.GameSummary game : server.games()) {
                            if (!game.gameId().equals(instanceId)
                                    && !game.gameId().startsWith(instanceId)) continue;
                            UnderstandableProxyServer proxy = new UnderstandableProxyServer(
                                    server.shortName(),
                                    java.util.UUID.fromString(server.serverUuid()),
                                    ServerType.RAVENGARD_DUNGEON, -1,
                                    new java.util.ArrayList<>(), server.maxPlayers(),
                                    server.shortName());
                            ORCHESTRATOR.handleRequest(new ChooseGameProtocol.ChooseGameMessage(
                                    player.getUuid(), proxy, game.gameId())).thenRun(() -> {
                                player.sendMessage("§aSending you to §f" + server.shortName() + "§a!");
                                player.asProxyPlayer().transferToWithIndication(proxy.uuid());
                            });
                            return;
                        }
                    }
                    player.sendMessage("§cNo instance found matching §f" + instanceId + "§c.");
                }).exceptionally(throwable -> {
                    player.sendMessage("§cLookup failed: " + throwable.getMessage());
                    return null;
                });
    }

    private boolean permissionCheck(net.minestom.server.command.CommandSender sender, Rank rank) {
        RavengardPlayer player = (RavengardPlayer) sender;
        if (!player.getRank().isEqualOrHigherThan(rank)) {
            player.sendMessage("§cYou do not have permission to use this command.");
            return false;
        }
        return true;
    }

    private static void queue(RavengardPlayer player, String mode) {
        player.sendMessage("§7Finding a dungeon server...");
        GetServerForMapProtocol.GetServerForMapMessage request =
                new GetServerForMapProtocol.GetServerForMapMessage(
                        ServerType.RAVENGARD_DUNGEON, null, mode, 1);

        ORCHESTRATOR.handleRequest(request).thenAccept(response -> {
            if (!(response instanceof GetServerForMapProtocol.GetServerForMapResponse(
                    UnderstandableProxyServer server, String gameId, boolean success, String error))
                    || server == null || gameId == null) {
                String reason = response instanceof GetServerForMapProtocol.GetServerForMapResponse r
                        && r.error() != null ? r.error() : "no servers available";
                player.sendMessage("§cCould not find a dungeon: " + reason);
                return;
            }

            ORCHESTRATOR.handleRequest(new ChooseGameProtocol.ChooseGameMessage(
                    player.getUuid(), server, gameId)).thenRun(() -> {
                player.sendMessage("§aSending you to §f" + server.shortName() + "§a!");
                player.asProxyPlayer().transferToWithIndication(server.uuid());
            }).exceptionally(throwable -> {
                player.sendMessage("§cFailed to register for the dungeon: " + throwable.getMessage());
                return null;
            });
        }).exceptionally(throwable -> {
            player.sendMessage("§cDungeon search failed: " + throwable.getMessage());
            return null;
        });
    }

    private static void list(RavengardPlayer player) {
        ORCHESTRATOR.handleRequest(new ListGamesProtocol.ListGamesMessage(ServerType.RAVENGARD_DUNGEON))
                .thenAccept(response -> {
                    if (!(response instanceof ListGamesProtocol.ListGamesResponse listing)
                            || !listing.success()) {
                        player.sendMessage("§cFailed to list dungeon servers.");
                        return;
                    }
                    if (listing.servers().isEmpty()) {
                        player.sendMessage("§cNo dungeon servers are online.");
                        return;
                    }
                    for (ListGamesProtocol.ServerGames server : listing.servers()) {
                        player.sendMessage("§e" + server.shortName() + " §7- §f"
                                + server.onlinePlayers() + "§7/§f" + server.maxPlayers()
                                + " players§7, §f" + (server.remainingGameSlots() == null
                                ? "?" : server.remainingGameSlots()) + "§7 free instance slots");
                        for (ListGamesProtocol.GameSummary game : server.games()) {
                            String expiry = game.map() != null && game.map().startsWith("empty:")
                                    ? " §c(dies in " + game.map().substring(6) + "s)" : "";
                            player.sendMessage("  §8- §f" + game.gameId().substring(0, 8)
                                    + " §7" + game.gameTypeName()
                                    + " §f" + game.playerCount() + " players "
                                    + (game.acceptingJoins() ? "§aopen" : "§cclosed") + expiry);
                        }
                        if (server.games().isEmpty()) {
                            player.sendMessage("  §8- §7no instances");
                        }
                    }
                }).exceptionally(throwable -> {
                    player.sendMessage("§cListing failed: " + throwable.getMessage());
                    return null;
                });
    }
}
