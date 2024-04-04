package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.GameMode;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "gm",
        description = "Sets a players gamemode",
        usage = "/gamemode <gamemode>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class GamemodeCommand extends SkyBlockCommand {

    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<GameMode> gamemode = ArgumentType.Enum("gamemode", GameMode.class);
        gamemode.setFormat(ArgumentEnum.Format.LOWER_CASED);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            final GameMode gamemodeType = context.get(gamemode);

            ((SkyBlockPlayer) sender).setGameMode(gamemodeType);

            sender.sendMessage("§aSet your gamemode to §e" + gamemodeType.name() + "§a.");
        }, gamemode);
    }

}
