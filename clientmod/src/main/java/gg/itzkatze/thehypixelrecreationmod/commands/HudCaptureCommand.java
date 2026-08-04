package gg.itzkatze.thehypixelrecreationmod.commands;

import com.mojang.brigadier.context.CommandContext;
import gg.itzkatze.thehypixelrecreationmod.features.hudcapture.GlyphAtlas;
import gg.itzkatze.thehypixelrecreationmod.features.hudcapture.HudCaptureRecorder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

public final class HudCaptureCommand {
    private HudCaptureCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                RecreationCommand.register(dispatcher, ClientCommands.literal("hudcapture")
                        .then(ClientCommands.literal("start")
                                .executes(context -> start(context, true))
                                .then(ClientCommands.literal("all")
                                        .executes(context -> start(context, false))))
                        .then(ClientCommands.literal("stop")
                                .executes(HudCaptureCommand::stop))
                        .then(ClientCommands.literal("glyphs")
                                .executes(context -> {
                                    GlyphAtlas.reload();
                                    context.getSource().sendFeedback(Component.literal(
                                            "§aGlyph atlas reloaded: " + GlyphAtlas.size() + " bitmap glyphs"));
                                    return 1;
                                }))));
    }

    private static int start(CommandContext<FabricClientCommandSource> context, boolean dedupe) {
        try {
            HudCaptureRecorder.StartResult result = HudCaptureRecorder.start(dedupe);
            context.getSource().sendFeedback(Component.literal(
                    "§aHUD capture started ("
                            + result.glyphCount()
                            + " glyphs resolved, "
                            + (dedupe ? "unique payloads only" : "every packet")
                            + ") → §f"
                            + result.path()));
            return 1;
        } catch (Exception exception) {
            context.getSource().sendFeedback(Component.literal("§cFailed to start HUD capture: " + exception.getMessage()));
            return 0;
        }
    }

    private static int stop(CommandContext<FabricClientCommandSource> context) {
        try {
            HudCaptureRecorder.StopResult result = HudCaptureRecorder.stop();
            context.getSource().sendFeedback(Component.literal(
                    "§aHUD capture stopped: "
                            + result.eventCount()
                            + " events, "
                            + result.mapDumpCount()
                            + " map images → §f"
                            + result.directory()));
            return 1;
        } catch (Exception exception) {
            context.getSource().sendFeedback(Component.literal("§cFailed to stop HUD capture: " + exception.getMessage()));
            return 0;
        }
    }
}
