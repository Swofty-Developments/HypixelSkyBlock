package net.swofty.command.commands;

import net.minestom.server.command.builder.arguments.*;
import net.minestom.server.entity.Entity;
import net.swofty.SkyBlock;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.entity.hologram.HologramEntity;
import net.swofty.user.Rank;
import net.swofty.user.SkyBlockPlayer;

import java.util.Arrays;

@CommandParameters(aliases = "spawnholo", description = "Rank command", usage = "/spawnhologram <text>", permission = Rank.ADMIN, allowsConsole = false)
public class SpawnHologramCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentStringArray nameArgument = ArgumentType.StringArray("name");

        command.addSyntax((sender, context) -> {
            String[] nameArray = context.get(nameArgument);
            String name = String.join(" ", nameArray);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            Entity entity = new HologramEntity(name);
            entity.setInstance(SkyBlock.getInstanceContainer(), player.getPosition());
            entity.setAutoViewable(true);
            entity.spawn();
        }, nameArgument);
    }
}
