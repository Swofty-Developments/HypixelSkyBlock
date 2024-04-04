package net.swofty.types.generic.command.commands;

import com.mongodb.client.model.Filters;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.data.mongodb.UserDatabase;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "cooperativeleave",
        description = "Leaves the current coop",
        usage = "/coopleave",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class CoopLeaveCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            if (!player.isCoop()) {
                player.sendMessage("§b[Co-op] §cYou are not on a coop profile!");
                return;
            }

            if (player.getProfiles().getProfiles().size() == 1) {
                player.sendMessage("§b[Co-op] §cYou cannot leave your last profile!");
                player.sendMessage("§b[Co-op] §eMake another profile before deleting this one.");
                return;
            }

            player.kick("§cYou must reconnect for this change to take effect");

            CoopDatabase.Coop coop = CoopDatabase.getFromMember(player.getUuid());
            coop.members().remove(player.getUuid());
            coop.memberProfiles().remove(player.getProfiles().getCurrentlySelected());
            coop.save();

            MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                ProfilesDatabase.collection.deleteOne(Filters.eq("_id", player.getProfiles().getCurrentlySelected().toString()));

                player.getProfiles().removeProfile(player.getProfiles().getCurrentlySelected());
                player.getProfiles().setCurrentlySelected(player.getProfiles().getProfiles().get(0));
                new UserDatabase(player.getUuid()).saveProfiles(player.getProfiles());
            }, TaskSchedule.tick(4), TaskSchedule.stop());
        });
    }
}
