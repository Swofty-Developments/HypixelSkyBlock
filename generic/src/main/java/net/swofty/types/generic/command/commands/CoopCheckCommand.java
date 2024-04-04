package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.gui.inventory.inventories.coop.GUICoopInviteSender;
import net.swofty.types.generic.gui.inventory.inventories.coop.GUICoopInviteTarget;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "cooperativecheck",
        description = "Checks outgoing invites",
        usage = "/coop",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class CoopCheckCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

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
