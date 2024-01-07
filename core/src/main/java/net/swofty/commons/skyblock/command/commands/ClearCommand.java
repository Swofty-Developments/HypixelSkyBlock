package net.swofty.commons.skyblock.command.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.item.updater.NonPlayerItemUpdater;
import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

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
