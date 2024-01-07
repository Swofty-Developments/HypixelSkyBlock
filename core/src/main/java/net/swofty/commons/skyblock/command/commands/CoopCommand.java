package net.swofty.commons.skyblock.command.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.commons.skyblock.SkyBlock;
import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.data.mongodb.CoopDatabase;
import net.swofty.commons.skyblock.user.categories.Rank;

import java.util.Arrays;
import java.util.List;

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

            if (Arrays.stream(players).anyMatch(player1 -> {
                return SkyBlock.getLoadedPlayers().stream().map(SkyBlockPlayer::getUsername).noneMatch(player1::equalsIgnoreCase);
            })) {
                player.sendMessage("§b[Co-op] §cOne or more of the players you specified are not online!");
                return;
            }

            if (Arrays.stream(players).anyMatch(player1 -> CoopDatabase.getFromMember(
                    SkyBlock.getLoadedPlayers().stream().filter(player2 -> player2.getUsername().equalsIgnoreCase(player1)).findFirst().get().getUuid()
            ) != null)) {
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

            List<SkyBlockPlayer> invitedPlayers = SkyBlock.getLoadedPlayers().stream().filter(player1 -> {
                return Arrays.stream(players).anyMatch(player2 -> player2.equalsIgnoreCase(player1.getUsername()));
            }).toList();

            CoopDatabase.Coop coop = CoopDatabase.getClean(player.getUuid());

            invitedPlayers.forEach(invitedPlayer -> {
                coop.addInvite(invitedPlayer.getUuid());
                player.sendMessage("§b[Co-op] §eYou invited " + invitedPlayer.getUsername() + " to a SkyBlock co-op!");

                invitedPlayer.sendMessage("§b----------------------------------------");
                invitedPlayer.sendMessage(player.getFullDisplayName() + " §einvited you to a SkyBlock co-op!");
                invitedPlayer.sendMessage(Component.text("§6Click here §eto view!")
                        .hoverEvent(Component.text("§eClick here to view the invite"))
                        .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/coopcheck")));
                invitedPlayer.sendMessage("§b----------------------------------------");
            });

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
                player.sendMessage("§eRun §a/coop leave §eto leave your current co-op.");
                return true;
            }

            boolean isOriginator = coop.isOriginator(player.getUuid());

            if (isOriginator) {
                player.sendMessage(Component.text("§eYou already have an outgoing co-op invite! §a§lCLICK TO VIEW!")
                        .hoverEvent(Component.text("§eClick here to view the invite"))
                        .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/coopcheck")));
                return true;
            } else {
                player.sendMessage(Component.text("§cYou already have an incoming co-op invite! §a§lCLICK TO VIEW!")
                        .hoverEvent(Component.text("§eClick here to view the invite"))
                        .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/coopcheck")));
                return true;
            }
        }
        return false;
    }
}
