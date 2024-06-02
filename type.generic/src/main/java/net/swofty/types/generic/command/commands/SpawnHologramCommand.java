package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Entity;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.entity.hologram.HologramEntity;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "spawnholo",
        description = "Rank command",
        usage = "/spawnhologram <text>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class SpawnHologramCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentStringArray nameArgument = ArgumentType.StringArray("name");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String[] nameArray = context.get(nameArgument);
            String name = String.join(" ", nameArray);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            Entity entity = new HologramEntity(name);
            entity.setAutoViewable(true);
            entity.setInstance(SkyBlockConst.getInstanceContainer(), player.getPosition());
        }, nameArgument);
    }
}
