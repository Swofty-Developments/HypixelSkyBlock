package net.swofty.types.generic.command.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "cl",
        description = "Rid your inventory of useless items",
        usage = "/clear",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class ClearCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.getInventory().clear();
            MinecraftServer.getSchedulerManager().scheduleTask(() -> player.getInventory().setItemStack(8,
                    new NonPlayerItemUpdater(new SkyBlockItem(ItemType.SKYBLOCK_MENU).getItemStack())
                            .getUpdatedItem().build()), TaskSchedule.tick(1), TaskSchedule.stop());
            player.sendMessage("§aWhoosh!");
        });
    }
}
