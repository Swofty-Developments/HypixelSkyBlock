package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.entity.Player;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.gui.inventories.experiments.GUIExperiments;
import net.swofty.type.skyblockgeneric.block.blocks.BlockExperimentationTable;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(description = "Opens the Experimentation Table",
        usage = "/experiment",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class ExperimentsCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
       command.setCondition((sender, commandString) -> sender instanceof Player);

       command.setDefaultExecutor((sender, context) -> {
            Player player = (Player) sender;
            SkyBlockPlayer skyBlockPlayer = SkyBlockGenericLoader.getFromUUID(player.getUuid());
            if (skyBlockPlayer != null) {
                new GUIExperiments().open(skyBlockPlayer);
            }
        });

        // Developer subcommands: clear, place
        ArgumentLiteral clear = ArgumentType.Literal("clear");
        ArgumentLiteral place = ArgumentType.Literal("place");

        // Reload subcommand removed (JSON loading no longer supported)

        command.addSyntax((sender, ctx) -> {
            Player player = (Player) sender;
            SkyBlockPlayer skyBlockPlayer = SkyBlockGenericLoader.getFromUUID(player.getUuid());
            if (skyBlockPlayer == null) return;
            BlockExperimentationTable.removeAllPlacedStructures();
            skyBlockPlayer.sendMessage("§eCleared all placed Experimentation Tables.");
        }, clear);

        command.addSyntax((sender, ctx) -> {
            Player player = (Player) sender;
            SkyBlockPlayer skyBlockPlayer = SkyBlockGenericLoader.getFromUUID(player.getUuid());
            if (skyBlockPlayer == null) return;
            var pos = skyBlockPlayer.getPosition().add(
                    Math.sin(Math.toRadians(-skyBlockPlayer.getPosition().yaw())) * 2,
                    0,
                    Math.cos(Math.toRadians(-skyBlockPlayer.getPosition().yaw())) * 2
            );
            BlockExperimentationTable.spawnStructure(skyBlockPlayer.getInstance(), pos, skyBlockPlayer);
            skyBlockPlayer.sendMessage("§aPlaced Experimentation Table with current blueprint.");
        }, place);
    }
}
