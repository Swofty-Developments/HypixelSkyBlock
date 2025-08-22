package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.entity.Player;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.gui.inventories.experiments.GUIExperiments;
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
    }
}
