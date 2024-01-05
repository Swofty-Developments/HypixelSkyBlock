package net.swofty.command.commands;

import com.mongodb.client.model.Filters;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointUUID;
import net.swofty.data.mongodb.IslandDatabase;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

import java.util.UUID;

@CommandParameters(aliases = "removeisland",
        description = "Deletes the island of the player",
        usage = "/deleteisland",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class DeleteIslandCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            try {
                ((SkyBlockPlayer) sender).sendToHub();
                ((SkyBlockPlayer) sender).getSkyBlockIsland().runVacantCheck();
            } catch (Exception e) {
            }
            IslandDatabase.collection.deleteMany(Filters.eq("_id", ((SkyBlockPlayer) sender).getProfiles().getCurrentlySelected()));
            UUID newIslandUUID = UUID.randomUUID();
            ((SkyBlockPlayer) sender).getDataHandler().get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(newIslandUUID);

            sender.sendMessage("§aYou have successfully deleted your island.");
            sender.sendMessage("§eNew Island UUID: §b" + newIslandUUID + "§e.");
        });
    }
}
