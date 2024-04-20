package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "updatecollection",
        description = "Updates the collection of a player",
        usage = "/setcollection <item_type> <amount>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class SetCollectionCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<ItemType> itemType = new ArgumentEnum("item_type", ItemType.class);
        ArgumentInteger amountArgument = new ArgumentInteger("amount");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            ItemType type = context.get(itemType);
            int amount = context.get(amountArgument);

            player.getCollection().set(type, amount);
            player.sendMessage("§aUpdated your §e" + CollectionCategories.getCategory(type).getName()
                    + " §acategory for the §c" + type.getDisplayName(null)
                    + " §acollection to §e" + amount + "§a.");
        }, itemType, amountArgument);
    }
}
