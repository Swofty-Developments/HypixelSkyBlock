package net.swofty.commons.skyblock.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.GameMode;
import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

@CommandParameters(aliases = "gm",
        description = "Sets a players gamemode",
        usage = "/gamemode <gamemode>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class GamemodeCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<GameMode> gamemode = ArgumentType.Enum("gamemode", GameMode.class);

        command.addSyntax((sender, context) -> {
            final GameMode gamemodeType = context.get(gamemode);

            ((SkyBlockPlayer) sender).setGameMode(gamemodeType);

            sender.sendMessage("§aSet your gamemode to §e" + gamemodeType.name() + "§a.");
        }, gamemode);
    }
}
