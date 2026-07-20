package net.swofty.type.skyblockgeneric.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.gui.inventories.hunting.GUIHuntingBox;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "huntingbox",
        description = "Opens your Hunting Box",
        usage = "/huntingbox",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class HuntingBoxCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            ((SkyBlockPlayer) sender).openView(new GUIHuntingBox());
        });
    }
}
