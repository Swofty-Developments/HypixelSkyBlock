package net.swofty.types.generic.command.commands;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.utility.MathUtility;

import java.util.List;

@CommandParameters(aliases = "minionrotation",
        description = "Tests minion rotation",
        usage = "/minionrotation",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class TestMinionRotation extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            Pos originalPos = player.getPosition();

            List<Pos> positions = MathUtility.lookSteps(originalPos, originalPos.add(2, 2, 2), 5);

            MathUtility.getCircleAroundPos(originalPos, 5, 40).forEach(pos -> {
                player.getInstance().setBlock(pos, Block.EMERALD_BLOCK);
            });

            positions.forEach(pos -> {
                player.getInstance().setBlock(pos, Block.DIAMOND_BLOCK);
            });


            player.getInstance().setBlock(
                    player.getPosition().add(player.getPosition().withPitch(0)
                            .direction().normalize().mul(3).add(0, 1, 0)),
                    Block.GOLD_BLOCK);
        });
    }
}
