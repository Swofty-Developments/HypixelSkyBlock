package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.gui.SkyBlockAnvilGUI;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "anvilgraphicaluserinterface",
        description = "Opens a graphical user interface",
        usage = "/anvilgui <text>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class AnvilGUICommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentString text = ArgumentType.String("text");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String anvilText = context.get(text);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            new SkyBlockAnvilGUI(player).open(anvilText).thenAccept(line -> {
                if (line == null) {
                    return;
                }

                player.sendMessage("§7You wrote: §a" + line);
            });
        }, text);
    }
}
