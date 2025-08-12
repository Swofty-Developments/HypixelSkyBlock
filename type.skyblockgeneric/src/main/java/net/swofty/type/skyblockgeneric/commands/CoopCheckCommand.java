package net.swofty.type.skyblockgeneric.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.monogdb.CoopDatabase;
import net.swofty.type.generic.gui.inventories.coop.GUICoopInviteSender;
import net.swofty.type.generic.gui.inventories.coop.GUICoopInviteTarget;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "cooperativecheck",
        description = "Checks outgoing invites",
        usage = "/coop",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class CoopCheckCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
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
