package net.swofty.types.generic.noteblock;

import lombok.Getter;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.user.SkyBlockPlayer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public record SkyBlockSongsHandler(SkyBlockPlayer player) {
    private static final Map<Player, PlayerSong> playerSongs = new HashMap<>();
    @Getter
    public static boolean isEnabled;

    public void setPlayerSong(SkyBlockSong song) {
        if (!isEnabled) throw new IllegalStateException("SkyBlock songs are not enabled");

        if (playerSongs.containsKey(player)) {
            playerSongs.get(player).task.cancel();
        }
        playerSongs.put(player, new PlayerSong(song,
                MinecraftServer.getSchedulerManager().submitTask(new Supplier<TaskSchedule>() {
            int tick = 0;

            @Override
            public TaskSchedule get() {
                if (tick > song.getLength() + 1) {
                    return TaskSchedule.stop();
                }

                List<Sound> sounds = song.getTicks().get(tick);
                if (sounds != null) {
                    for (Sound sound : sounds) {
                        player.playSound(sound, Sound.Emitter.self());
                    }
                }

                tick++;

                return TaskSchedule.millis((long) (1000.0 / song.getTps()));
            }
        })));
    }

    public @Nullable PlayerSong getPlayerSong() {
        if (!isEnabled) throw new IllegalStateException("SkyBlock songs are not enabled");
        return playerSongs.get(player);
    }

    public static class PlayerSong {
        private final SkyBlockSong song;
        public final Task task;

        public PlayerSong(SkyBlockSong song, Task task) {
            this.song = song;
            this.task = task;
        }
    }
}
