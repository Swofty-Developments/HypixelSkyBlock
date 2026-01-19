package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.gui.inventories.GUICreative;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "e",
        description = "Open the E menu",
        usage = "/e",
        permission = Rank.STAFF,
        allowsConsole = false)
public class ItemListCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            ((SkyBlockPlayer) sender).openView(new GUICreative(), GUICreative.createInitialState());
        });

        ArgumentString lookup = new ArgumentString("lookup");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String lookupValue = context.get(lookup);
            ((SkyBlockPlayer) sender).openView(new GUICreative(), GUICreative.createInitialState(lookupValue, 0));
        }, lookup);
    }
}
