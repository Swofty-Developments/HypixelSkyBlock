package net.swofty.type.generic.command.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.generic.utility.MathUtility;

@CommandParameters(description = "Allows the player to wipe themselves",
        usage = "/wipeme",
        permission = Rank.STAFF,
        aliases = "deletemyprofiles",
        allowsConsole = false)
public class WipeMeCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            player.kick("§cYou have been wiped");

            MathUtility.delay(() -> {
                UserDatabase database = new UserDatabase(player.getUuid());
                SkyBlockPlayerProfiles profiles = database.getProfiles();

                database.deleteSelf();
                profiles.getProfiles().forEach(uuid ->
                        new ProfilesDatabase(player.getUuid().toString()).remove(String.valueOf(uuid))
                );
            }, 4);
        });
    }
}
