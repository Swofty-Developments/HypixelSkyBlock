package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentDouble;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(description = "Tests the head rotation of ArmorStands",
        usage = "/testheadrotation",
        permission = Rank.STAFF,
        aliases = "testheadrot",
        allowsConsole = false)
public class TestHeadRotation extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
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
