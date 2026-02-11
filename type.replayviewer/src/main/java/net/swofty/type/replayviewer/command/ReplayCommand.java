package net.swofty.type.replayviewer.command;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.playback.ReplaySession;

@CommandParameters(
        description = "Controls replay playback",
        usage = "/viewer <play|pause|speed|skip|goto|restart|leave>",
        permission = Rank.DEFAULT,
        allowsConsole = false,
        aliases = "viewer"
)
public class ReplayCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        var valueArg = ArgumentType.String("value");

        // /viewer play
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    ReplaySession::play,
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, ArgumentType.Literal("play"));

        // /viewer pause
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    ReplaySession::pause,
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, ArgumentType.Literal("pause"));

        // /viewer speed <value>
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            String value = context.get(valueArg);
            try {
                float speed = Float.parseFloat(value);
                TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                        session -> session.setPlaybackSpeed(speed),
                        () -> player.sendMessage("§cNo active replay session.")
                );
            } catch (NumberFormatException e) {
                player.sendMessage("§cInvalid speed. Use a number like 0.5, 1, 2, etc.");
            }
        }, ArgumentType.Literal("speed"), valueArg);

        // /viewer skip <seconds>
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            String value = context.get(valueArg);
            try {
                int seconds = Integer.parseInt(value);
                TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                        session -> {
                            if (seconds > 0) {
                                session.skipForward(seconds);
                            } else {
                                session.skipBackward(-seconds);
                            }
                        },
                        () -> player.sendMessage("§cNo active replay session.")
                );
            } catch (NumberFormatException e) {
                player.sendMessage("§cInvalid seconds. Use a number like 10, -10, etc.");
            }
        }, ArgumentType.Literal("skip"), valueArg);

        // /viewer goto <tick or time>
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            String value = context.get(valueArg);
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    session -> {
                        int tick = parseTimeOrTick(value);
                        if (tick >= 0) {
                            session.seekTo(tick);
                        } else {
                            player.sendMessage("§cInvalid time. Use format like 1:30 or tick number.");
                        }
                    },
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, ArgumentType.Literal("goto"), valueArg);

        // /viewer restart
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    session -> session.seekTo(0),
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, ArgumentType.Literal("restart"));

        // /viewer leave
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.removeSession(player.getUuid());
            player.sendMessage("§aLeaving replay...");
            // Would send player back to lobby
        }, ArgumentType.Literal("leave"));

        // /viewer info
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    session -> {
                        player.sendMessage("§6§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                        player.sendMessage("§b§lReplay Info");
                        player.sendMessage("");
                        player.sendMessage("§7Map: §f" + session.getMetadata().getMapName());
                        player.sendMessage("§7Game: §f" + session.getMetadata().getGameTypeName());
                        player.sendMessage("§7Time: §e" + session.getFormattedTime() + " §7/ §e" + session.getFormattedTotalTime());
                        player.sendMessage("§7Speed: §e" + session.getPlaybackSpeed() + "x");
                        player.sendMessage("§7Status: " + (session.isPlaying() ? "§aPlaying" : "§ePaused"));
                        player.sendMessage("§7Players: §f" + session.getMetadata().getPlayers().size());
                        player.sendMessage("§7Progress: §e" + String.format("%.1f%%", session.getProgress()));
                        player.sendMessage("§6§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                    },
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, ArgumentType.Literal("info"));

        // /viewer follow [player]
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    ReplaySession::followNextPlayer,
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, ArgumentType.Literal("follow"));

        // /viewer follow next
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    ReplaySession::followNextPlayer,
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, ArgumentType.Literal("follow"), ArgumentType.Literal("next"));

        // /viewer follow prev
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    ReplaySession::followPreviousPlayer,
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, ArgumentType.Literal("follow"), ArgumentType.Literal("prev"));

        // /viewer free (stop following)
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    ReplaySession::stopFollowing,
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, ArgumentType.Literal("free"));

        // /viewer faster
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    ReplaySession::cycleSpeedUp,
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, ArgumentType.Literal("faster"));

        // /viewer slower
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    ReplaySession::cycleSpeedDown,
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, ArgumentType.Literal("slower"));

        // Default - toggle play/pause
        command.setDefaultExecutor((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    ReplaySession::togglePlayPause,
                    () -> player.sendMessage("§cNo active replay session.")
            );
        });
    }

    private int parseTimeOrTick(String value) {
        // Try parsing as mm:ss format
        if (value.contains(":")) {
            String[] parts = value.split(":");
            try {
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);
                return (minutes * 60 + seconds) * 20;
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        // Try parsing as tick number
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
