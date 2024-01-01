package net.swofty.command.commands;

import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.data.mongodb.CoopDatabase;
import net.swofty.gui.inventory.inventories.coop.GUICoopInviteSender;
import net.swofty.gui.inventory.inventories.coop.GUICoopInviteTarget;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

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
            CoopDatabase.Coop coop = CoopDatabase.getFromMember(player.getUuid());

            if (coop == null) {
                player.sendMessage("§cYou don't have any co-op invites!");
                return;
            }

            if (coop.members().contains(player.getUuid())) {
                player.sendMessage("§b[Co-op] §cYou are already in a co-op!");
                return;
            }

            if (coop.isOriginator(player.getUuid())) {
                new GUICoopInviteSender(coop).open(player);
            } else {
                new GUICoopInviteTarget(coop).open(player);
            }
        });
    }
}
