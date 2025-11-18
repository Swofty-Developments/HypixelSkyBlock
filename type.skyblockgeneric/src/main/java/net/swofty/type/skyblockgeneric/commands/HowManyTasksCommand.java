package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.MinecraftServer;
import org.tinylog.Logger;
import net.minestom.server.timer.SchedulerManager;
import org.tinylog.Logger;
import net.swofty.type.generic.command.CommandParameters;
import org.tinylog.Logger;
import net.swofty.type.generic.command.HypixelCommand;
import org.tinylog.Logger;
import net.swofty.type.generic.user.categories.Rank;
import org.tinylog.Logger;

import java.util.concurrent.atomic.AtomicInteger;
import org.tinylog.Logger;

@CommandParameters(aliases = "taskcount",
        description = "Gets the number of tasks currently running on the server",
        usage = "/taskcount",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class HowManyTasksCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SchedulerManager manager = MinecraftServer.getSchedulerManager();
            AtomicInteger TASK_COUNTER;

            // Use reflection to get the TASK_COUNTER field
            try {
                TASK_COUNTER = (AtomicInteger) manager.getClass().getDeclaredField("TASK_COUNTER").get(manager);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                Logger.error(e, "Error while counting tasks");
                return;
            }

            sender.sendMessage(TASK_COUNTER.get() + " tasks running");
        });
    }
}
