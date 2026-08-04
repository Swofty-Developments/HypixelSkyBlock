package gg.itzkatze.thehypixelrecreationmod.commands;

import com.mojang.brigadier.context.CommandContext;
import gg.itzkatze.thehypixelrecreationmod.features.packetlog.RavengardSessionLogger;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.file.Path;

public final class RavengardSessionCommand {
    private RavengardSessionCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("ravengard")
                        .then(ClientCommands.literal("start").executes(RavengardSessionCommand::start))
                        .then(ClientCommands.literal("stop").executes(RavengardSessionCommand::stop))));
    }

    private static int start(CommandContext<FabricClientCommandSource> context) {
        if (RavengardSessionLogger.isActive()) {
            context.getSource().sendFeedback(Component.literal("§cA ravengard session log is already running."));
            return 0;
        }
        try {
            Path path = RavengardSessionLogger.start();
            context.getSource().sendFeedback(Component.literal(
                    "§aRavengard session log started, auto-tracking every rig → §f" + path.getFileName()));
            return 1;
        } catch (IOException | RuntimeException exception) {
            context.getSource().sendFeedback(Component.literal(
                    "§cFailed to start ravengard session log: " + exception.getMessage()));
            return 0;
        }
    }

    private static int stop(CommandContext<FabricClientCommandSource> context) {
        if (!RavengardSessionLogger.isActive()) {
            context.getSource().sendFeedback(Component.literal("§cNo ravengard session log is running."));
            return 0;
        }
        RavengardSessionLogger.StopResult result = RavengardSessionLogger.stop();
        context.getSource().sendFeedback(Component.literal(
                "§aRavengard session log stopped after " + result.packetCount() + " packets across "
                        + result.rigCount() + " rigs → §f" + result.path()));
        return 1;
    }
}
