package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.instance.SharedInstance;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.structure.structures.IslandPortal;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "structure",
        description = "Places a test debug structure",
        usage = "/structure",
        permission = Rank.STAFF,
        allowsConsole = false)
public class PlaceStructureCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentInteger rotation = ArgumentType.Integer("rotation");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            IslandPortal portal = new IslandPortal(context.get(rotation),
                    player.getPosition().blockX(),
                    player.getPosition().blockY(),
                    player.getPosition().blockZ());

            portal.setType(IslandPortal.PortalType.HUB);
            portal.build((SharedInstance) player.getInstance());
        }, rotation);
    }
}
