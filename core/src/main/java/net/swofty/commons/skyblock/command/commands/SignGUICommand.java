package net.swofty.commons.skyblock.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.commons.skyblock.gui.SkyBlockSignGUI;
import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

@CommandParameters(aliases = "signgraphicaluserinterface",
        description = "Opens a graphical user interface",
        usage = "/signgui <text>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class SignGUICommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentString text = ArgumentType.String("text");

        command.addSyntax((sender, context) -> {
            String signContent = context.get(text);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            new SkyBlockSignGUI(player).open(new String[]{"Test1", "Test2"}).thenAccept(line -> {
                if (line == null) {
                    System.out.println(player.getDisplayName().toString() + " left server while GUI was open");
                    return;
                }

                player.sendMessage("§7You wrote: §a" + line);
                System.out.println(line);
            });
        }, text);
    }
}
