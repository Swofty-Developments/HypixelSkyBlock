package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.murdermystery.MurderMysteryGameType;
import net.swofty.commons.party.FullParty;
import net.swofty.commons.protocol.objects.orchestrator.ChooseGameProtocolObject;
import net.swofty.commons.protocol.objects.orchestrator.GetServerForMapProtocolObject;
import net.swofty.commons.skywars.SkywarsGameType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.party.PartyManager;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CommandParameters(aliases = "",
        description = "Queue for a game",
        usage = "/play <gamemode>",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class PlayCommand extends HypixelCommand {
    private static final ProxyService PROXY_SERVICE = new ProxyService(ServiceType.ORCHESTRATOR);
    private static final List<UUID> PLAYERS_SEARCHING = new ArrayList<>();

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString gamemodeArg = ArgumentType.String("gamemode");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            String gamemode = context.get(gamemodeArg).toLowerCase();

            if (gamemode.equals("sb")) {
                handleSkyblockQueue(player);
            } else if (gamemode.startsWith("bedwars_")) {
                handleBedwarsQueue(player, gamemode);
            } else if (gamemode.startsWith("murder_")) {
                handleMurderMysteryQueue(player, gamemode);
            } else if (gamemode.startsWith("solo_") || gamemode.startsWith("teams_") || gamemode.equals("ranked_normal")) {
                handleSkywarsQueue(player, gamemode);
            } else {
                player.sendMessage("§cUnknown game mode: " + gamemode);
            }
        }, gamemodeArg);

        command.setDefaultExecutor((sender, context) -> {
            sender.sendMessage("§cUsage: /play <gamemode>");
        });
    }

    private void handleBedwarsQueue(HypixelPlayer player, String gamemode) {
        if (PLAYERS_SEARCHING.contains(player.getUuid())) {
            player.sendMessage("§cYou are already searching for a game!");
            return;
        }

        if (PartyManager.isInParty(player)) {
            FullParty party = PartyManager.getPartyFromPlayer(player);
            if (party != null && !party.getLeader().getUuid().equals(player.getUuid())) {
                player.sendMessage("§cYou are in a party! Ask your leader to start the game, or /p leave");
                return;
            }
        }

        BedwarsGameType gameType = parseBedwarsGameType(gamemode);
        if (gameType == null) {
            player.sendMessage("§cUnknown Bed Wars game mode: " + gamemode);
            return;
        }

        PLAYERS_SEARCHING.add(player.getUuid());

        GetServerForMapProtocolObject.GetServerForMapMessage message =
                new GetServerForMapProtocolObject.GetServerForMapMessage(
                        ServerType.BEDWARS_GAME,
                        null,
                        gameType.toString(),
                        1
                );

        PROXY_SERVICE.handleRequest(message).thenAccept(response -> {
            PLAYERS_SEARCHING.remove(player.getUuid());

            if (response instanceof GetServerForMapProtocolObject.GetServerForMapResponse resp) {
                if (resp.server() != null && resp.gameId() != null) {
                    ChooseGameProtocolObject.ChooseGameMessage chooseMessage =
                            new ChooseGameProtocolObject.ChooseGameMessage(
                                    player.getUuid(),
                                    resp.server(),
                                    resp.gameId()
                            );

                    PROXY_SERVICE.handleRequest(chooseMessage)
                            .exceptionally(throwable -> {
                                player.sendMessage("§cFailed to choose game: " + throwable.getMessage());
                                return null;
                            });

                    player.sendMessage("§aSending you to " + resp.server().shortName() + "!");
                    player.asProxyPlayer().transferToWithIndication(resp.server().uuid());
                } else {
                    player.sendMessage("§cNo available servers found! Please try again later.");
                }
            } else {
                player.sendMessage("§cFailed to find server. Please try again later.");
            }
        }).exceptionally(throwable -> {
            PLAYERS_SEARCHING.remove(player.getUuid());
            player.sendMessage("§cFailed to find server: " + throwable.getMessage());
            return null;
        });
    }

    private void handleSkyblockQueue(HypixelPlayer player) {
        player.sendMessage("§aSending you to SkyBlock...");
        player.sendTo(ServerType.SKYBLOCK_ISLAND);
    }

    private void handleSkywarsQueue(HypixelPlayer player, String gamemode) {
        if (PLAYERS_SEARCHING.contains(player.getUuid())) {
            player.sendMessage("§cYou are already searching for a game!");
            return;
        }

        if (PartyManager.isInParty(player)) {
            FullParty party = PartyManager.getPartyFromPlayer(player);
            if (party != null && !party.getLeader().getUuid().equals(player.getUuid())) {
                player.sendMessage("§cYou are in a party! Ask your leader to start the game, or /p leave");
                return;
            }
        }

        SkywarsGameType gameType = parseSkywarsGameType(gamemode);
        if (gameType == null) {
            player.sendMessage("§cUnknown SkyWars game mode: " + gamemode);
            return;
        }

        PLAYERS_SEARCHING.add(player.getUuid());

        GetServerForMapProtocolObject.GetServerForMapMessage message =
                new GetServerForMapProtocolObject.GetServerForMapMessage(
                        ServerType.SKYWARS_GAME,
                        null,
                        gameType.toString(),
                        1
                );

        PROXY_SERVICE.handleRequest(message).thenAccept(response -> {
            PLAYERS_SEARCHING.remove(player.getUuid());

            if (response instanceof GetServerForMapProtocolObject.GetServerForMapResponse resp) {
                if (resp.server() != null && resp.gameId() != null) {
                    ChooseGameProtocolObject.ChooseGameMessage chooseMessage =
                            new ChooseGameProtocolObject.ChooseGameMessage(
                                    player.getUuid(),
                                    resp.server(),
                                    resp.gameId()
                            );

                    PROXY_SERVICE.handleRequest(chooseMessage)
                            .exceptionally(throwable -> {
                                player.sendMessage("§cFailed to choose game: " + throwable.getMessage());
                                return null;
                            });

                    player.sendMessage("§aSending you to " + resp.server().shortName() + "!");
                    player.asProxyPlayer().transferToWithIndication(resp.server().uuid());
                } else {
                    player.sendMessage("§cNo available servers found! Please try again later.");
                }
            } else {
                player.sendMessage("§cFailed to find server. Please try again later.");
            }
        }).exceptionally(throwable -> {
            PLAYERS_SEARCHING.remove(player.getUuid());
            player.sendMessage("§cFailed to find server: " + throwable.getMessage());
            return null;
        });
    }

    private void handleMurderMysteryQueue(HypixelPlayer player, String gamemode) {
        if (PLAYERS_SEARCHING.contains(player.getUuid())) {
            player.sendMessage("§cYou are already searching for a game!");
            return;
        }

        if (PartyManager.isInParty(player)) {
            FullParty party = PartyManager.getPartyFromPlayer(player);
            if (party != null && !party.getLeader().getUuid().equals(player.getUuid())) {
                player.sendMessage("§cYou are in a party! Ask your leader to start the game, or /p leave");
                return;
            }
        }

        MurderMysteryGameType gameType = parseMurderMysteryGameType(gamemode);
        if (gameType == null) {
            player.sendMessage("§cUnknown Murder Mystery game mode: " + gamemode);
            return;
        }

        PLAYERS_SEARCHING.add(player.getUuid());

        GetServerForMapProtocolObject.GetServerForMapMessage message =
                new GetServerForMapProtocolObject.GetServerForMapMessage(
                        ServerType.MURDER_MYSTERY_GAME,
                        null,
                        gameType.toString(),
                        1
                );

        PROXY_SERVICE.handleRequest(message).thenAccept(response -> {
            PLAYERS_SEARCHING.remove(player.getUuid());

            if (response instanceof GetServerForMapProtocolObject.GetServerForMapResponse resp) {
                if (resp.server() != null && resp.gameId() != null) {
                    ChooseGameProtocolObject.ChooseGameMessage chooseMessage =
                            new ChooseGameProtocolObject.ChooseGameMessage(
                                    player.getUuid(),
                                    resp.server(),
                                    resp.gameId()
                            );

                    PROXY_SERVICE.handleRequest(chooseMessage)
                            .exceptionally(throwable -> {
                                player.sendMessage("§cFailed to choose game: " + throwable.getMessage());
                                return null;
                            });

                    player.sendMessage("§aSending you to " + resp.server().shortName() + "!");
                    player.asProxyPlayer().transferToWithIndication(resp.server().uuid());
                } else {
                    player.sendMessage("§cNo available servers found! Please try again later.");
                }
            } else {
                player.sendMessage("§cFailed to find server. Please try again later.");
            }
        }).exceptionally(throwable -> {
            PLAYERS_SEARCHING.remove(player.getUuid());
            player.sendMessage("§cFailed to find server: " + throwable.getMessage());
            return null;
        });
    }

    @Nullable
    private SkywarsGameType parseSkywarsGameType(String gamemode) {
        return switch (gamemode) {
            case "solo_normal" -> SkywarsGameType.SOLO_NORMAL;
            case "solo_insane" -> SkywarsGameType.SOLO_INSANE;
            case "teams_normal" -> SkywarsGameType.DOUBLES_NORMAL;
            case "solo_insane_lucky" -> SkywarsGameType.SOLO_LUCKY_BLOCK;
            default -> null;
        };
    }

    @Nullable
    private MurderMysteryGameType parseMurderMysteryGameType(String gamemode) {
        return switch (gamemode) {
            case "murder_classic" -> MurderMysteryGameType.CLASSIC;
            case "murder_double_up" -> MurderMysteryGameType.DOUBLE_UP;
            case "murder_assassins" -> MurderMysteryGameType.ASSASSINS;
            default -> null;
        };
    }

    @Nullable
    private BedwarsGameType parseBedwarsGameType(String gamemode) {
        return switch (gamemode) {
            case "bedwars_eight_one" -> BedwarsGameType.SOLO;
            case "bedwars_eight_two" -> BedwarsGameType.DOUBLES;
            case "bedwars_four_three" -> BedwarsGameType.THREE_THREE_THREE_THREE;
            case "bedwars_four_four" -> BedwarsGameType.FOUR_FOUR_FOUR_FOUR;
            case "bedwars_two_four" -> BedwarsGameType.FOUR_FOUR;
            case "bedwars_eight_two_ultimate" -> BedwarsGameType.ULTIMATE_DOUBLES;
            case "bedwars_four_four_ultimate" -> BedwarsGameType.ULTIMATE_FOURS;
            default -> null;
        };
    }
}

