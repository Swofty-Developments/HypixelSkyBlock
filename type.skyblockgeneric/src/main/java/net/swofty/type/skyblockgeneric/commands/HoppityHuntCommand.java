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
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HoppityHuntManager manager = HoppityHuntManager.getInstance();
            if (manager.isActive()) {
                sender.sendMessage("§cHoppity's Hunt is already active!");
                return;
            }

            manager.startHunt();
            sender.sendMessage("§aHoppity's Hunt has been started! §e17 eggs §ahave been spawned.");
        }, ArgumentType.Literal("start"));

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HoppityHuntManager manager = HoppityHuntManager.getInstance();
            if (!manager.isActive()) {
                sender.sendMessage("§cHoppity's Hunt is not currently active!");
                return;
            }

            manager.stopHunt();
            sender.sendMessage("§aHoppity's Hunt has been stopped! All eggs have been removed.");
        }, ArgumentType.Literal("stop"));
    }
}
