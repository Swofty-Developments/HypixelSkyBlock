package net.swofty.commons.skyblock.command.commands;

import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.data.mongodb.CoopDatabase;
import net.swofty.commons.skyblock.gui.inventory.inventories.coop.GUICoopInviteSender;
import net.swofty.commons.skyblock.gui.inventory.inventories.coop.GUICoopInviteTarget;
import net.swofty.commons.skyblock.user.categories.Rank;

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
