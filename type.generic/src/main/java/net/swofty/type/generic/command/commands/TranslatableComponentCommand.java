package net.swofty.type.generic.command.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@SuppressWarnings("unused")
@CommandParameters(aliases = "translatablecomponent",
    description = "Preview a TranslatableComponent",
    usage = "/translatablecomponent <string>",
    permission = Rank.STAFF,
    allowsConsole = false)
public class TranslatableComponentCommand extends HypixelCommand {

    @Override
    public void registerUsage(HypixelCommand.MinestomCommand command) {
        ArgumentString arg = ArgumentType.String("translatable");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            String key = context.get(arg);

            HypixelPlayer player = (HypixelPlayer) sender;
            player.sendMessage(Component.translatable(key));
        }, arg);
    }

}
