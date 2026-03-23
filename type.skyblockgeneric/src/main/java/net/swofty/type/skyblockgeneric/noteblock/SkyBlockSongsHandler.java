package net.swofty.type.skyblockgeneric.noteblock;

import lombok.Getter;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record SkyBlockSongsHandler(SkyBlockPlayer player) {
    private static final Map<Player, PlayerSong> PLAYER_SONGS = new ConcurrentHashMap<>();
    @Getter
    public static boolean isEnabled;

    public void setPlayerSong(SkyBlockSong song) {
        if (!isEnabled) throw new IllegalStateException("SkyBlock songs are not enabled");
        stopPlayerSong();

        int songLength = song.getLength();
        Map<Integer, Sound[]> ticks = song.getTicks();
        var tempoEvents = song.getTempoEvents();
        float baseTps = Math.max(0.01f, song.getTps());
        boolean loops = song.isLoop();
        int loopStart = song.getLoopStartTick();

        Task task = MinecraftServer.getSchedulerManager().buildTask(new Runnable() {
            int songTick = 0;
            float currentTps = baseTps;
            double tickAccumulator = 0.0;

            @Override
            public void run() {
                if (songTick > songLength) {
                    if (loops) {
                        songTick = loopStart;
                        tickAccumulator = 0.0;
                        var floorTempo = tempoEvents.floorEntry(songTick);
                        currentTps = floorTempo != null ? floorTempo.getValue() : baseTps;
                    } else {
                        PlayerSong ps = PLAYER_SONGS.remove(player);
                        if (ps != null) ps.task().cancel();
                        return;
                    }
                }

                tickAccumulator += currentTps / 20.0;
                int safety = 0;
                while (tickAccumulator >= 1.0 && safety++ < 100) {
                    Float tempoChange = tempoEvents.get(songTick);
                    if (tempoChange != null && tempoChange > 0f) {
                        currentTps = tempoChange;
                    }

                    Sound[] sounds = ticks.get(songTick);
                    if (sounds != null) {
                        for (Sound sound : sounds) {
                            player.playSound(sound, Sound.Emitter.self());
                        }
                    }

                    songTick++;
                    tickAccumulator -= 1.0;
                    if (songTick > songLength) {
                        break;
                    }
                }
            }
        }).delay(TaskSchedule.immediate()).repeat(TaskSchedule.tick(1)).schedule();

        PLAYER_SONGS.put(player, new PlayerSong(song, task));
    }

    public void stopPlayerSong() {
        PlayerSong existing = PLAYER_SONGS.remove(player);
        if (existing != null) existing.task().cancel();
    }

    public @Nullable PlayerSong getPlayerSong() {
        if (!isEnabled) throw new IllegalStateException("SkyBlock songs are not enabled");
        return PLAYER_SONGS.get(player);
    }

    public record PlayerSong(SkyBlockSong song, Task task) {
    }
}
