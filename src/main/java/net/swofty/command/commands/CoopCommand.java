package net.swofty.command.commands;

import kotlin.reflect.jvm.internal.impl.types.model.ArgumentList;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.swofty.SkyBlock;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointCoopInvitation;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;
import org.tinylog.Logger;

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

            player.sendMessage("§cYou don't have an outgoing co-op invite!");
            player.sendMessage("§eUse §b/coop <player 1> <player 2>... §eto create one!");
            player.sendMessage("§eUse §a/coopadd <player> §eto add a player to your current co-op!");
        });

        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            String[] players = context.get(args);
            DatapointCoopInvitation coopInvitation = player.getDataHandler().get(DataHandler.Data.COOP_INVITES, DatapointCoopInvitation.class);

            if (!coopInvitation.getValue().isEmpty()) {
                boolean hasOutgoing = coopInvitation.getValue().stream().anyMatch(DatapointCoopInvitation.CoopInvitation::outgoing);

                if (hasOutgoing) {
                    player.sendMessage(Component.text("§eYou already have an outgoing co-op invite! §a§lCLICK TO VIEW!")
                            .hoverEvent(Component.text("§eClick here to view the invite"))
                            .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/coopcheck")));
                    return;
                } else {
                    player.sendMessage(Component.text("§cYou already have an incoming co-op invite! §a§lCLICK TO VIEW!")
                            .hoverEvent(Component.text("§eClick here to view the invite"))
                            .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/coopcheck")));
                    return;
                }
            }

            player.sendMessage("§7Validating invite...");

            if (Arrays.stream(players).anyMatch(player1 -> player1.equalsIgnoreCase(player.getUsername()))) {
                player.sendMessage("§cYou can't invite yourself to a co-op!");
                return;
            }

            if (Arrays.stream(players).anyMatch(player1 ->  {
                return SkyBlock.getLoadedPlayers().stream().map(SkyBlockPlayer::getUsername).noneMatch(player1::equalsIgnoreCase);
            })) {
                player.sendMessage("§b[Co-op] §cOne or more of the players you specified are not online!");
                return;
            }

            if (Arrays.stream(players).anyMatch(player1 -> {
                SkyBlockPlayer player2 = SkyBlock.getLoadedPlayers().stream().filter(player3 -> player3.getUsername().equalsIgnoreCase(player1)).findFirst().orElse(null);
                if (player2 == null) return false;

                return !player2.getDataHandler().get(DataHandler.Data.COOP_INVITES, DatapointCoopInvitation.class).getValue().isEmpty();
            })) {
                player.sendMessage("§b[Co-op] §cOne or more of the players you specified already have an incoming co-op invite!");
                return;
            }

            // Check if player put same player name in twice
            if (Arrays.stream(players).distinct().count() != Arrays.stream(players).count()) {
                player.sendMessage("§b[Co-op] §cYou can't invite the same player twice!");
                return;
            }

            List<SkyBlockPlayer> invitedPlayers = SkyBlock.getLoadedPlayers().stream().filter(player1 -> {
                return Arrays.stream(players).anyMatch(player2 -> player2.equalsIgnoreCase(player1.getUsername()));
            }).toList();

            invitedPlayers.forEach(invitedPlayer -> {
                player.sendMessage("§b[Co-op] §eYou invited " + invitedPlayer.getUsername() + " to a SkyBlock co-op!");
                player.sendMessage(Component.text("§eUse §b/coop §eor §a§lCLICK THIS §efor status!")
                        .hoverEvent(Component.text("§eClick here to view the invite"))
                        .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/coopcheck")));

                DatapointCoopInvitation.CoopInvitation senderInvitation = new DatapointCoopInvitation.CoopInvitation(
                        true,
                        invitedPlayer.getUuid(),
                        false,
                        System.currentTimeMillis());

                Logger.info("Adding to " + player.getUsername() + "'s coop invites");
                Logger.info(player.getDataHandler().get(DataHandler.Data.COOP_INVITES, DatapointCoopInvitation.class).getValue().size());
                player.getDataHandler().get(DataHandler.Data.COOP_INVITES, DatapointCoopInvitation.class).add(senderInvitation);
                Logger.info(player.getDataHandler().get(DataHandler.Data.COOP_INVITES, DatapointCoopInvitation.class).getValue().size());

                invitedPlayer.sendMessage("§b----------------------------------------");
                invitedPlayer.sendMessage(player.getFullDisplayName() + " §einvited you to a SkyBlock co-op!");
                invitedPlayer.sendMessage(Component.text("§6Click here §eto view!")
                                .hoverEvent(Component.text("§eClick here to view the invite"))
                                .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/coopcheck")));
                invitedPlayer.sendMessage("§b----------------------------------------");

                DatapointCoopInvitation.CoopInvitation targetInvitation = new DatapointCoopInvitation.CoopInvitation(
                        false,
                        player.getUuid(),
                        false,
                        System.currentTimeMillis());

                Logger.info("Adding to " + invitedPlayer.getUsername() + "'s coop invites");
                Logger.info(invitedPlayer.getDataHandler().get(DataHandler.Data.COOP_INVITES, DatapointCoopInvitation.class).getValue().size());
                invitedPlayer.getDataHandler().get(DataHandler.Data.COOP_INVITES, DatapointCoopInvitation.class).add(targetInvitation);
                Logger.info(invitedPlayer.getDataHandler().get(DataHandler.Data.COOP_INVITES, DatapointCoopInvitation.class).getValue().size());
            });
        }, args);
    }
}
