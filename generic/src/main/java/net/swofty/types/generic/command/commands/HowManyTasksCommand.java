package net.swofty.types.generic.command.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.SchedulerManager;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.categories.Rank;

import java.util.concurrent.atomic.AtomicInteger;

@CommandParameters(aliases = "taskcount",
        description = "Gets the number of tasks currently running on the server",
        usage = "/taskcount",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class HowManyTasksCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SchedulerManager manager = MinecraftServer.getSchedulerManager();
            AtomicInteger TASK_COUNTER;

            // Use reflection to get the TASK_COUNTER field
            try {
                TASK_COUNTER = (AtomicInteger) manager.getClass().getDeclaredField("TASK_COUNTER").get(manager);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return;
            }

            sender.sendMessage(TASK_COUNTER.get() + " tasks running");
        });
    }
}
