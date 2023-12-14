package net.swofty.command.commands;

import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentGroup;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.item.ItemType;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.attribute.ItemAttribute;
import net.swofty.item.attribute.attributes.ItemAttributeType;
import net.swofty.user.Rank;
import net.swofty.user.SkyBlockPlayer;

@CommandParameters(aliases = "iteminfo", description = "Returns the players held item info", usage = "/nbt", permission = Rank.HELPER, allowsConsole = false)
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
