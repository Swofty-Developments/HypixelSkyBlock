package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.chocolatefactory.HoppityHuntManager;

@CommandParameters(description = "Manages the Hoppity Hunt event",
        usage = "/hoppityhunt <start|stop>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class HoppityHuntCommand extends HypixelCommand {
    private static final String HUNT_ALREADY_ACTIVE_MESSAGE = "§cHoppity's Hunt is already active!";
    private static final String HUNT_NOT_ACTIVE_MESSAGE = "§cHoppity's Hunt is not currently active!";
    private static final String HUNT_STARTED_MESSAGE = "§aHoppity's Hunt has been started! §e17 eggs §ahave been spawned.";
    private static final String HUNT_STOPPED_MESSAGE = "§aHoppity's Hunt has been stopped! All eggs have been removed.";

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HoppityHuntManager manager = HoppityHuntManager.getInstance();
            if (manager.isActive()) {
                sender.sendMessage(HUNT_ALREADY_ACTIVE_MESSAGE);
                return;
            }

            manager.startHunt();
            sender.sendMessage(HUNT_STARTED_MESSAGE);
        }, ArgumentType.Literal("start"));

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HoppityHuntManager manager = HoppityHuntManager.getInstance();
            if (!manager.isActive()) {
                sender.sendMessage(HUNT_NOT_ACTIVE_MESSAGE);
                return;
            }

            manager.stopHunt();
            sender.sendMessage(HUNT_STOPPED_MESSAGE);
        }, ArgumentType.Literal("stop"));
    }
}
