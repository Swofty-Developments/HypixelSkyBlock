package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.swofty.commons.Songs;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.noteblock.SkyBlockSong;
import net.swofty.type.skyblockgeneric.noteblock.SkyBlockSongsHandler;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "playsong",
        description = "Plays a song",
        usage = "/playmusic <song>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class PlayMusicCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
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
