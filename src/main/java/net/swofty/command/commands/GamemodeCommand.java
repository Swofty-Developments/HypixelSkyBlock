package net.swofty.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.GameMode;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.item.ItemType;
import net.swofty.item.SkyBlockItem;
import net.swofty.user.Rank;
import net.swofty.user.SkyBlockPlayer;

@CommandParameters(aliases = "gm", description = "Sets a players gamemode", usage = "/gamemode <gamemode>", permission = Rank.ADMIN, allowsConsole = false)
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
