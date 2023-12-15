package net.swofty.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.item.ItemType;
import net.swofty.item.SkyBlockItem;
import net.swofty.user.categories.Rank;
import net.swofty.user.SkyBlockPlayer;

@CommandParameters(aliases = "giveitem",
        description = "Gives an item to the player",
        usage = "/item <item>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class ItemCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<ItemType> itemArgument = ArgumentType.Enum("item", ItemType.class);

        command.addSyntax((sender, context) -> {
            final ItemType itemType = context.get(itemArgument);

            SkyBlockItem item = new SkyBlockItem(itemType);
            ((SkyBlockPlayer) sender).getInventory().addItemStack(item.getItemStack());

            sender.sendMessage("§aGiven you item §e" + itemType.name() + "§a.");
        }, itemArgument);
    }
}
