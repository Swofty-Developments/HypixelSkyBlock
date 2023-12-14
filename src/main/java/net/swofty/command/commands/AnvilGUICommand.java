package net.swofty.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.gui.SkyBlockAnvilGUI;
import net.swofty.gui.SkyBlockSignGUI;
import net.swofty.user.Rank;
import net.swofty.user.SkyBlockPlayer;

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
                    System.out.println(sender.asPlayer().getName() + " left server while GUI was open");
                    return;
                }

                System.out.println("Line: " + line);
                player.sendMessage("§7You wrote: §a" + line);
            });
        }, text);
    }
}
