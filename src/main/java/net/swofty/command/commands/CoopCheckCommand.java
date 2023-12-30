package net.swofty.command.commands;

import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointCoopInvitation;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

import java.util.List;

@CommandParameters(aliases = "cooperativecheck",
        description = "Checks outgoing invites",
        usage = "/coop",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class CoopCheckCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            boolean hasOutgoing = player.getDataHandler().get(DataHandler.Data.COOP_INVITES, DatapointCoopInvitation.class)
                    .getValue().stream().anyMatch(DatapointCoopInvitation.CoopInvitation::outgoing);
            boolean hasIncoming = player.getDataHandler().get(DataHandler.Data.COOP_INVITES, DatapointCoopInvitation.class)
                    .getValue().stream().anyMatch(coopInvitation -> !coopInvitation.outgoing());

            player.getDataHandler().get(DataHandler.Data.COOP_INVITES, DatapointCoopInvitation.class).getValue().forEach(coopInvitation -> {
                if (coopInvitation.outgoing()) {
                    player.sendMessage("§dYou have an outgoing co-op invite to §b" + coopInvitation.target() + "§e!");
                } else {
                    player.sendMessage("§dYou have an incoming co-op invite from §b" + coopInvitation.target() + "§e!");
                }
            });

            if (hasOutgoing) {
                List<DatapointCoopInvitation.CoopInvitation> outgoingInvites = player.getDataHandler().get(DataHandler.Data.COOP_INVITES, DatapointCoopInvitation.class)
                        .getValue().stream().filter(DatapointCoopInvitation.CoopInvitation::outgoing).toList();

                player.sendMessage("§eYou have an outgoing co-op invite to §b" + outgoingInvites.get(0).target() + "§e!");
            } else if (hasIncoming) {
                DatapointCoopInvitation.CoopInvitation incomingInvite = player.getDataHandler().get(DataHandler.Data.COOP_INVITES, DatapointCoopInvitation.class)
                        .getValue().stream().filter(coopInvitation -> !coopInvitation.outgoing()).toList().get(0);

                player.sendMessage("§eYou have an incoming co-op invite from §b" + incomingInvite.target() + "§e!");
            } else {
                player.sendMessage("§cYou don't have any co-op invites!");
            }
        });
    }
}
