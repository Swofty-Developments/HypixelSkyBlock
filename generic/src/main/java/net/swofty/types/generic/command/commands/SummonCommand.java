package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentEntityType;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "summonmob",
        description = "Summons a mob at your location",
        usage = "/summon <mob>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class SummonCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        // WIP
        ArgumentEntityType entityType = new ArgumentEntityType("mob");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            EntityType type = context.get(entityType);
            Entity entity = new Entity(type);
            entity.spawn();
            entity.teleport(player.getPosition());
        }, entityType);
    }
}
