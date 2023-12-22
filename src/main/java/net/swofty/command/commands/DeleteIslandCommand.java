package net.swofty.command.commands;

import com.mongodb.client.model.Filters;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.data.mongodb.IslandDatabase;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

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
            } catch (Exception e) {}
            IslandDatabase.collection.deleteMany(Filters.eq("_id", ((SkyBlockPlayer) sender).getUuid().toString()));

            sender.sendMessage("§aYou have successfully deleted your island.");
        });
    }
}
