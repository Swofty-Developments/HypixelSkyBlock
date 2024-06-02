package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.collection.GUICollectionItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Opens up a collections GUI",
        usage = "/viewcollection <collection>",
        permission = Rank.DEFAULT,
        aliases = "vc",
        allowsConsole = false)
public class ViewCollectionCommand extends SkyBlockCommand {

    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<ItemType> itemArgument = ArgumentType.Enum("item", ItemType.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            final ItemType itemType = context.get(itemArgument);

            if (CollectionCategories.getCategory(itemType) == null) {
                sender.sendMessage("§cThis item does not have a collection!");
            }

            new GUICollectionItem(itemType).open((SkyBlockPlayer) sender);
        }, itemArgument);
    }
}
