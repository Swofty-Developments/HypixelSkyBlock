package net.swofty.type.skyblockgeneric.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.data.monogdb.CrystalDatabase;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "deleteorb",
        description = "Deletes orbs at the player's location.",
        usage = "/removeorb",
        permission = Rank.STAFF,
        allowsConsole = false)
public class RemoveOrbCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            new CrystalDatabase().removeCrystals(((SkyBlockPlayer) sender).getPosition(), 1.5);
        });
    }
}
