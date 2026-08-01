package gg.itzkatze.thehypixelrecreationmod.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import gg.itzkatze.thehypixelrecreationmod.features.worldexport.ChunkExportRecorder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.network.chat.Component;

public final class ChunkExporterCommand {
    private ChunkExporterCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommands.literal("chunkexporter")
                        .then(ClientCommands.literal("start")
                                .executes(context -> {
                                    try {
                                        ChunkExportRecorder.StartResult result = ChunkExportRecorder.start();
                                        context.getSource().sendFeedback(Component.literal(
                                                "§aChunk export session started. Captured "
                                                        + result.initialChunkCount()
                                                        + " loaded chunks in "
                                                        + result.dimension()
                                        ));
                                        return 1;
                                    } catch (Exception exception) {
                                        context.getSource().sendFeedback(Component.literal("§cFailed to start chunk export session: " + exception.getMessage()));
                                        return 0;
                                    }
                                }))
                        .then(ClientCommands.literal("stop")
                                .then(ClientCommands.argument("name", StringArgumentType.string())
                                        .executes(context -> {
                                            String name = StringArgumentType.getString(context, "name");
                                            try {
                                                ChunkExportRecorder.StopResult result = ChunkExportRecorder.stop(name);
                                                context.getSource().sendFeedback(Component.literal(
                                                        "§aSaved chunk export session '"
                                                                + result.sessionName()
                                                                + "' with "
                                                                + result.chunkCount()
                                                                + " chunks, "
                                                                + result.sectionCount()
                                                                + " sections, and "
                                                                + result.blockEntityCount()
                                                                + " block entities to "
                                                                + result.path().getFileName()
                                                                + " and "
                                                                + result.polarPath().getFileName()
                                                                + " with "
                                                                + result.customBiomeCount()
                                                                + " custom biome definitions"
                                                ));
                                                return 1;
                                            } catch (Exception exception) {
                                                context.getSource().sendFeedback(Component.literal("§cFailed to stop chunk export session: " + exception.getMessage()));
                                                return 0;
                                            }
                                        })))
                        .then(ClientCommands.literal("status")
                                .executes(context -> {
                                    if (!ChunkExportRecorder.isActive()) {
                                        context.getSource().sendFeedback(Component.literal("§eNo chunk export session is active."));
                                        return 1;
                                    }

                                    ChunkExportRecorder.Status status = ChunkExportRecorder.getStatus();
                                    context.getSource().sendFeedback(Component.literal(
                                            "§eChunk export session active in "
                                                    + status.dimension()
                                                    + ": "
                                                    + status.chunkCount()
                                                    + " captured chunks"
                                    ));
                                    return 1;
                                }))));
    }
}
