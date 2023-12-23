package net.swofty.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.structure.structures.IslandPortal;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

@CommandParameters(aliases = "structure",
        description = "Places a test debug structure",
        usage = "/structure",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class PlaceStructureCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentInteger rotation = ArgumentType.Integer("rotation");

        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            IslandPortal portal = new IslandPortal(context.get(rotation),
                    player.getPosition().blockX(),
                    player.getPosition().blockY(),
                    player.getPosition().blockZ());

            portal.setType(IslandPortal.PortalType.HUB);
            portal.build(player.getInstance());
        }, rotation);
    }
}
