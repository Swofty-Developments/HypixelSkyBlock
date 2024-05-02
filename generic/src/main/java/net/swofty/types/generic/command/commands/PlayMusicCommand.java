package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.swofty.commons.Songs;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.noteblock.SkyBlockSong;
import net.swofty.types.generic.noteblock.SkyBlockSongsHandler;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "playsong",
        description = "Plays a song",
        usage = "/playmusic <song>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class PlayMusicCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<Songs> song = new ArgumentEnum<>("song", Songs.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            if (!SkyBlockSongsHandler.isEnabled) {
                player.sendMessage("§cSkyBlock songs are not enabled on this server.");
                return;
            }

            SkyBlockSong skyBlockSong = new SkyBlockSong(context.get(song));
            SkyBlockSongsHandler songsHandler = new SkyBlockSongsHandler(player);
            songsHandler.setPlayerSong(skyBlockSong);

            player.sendMessage("§aPlaying song §e" + skyBlockSong.getSong().name() + "§a.");
        }, song);
    }
}
