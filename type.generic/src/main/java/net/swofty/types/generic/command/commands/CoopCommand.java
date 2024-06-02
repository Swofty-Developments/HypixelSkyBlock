package net.swofty.types.generic.command.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyPlayerSet;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@CommandParameters(aliases = "cooperative",
        description = "Primary coop command",
        usage = "/coop",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class CoopCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentStringArray args = ArgumentType.StringArray("player");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            if (checkIfAlreadyExisting(player)) return;

            player.sendMessage("§cYou don't have an outgoing co-op invite!");
            player.sendMessage("§eUse §b/coop <player 1> <player 2>... §eto create one!");
            player.sendMessage("§eUse §a/coopadd <player> §eto add a player to your current co-op!");
        });

        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            String[] players = context.get(args);

            if (checkIfAlreadyExisting(player)) return;

            player.sendMessage("§7Validating invite...");

            if (Arrays.stream(players).anyMatch(player1 -> player1.equalsIgnoreCase(player.getUsername()))) {
                player.sendMessage("§cYou can't invite yourself to a co-op!");
                return;
            }

            ProxyPlayerSet chosenPlayers;

            try {
                chosenPlayers = new ProxyPlayerSet(Arrays.stream(players).map(
                        ProfilesDatabase::fetchUUID
                ).collect(Collectors.toList()));
            } catch (Exception e) {
                player.sendMessage("§b[Co-op] §cOne or more of the players you specified are not online!");
                return;
            }

            if (chosenPlayers.getPlayers().stream().anyMatch(Objects::isNull)) {
                player.sendMessage("§b[Co-op] §cOne or more of the players you specified are not online!");
                return;
            }

            if (chosenPlayers.asProxyPlayers().stream().anyMatch(player1 -> !player1.isOnline().join())) {
                player.sendMessage("§b[Co-op] §cOne or more of the players you specified are not online!");
                return;
            }

            if (chosenPlayers.asProxyPlayers().stream().anyMatch(player1 -> {
                return CoopDatabase.getFromMember(player1.getUuid()) != null;
            })) {
                player.sendMessage("§b[Co-op] §cOne or more of the players you specified already have a co-op or an invite pending!");
                return;
            }

            // Check if player put same player name in twice
            if (Arrays.stream(players).distinct().count() != Arrays.stream(players).count()) {
                player.sendMessage("§b[Co-op] §cYou can't invite the same player twice!");
                return;
            }

            // Check if there are more than 4 names
            if (players.length > 4) {
                player.sendMessage("§b[Co-op] §cYou can't invite more than 4 players!");
                return;
            }

            CoopDatabase.Coop coop = CoopDatabase.getClean(player.getUuid());

            int i = 0;
            for (ProxyPlayer invitedPlayer : chosenPlayers.asProxyPlayers()) {
                coop.addInvite(invitedPlayer.getUuid());
                player.sendMessage("§b[Co-op] §eYou invited " + players[i] + " to a SkyBlock co-op!");
                i++;

                if (!invitedPlayer.isOnline().join()) continue;

                invitedPlayer.sendMessage("§b----------------------------------------");
                invitedPlayer.sendMessage(player.getFullDisplayName() + " §einvited you to a SkyBlock co-op!");
                invitedPlayer.sendMessage(Component.text("§6Click here §eto view!")
                        .hoverEvent(Component.text("§eClick here to view the invite"))
                        .clickEvent(ClickEvent.runCommand("/coopcheck")));
                invitedPlayer.sendMessage("§b----------------------------------------");
            }

            player.sendMessage(Component.text("§eUse §b/coop §eor §a§lCLICK THIS §efor status!")
                    .hoverEvent(Component.text("§eClick here to view the invite"))
                    .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/coopcheck")));

            coop.save();
        }, args);
    }

    private boolean checkIfAlreadyExisting(SkyBlockPlayer player) {
        CoopDatabase.Coop coop = CoopDatabase.getFromMember(player.getUuid());

        if (coop != null) {
            if (coop.members().contains(player.getUuid())) {
                player.sendMessage("§cYou are already in a co-op!");
                player.sendMessage("§eRun §a/coopleave §eto leave your current co-op.");
                return true;
            }

            boolean isOriginator = coop.isOriginator(player.getUuid());

            if (isOriginator) {
                player.sendMessage(Component.text("§eYou already have an outgoing co-op invite! §a§lCLICK TO VIEW!")
                        .hoverEvent(Component.text("§eClick here to view the invite"))
                        .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/coopcheck")));
            } else {
                player.sendMessage(Component.text("§cYou already have an incoming co-op invite! §a§lCLICK TO VIEW!")
                        .hoverEvent(Component.text("§eClick here to view the invite"))
                        .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/coopcheck")));
            }
            return true;
        }
        return false;
    }
}
