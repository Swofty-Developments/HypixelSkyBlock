package net.swofty.command.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.item.ItemType;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.updater.NonPlayerItemUpdater;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

@CommandParameters(aliases = "cl",
        description = "Rid your inventory of useless items",
        usage = "/clear",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class ClearCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.getInventory().clear();
            MinecraftServer.getSchedulerManager().scheduleTask(() -> player.getInventory().setItemStack(8,
                    new NonPlayerItemUpdater(new SkyBlockItem(ItemType.SKYBLOCK_MENU).getItemStack())
                            .getUpdatedItem().build()), TaskSchedule.tick(1), TaskSchedule.stop());
            player.sendMessage("§aWhoosh!");
        });
    }
}
