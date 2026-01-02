package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentGroup;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.utils.location.RelativeVec;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegion;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "regions",
        description = "Handles regions across the server",
        usage = "/signgui <text>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class RegionCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentGroup removeRegion = ArgumentType.Group("remove",
                ArgumentType.Literal("remove"),
                ArgumentType.String("region_id"));
        ArgumentGroup addRegion = ArgumentType.Group("add",
                ArgumentType.Literal("add"),
                ArgumentType.String("region_id"),
                ArgumentType.Enum("region_type", RegionType.class),
                ArgumentType.RelativeBlockPosition("pos1"),
                ArgumentType.RelativeBlockPosition("pos2"));

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String regionId = context.get(removeRegion).get("region_id");
            SkyBlockRegion region = SkyBlockRegion.getFromID(regionId);

            if (region == null) {
                sender.sendMessage("§cUnable to find a region by the ID §e" + regionId + "§c.");
                return;
            }

            region.delete();
            sender.sendMessage("§aSuccessfully deleted region §e" + regionId + "§a.");
        }, removeRegion);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String regionId = context.get(addRegion).get("region_id");
            RelativeVec position1 = context.get(addRegion).get("pos1");
            RelativeVec position2 = context.get(addRegion).get("pos2");
            Vec vectorPosition1 = position1.from((SkyBlockPlayer) sender);
            Vec vectorPosition2 = position2.from((SkyBlockPlayer) sender);
            RegionType regionType = context.get(addRegion).get("region_type");

            sender.sendMessage("§aSuccessfully created region §e" + regionId + "§a with type §e" + regionType.name() + "§a.");
            sender.sendMessage("§aPosition 1: §e" + vectorPosition1.x() + ", " + vectorPosition1.y() + ", " + vectorPosition1.z());
            sender.sendMessage("§aPosition 2: §e" + vectorPosition2.x() + ", " + vectorPosition2.y() + ", " + vectorPosition2.z());

            new SkyBlockRegion(regionId,
                    new Pos(vectorPosition1.x(), vectorPosition1.y(), vectorPosition1.z()),
                    new Pos(vectorPosition2.x(), vectorPosition2.y(), vectorPosition2.z()),
                    regionType,
                    HypixelConst.getTypeLoader().getType()).save();
        }, addRegion);
    }
}
