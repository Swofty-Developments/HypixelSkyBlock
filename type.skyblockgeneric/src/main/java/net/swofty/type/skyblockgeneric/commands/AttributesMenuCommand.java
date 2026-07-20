package net.swofty.type.skyblockgeneric.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.gui.inventories.hunting.GUIAttributeMenu;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "attributes attributesmenu",
        description = "Opens your Attributes menu",
        usage = "/attributesmenu",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class AttributesMenuCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, _) -> {
            if (!permissionCheck(sender)) return;
            ((SkyBlockPlayer) sender).openView(new GUIAttributeMenu());
        });
    }
}
