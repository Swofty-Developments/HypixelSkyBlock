package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Modifies the NBT of the item in your hand",
        usage = "/changenbt <key> <value>",
        permission = Rank.ADMIN,
        aliases = "editnbt",
        allowsConsole = false)
public class ChangeNBTCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentString keyArgument = ArgumentType.String("key");
        ArgumentString valueArgument = ArgumentType.String("value");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String key = context.get(keyArgument);
            String value = context.get(valueArgument);

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            SkyBlockItem item = new SkyBlockItem(player.getInventory().getItemInHand(Player.Hand.MAIN));

            ItemAttribute attribute = (ItemAttribute) item.getAttribute(key);
            attribute.setValue(attribute.loadFromString(value));

            player.getInventory().setItemInHand(Player.Hand.MAIN, item.getItemStack());
            player.sendMessage("§aSuccessfully changed NBT key §e" + key + "§a to §e" + value + "§a.");
        }, keyArgument, valueArgument);
    }
}
