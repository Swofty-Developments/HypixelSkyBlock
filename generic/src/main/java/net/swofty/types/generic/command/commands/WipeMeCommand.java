package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.utility.MathUtility;

@CommandParameters(description = "Allows the player to wipe themselves",
        usage = "/wipeme",
        permission = Rank.ADMIN,
        aliases = "deletemyprofiles",
        allowsConsole = false)
public class WipeMeCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.kick("§cYou have been wiped");

            MathUtility.delay(() -> {

            }, 4);
        });
    }
}
