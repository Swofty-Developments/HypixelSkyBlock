package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.enchantment.EnchantmentType;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.item.attribute.AttributeHandler;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "ench",
        description = "Enchants the contents o the players hand",
        usage = "/enchant",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class EnchantCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<EnchantmentType> typeArgument =
                ArgumentType.Enum("enchantment_type", EnchantmentType.class);
        ArgumentInteger level = ArgumentType.Integer("level");

        command.addSyntax((sender, context) -> {
            ((SkyBlockPlayer) sender).updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                AttributeHandler attributeHandler = item.getAttributeHandler();
                attributeHandler.addEnchantment(
                        SkyBlockEnchantment.builder()
                                .level(context.get(level))
                                .type(context.get(typeArgument))
                                .build()
                );
                sender.sendMessage("§aYour item has been enchanted");
            });
        }, typeArgument, level);
    }
}
