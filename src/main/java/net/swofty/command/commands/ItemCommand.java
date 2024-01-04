package net.swofty.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.item.ItemType;
import net.swofty.item.SkyBlockItem;
import net.swofty.user.categories.Rank;
import net.swofty.user.SkyBlockPlayer;

@CommandParameters(aliases = "i",
        description = "Gives an item to the player",
        usage = "/item <item> [amount]",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class ItemCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<ItemType> itemArgument = ArgumentType.Enum("item", ItemType.class);
        ArgumentInteger amountArgument = ArgumentType.Integer("amount");

        command.addSyntax((sender, context) -> {
            final ItemType itemType = context.get(itemArgument);

            SkyBlockItem item = new SkyBlockItem(itemType);
            ((SkyBlockPlayer) sender).getInventory().addItemStack(item.getItemStack());

            sender.sendMessage("§aGiven you item §e" + itemType.name() + "§a.");
        }, itemArgument);

        command.addSyntax((sender, context) -> {
            final ItemType itemType = context.get(itemArgument);
            final int amount = context.get(amountArgument);

            SkyBlockItem item = new SkyBlockItem(itemType);
            item.setAmount(amount);
            ((SkyBlockPlayer) sender).getInventory().addItemStack(item.getItemStack());

            sender.sendMessage("§aGiven you item §e" + itemType.name() + "§8 x" + amount + "§a.");
        }, itemArgument, amountArgument);
    }
}
