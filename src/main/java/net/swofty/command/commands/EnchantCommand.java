package net.swofty.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.enchantment.EnchantmentType;
import net.swofty.enchantment.SkyBlockEnchantment;
import net.swofty.item.attribute.AttributeHandler;
import net.swofty.item.updater.PlayerItemOrigin;
import net.swofty.item.updater.PlayerItemUpdater;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

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
            new PlayerItemUpdater((player, item) -> {
                AttributeHandler attributeHandler = item.getAttributeHandler();
                attributeHandler.removeEnchantment(context.get(typeArgument));
                attributeHandler.addEnchantment(
                        SkyBlockEnchantment.builder()
                                .level(context.get(level))
                                .type(context.get(typeArgument))
                                .build()
                );
                return attributeHandler.asSkyBlockItem();
            }).queueUpdate((SkyBlockPlayer) sender, PlayerItemOrigin.MAIN_HAND).thenAccept((item) -> {
                sender.sendMessage("§aYour item has been enchanted");
            });
        }, typeArgument, level);
    }
}
