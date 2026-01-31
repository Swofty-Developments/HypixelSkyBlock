package net.swofty.type.replayviewer.command;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;

@CommandParameters(
        description = "Sets replay playback speed",
        usage = "/speed <speed>",
        permission = Rank.DEFAULT,
        allowsConsole = false
)
public class SpeedCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        var speedArg = ArgumentType.Float("speed");

        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            float speed = context.get(speedArg);

            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    session -> session.setSpeed(speed),
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, speedArg);

        // Shortcuts
        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    session -> session.setSpeed(0.5f),
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, ArgumentType.Literal("slow"));

        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    session -> session.setSpeed(1.0f),
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, ArgumentType.Literal("normal"));

        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
                    session -> session.setSpeed(2.0f),
                    () -> player.sendMessage("§cNo active replay session.")
            );
        }, ArgumentType.Literal("fast"));
    }
}
