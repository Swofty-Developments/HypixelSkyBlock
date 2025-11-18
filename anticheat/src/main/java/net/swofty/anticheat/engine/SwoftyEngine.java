package net.swofty.anticheat.engine;

import net.swofty.anticheat.api.AnticheatAPI;
import net.swofty.anticheat.event.SwoftyEventHandler;
import net.swofty.anticheat.flag.FlagType;
import net.swofty.anticheat.loader.Loader;
import net.swofty.anticheat.loader.SwoftyAnticheat;
import net.swofty.anticheat.loader.managers.SwoftySchedulerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SwoftyEngine {
    public static void startSchedulers(Loader loader) {
        SwoftySchedulerManager scheduler = loader.getSchedulerManager();
        scheduler.scheduleRepeatingTask(() -> {
            List<UUID> onlinePlayers = loader.getOnlinePlayers();
            List<SwoftyPlayer> players = new ArrayList<>();

            synchronized (SwoftyPlayer.players) {
                SwoftyPlayer.players.forEach((uuid, player) -> {
                    if (!onlinePlayers.contains(uuid)) {
                        player.getWorld().shutdown();
                        SwoftyPlayer.players.remove(uuid);
                        // Clean up player data when they disconnect
                        AnticheatAPI.clearPlayerData(uuid);
                        AnticheatAPI.clearBypasses(uuid);
                    }
                });
            }

            onlinePlayers.forEach(uuid -> {
                if (!SwoftyPlayer.players.containsKey(uuid)) {
                    players.add(new SwoftyPlayer(uuid));
                } else {
                    players.add(SwoftyPlayer.players.get(uuid));
                }
            });

            players.forEach((player -> {
                player.moveTickOn();
                player.sendPingRequest();
                if (player.ticksSinceLastPingResponse() > SwoftyAnticheat.getValues().getTicksAllowedToMissPing()) {
                    player.flag(FlagType.TIMEOUT_PING_PACKETS, 100);
                }
            }));
        }, 1, 1);

        // Cleanup expired bypasses every 5 seconds (100 ticks)
        scheduler.scheduleRepeatingTask(() -> {
            AnticheatAPI.getBypassManager().cleanupExpired();
        }, 100, 100);
    }

    public static void registerEvents() {
        SwoftyEventHandler.registerEventMethods(new MovementEvents());
        SwoftyEventHandler.registerEventMethods(new PingEvents());

        Arrays.stream(FlagType.values()).forEach(flagType -> {
            SwoftyEventHandler.registerEventMethods(flagType.getFlagSupplier().get());
        });
    }
}
