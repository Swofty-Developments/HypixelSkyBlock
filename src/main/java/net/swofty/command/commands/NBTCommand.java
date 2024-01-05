package net.swofty.command.commands;

import net.minestom.server.item.ItemStack;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.attribute.ItemAttribute;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

@CommandParameters(aliases = "iteminfo",
        description = "Returns the players held item info",
        usage = "/nbt",
        permission = Rank.HELPER,
        allowsConsole = false)
public class NBTCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            ItemStack item = ((SkyBlockPlayer) sender).getInventory().getItemInMainHand();
            SkyBlockItem skyBlockItem = new SkyBlockItem(item);

            ItemAttribute.getPossibleAttributes().forEach(attribute -> {
                sender.sendMessage(attribute.getKey() + ": " + ((ItemAttribute<?>) skyBlockItem.getAttribute(attribute.getKey())).saveIntoString());
            });
        });
    }
}
