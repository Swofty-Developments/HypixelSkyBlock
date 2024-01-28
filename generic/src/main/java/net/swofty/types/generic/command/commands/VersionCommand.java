package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.commons.MinecraftVersion;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.collection.GUICollectionItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Tells the player their version",
        usage = "/version",
        permission = Rank.DEFAULT,
        aliases = "playerversion",
        allowsConsole = false)
public class VersionCommand extends SkyBlockCommand {

    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            MinecraftVersion playerVersion = ((SkyBlockPlayer) sender).getVersion();
            sender.sendMessage("§aYou are currently running §e" + playerVersion.name() + "§a!");
        });
    }
}
