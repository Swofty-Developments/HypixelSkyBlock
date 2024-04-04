package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.data.mongodb.UserDatabase;
import net.swofty.types.generic.user.PlayerProfiles;
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
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.kick("§cYou have been wiped");

            MathUtility.delay(() -> {
                UserDatabase database = new UserDatabase(player.getUuid());
                PlayerProfiles profiles = database.getProfiles();

                database.deleteProfiles();
                profiles.getProfiles().forEach(uuid ->
                        new ProfilesDatabase(player.getUuid().toString()).remove(String.valueOf(uuid))
                );
            }, 4);
        });
    }
}
