package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "i",
        description = "Gives an item to the player",
        usage = "/item <item> [amount]",
        permission = Rank.STAFF,
        allowsConsole = false)
public class ItemCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<ItemType> itemArgument = ArgumentType.Enum("item", ItemType.class);
        ArgumentInteger amountArgument = ArgumentType.Integer("amount");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            final ItemType itemTypeLinker = context.get(itemArgument);

            SkyBlockItem item = new SkyBlockItem(itemTypeLinker);
            ((SkyBlockPlayer) sender).addAndUpdateItem(item);

            sender.sendMessage("§aGiven you item §e" + itemTypeLinker.name() + "§a.");
        }, itemArgument);

        command.addSyntax((sender, context) -> {
            final ItemType itemTypeLinker = context.get(itemArgument);
            final int amount = context.get(amountArgument);

            SkyBlockItem item = new SkyBlockItem(itemTypeLinker);
            item.setAmount(amount);
            ((SkyBlockPlayer) sender).addAndUpdateItem(item);

            sender.sendMessage("§aGiven you item §e" + itemTypeLinker.name() + "§8 x" + amount + "§a.");
        }, itemArgument, amountArgument);
    }
}
