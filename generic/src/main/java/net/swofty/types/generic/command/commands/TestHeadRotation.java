package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentDouble;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Tests the head rotation of ArmorStands",
        usage = "/testheadrotation",
        permission = Rank.ADMIN,
        aliases = "testheadrot",
        allowsConsole = false)
public class TestHeadRotation extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentDouble x = ArgumentType.Double("x");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            Double xValue = context.get(x);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            LivingEntity entity = new LivingEntity(EntityType.ARMOR_STAND);
            entity.setInstance(player.getInstance(), player.getPosition());
            ((ArmorStandMeta) entity.getEntityMeta()).setHeadRotation(new Vec(xValue, 0, 0));
        }, x);
    }
}
