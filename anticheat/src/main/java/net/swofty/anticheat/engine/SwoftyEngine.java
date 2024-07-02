package net.swofty.anticheat.engine;

import net.swofty.anticheat.loader.Loader;
import net.swofty.anticheat.loader.SwoftyPlayerManager;
import net.swofty.anticheat.loader.SwoftySchedulerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SwoftyEngine {
    public static void startSchedulers(Loader loader) {
        SwoftySchedulerManager scheduler = loader.getSchedulerManager();
        scheduler.scheduleRepeatingTask(() -> {
            List<UUID> onlinePlayers = loader.getOnlinePlayers();
            List<SwoftyPlayer> players = new ArrayList<>();

            SwoftyPlayer.players.forEach((uuid, player) -> {
                if (!onlinePlayers.contains(uuid)) {
                    SwoftyPlayer.players.remove(uuid);
                    return;
                }
            });

            onlinePlayers.forEach(uuid -> {
                if (!SwoftyPlayer.players.containsKey(uuid)) {
                    players.add(new SwoftyPlayer(uuid));
                } else {
                    players.add(SwoftyPlayer.players.get(uuid));
                }
            });

            players.forEach(SwoftyPlayer::moveTickOn);
        }, 1, 1);
    }

    public static void registerModules() {
        // Register all modules
    }
}
