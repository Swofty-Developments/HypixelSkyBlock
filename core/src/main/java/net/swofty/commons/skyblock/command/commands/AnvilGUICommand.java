package net.swofty.commons.skyblock.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.gui.SkyBlockAnvilGUI;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

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
