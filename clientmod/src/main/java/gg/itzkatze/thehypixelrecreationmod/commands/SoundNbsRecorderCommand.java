package gg.itzkatze.thehypixelrecreationmod.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import gg.itzkatze.thehypixelrecreationmod.features.nbs.SoundNbsRecorder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.network.chat.Component;

public final class SoundNbsRecorderCommand {
    private SoundNbsRecorderCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> dispatcher.register(
                ClientCommands.literal("nbsrecord")
                        .then(ClientCommands.literal("start")
                                .executes(context -> {
                                    try {
                                        SoundNbsRecorder.start();
                                        context.getSource().sendFeedback(Component.literal(
                                                "§aStarted recording sounds."
                                        ));
                                        return 1;
                                    } catch (Exception exception) {
                                        context.getSource().sendFeedback(Component.literal("§c" + exception.getMessage()));
                                        return 0;
                                    }
                                }))
                        .then(ClientCommands.literal("stop")
                                .then(ClientCommands.argument("name", StringArgumentType.string())
                                        .executes(context -> {
                                            try {
                                                SoundNbsRecorder.StopResult result = SoundNbsRecorder.stop(
                                                        StringArgumentType.getString(context, "name")
                                                );
                                                context.getSource().sendFeedback(Component.literal(
                                                        "§aSaved " + result.noteCount() + " notes and "
                                                                + result.customInstrumentCount() + " custom instruments to "
                                                                + result.path() + ". Ignored " + result.ignoredSoundCount() + " sounds."
                                                ));
                                                return 1;
                                            } catch (Exception exception) {
                                                context.getSource().sendFeedback(Component.literal(
                                                        "§cFailed to save NBS recording: " + exception.getMessage()
                                                ));
                                                return 0;
                                            }
                                        })))
                        .then(ClientCommands.literal("status")
                                .executes(context -> {
                                    if (!SoundNbsRecorder.isActive()) {
                                        context.getSource().sendFeedback(Component.literal("§eNo NBS recording is active."));
                                        return 1;
                                    }
                                    SoundNbsRecorder.Status status = SoundNbsRecorder.getStatus();
                                    context.getSource().sendFeedback(Component.literal(
                                            "§eRecording: " + status.noteCount() + " notes, "
                                                    + status.customInstrumentCount() + " custom instruments, "
                                                    + status.ignoredSoundCount() + " ignored sounds."
                                    ));
                                    return 1;
                                }))
        ));
    }
}
