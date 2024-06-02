package net.swofty.types.generic.command.commands;

import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "iteminfo",
        description = "Returns the players held item info",
        usage = "/nbt",
        permission = Rank.HELPER,
        allowsConsole = false)
public class NBTCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            ItemStack item = ((SkyBlockPlayer) sender).getInventory().getItemInMainHand();
            SkyBlockItem skyBlockItem = new SkyBlockItem(item);

            ItemAttribute.getPossibleAttributes().forEach(attribute -> {
                sender.sendMessage(attribute.getKey() + ": " + ((ItemAttribute<?>) skyBlockItem.getAttribute(attribute.getKey())).saveIntoString());
            });
        });
    }
}
