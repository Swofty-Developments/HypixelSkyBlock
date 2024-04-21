package net.swofty.types.generic.command.commands;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentFloat;
import net.minestom.server.entity.Player;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Plays a vanilla sound effect",
        usage = "/playvanillasound <sound> [volume] [pitch]",
        aliases = "playsound",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class PlayVanillaSoundCommand extends SkyBlockCommand {

    @Override
    public void run(MinestomCommand command) {
        ArgumentString soundsArgument = ArgumentType.String("sound");
        ArgumentFloat volumeArgument = ArgumentType.Float("volume");
        ArgumentFloat pitchArgument = ArgumentType.Float("pitch");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            String soundString = context.get(soundsArgument);

            Player player = (Player) sender;
            player.playSound(Sound.sound(Key.key(soundString), Sound.Source.PLAYER, 1f, 1f), Sound.Emitter.self());
        }, soundsArgument);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            String soundString = context.get(soundsArgument);
            float volumeFloat = context.get(volumeArgument);
            float pitchFloat = context.get(pitchArgument);

            Player player = (Player) sender;
            player.playSound(Sound.sound(Key.key(soundString), Sound.Source.PLAYER, volumeFloat, pitchFloat), Sound.Emitter.self());
        }, soundsArgument, volumeArgument, pitchArgument);
    }
}
