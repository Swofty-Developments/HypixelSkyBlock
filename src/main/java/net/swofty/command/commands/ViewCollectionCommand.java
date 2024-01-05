package net.swofty.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.collection.CollectionCategories;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.item.ItemType;
import net.swofty.user.categories.Rank;

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
            final ItemType itemType = context.get(itemArgument);

            if (CollectionCategories.getCategory(itemType) == null) {
                sender.sendMessage("§cThis item does not have a collection!");
            }
        });
    }
}
