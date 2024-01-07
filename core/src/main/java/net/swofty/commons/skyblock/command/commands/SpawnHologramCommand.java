package net.swofty.commons.skyblock.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Entity;
import net.swofty.commons.skyblock.SkyBlock;
import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.entity.hologram.HologramEntity;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

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
