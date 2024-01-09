package net.swofty.types.generic.server.eventcaller;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomEventCaller {
    private static final Map<SkyBlockPlayer, PlayerValues> playerValuesCache = new HashMap<>();
    private static ServerValues serverValuesCache = null;

    public static void start() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();

        // Per Player Caller
        scheduler.submitTask(() -> {
            for (SkyBlockPlayer player : SkyBlockGenericLoader.getLoadedPlayers()) {
                if (!playerValuesCache.containsKey(player)) {
                    playerValuesCache.put(player, new PlayerValues(player, PlayerValues.Value.getValues()));
                    return TaskSchedule.seconds(1);
                }

                PlayerValues newValues = new PlayerValues(player, PlayerValues.Value.getValues());
                List<PlayerValues.Value> differentValues = playerValuesCache.get(player).getDifferentValues(newValues);

                differentValues.forEach(playerValue -> {
                    playerValue.getConsumer().accept(player, safeEntry(
                            playerValuesCache.get(player).getValue(playerValue),
                            newValues.getValue(playerValue)
                    ));
                });

                playerValuesCache.put(player, newValues);
            }

            return TaskSchedule.seconds(1);
        });

        // Server Caller
        scheduler.submitTask(() -> {
            if (serverValuesCache == null) {
                serverValuesCache = new ServerValues(ServerValues.Value.getValues());
                return TaskSchedule.seconds(1);
            }

            ServerValues newValues = new ServerValues(ServerValues.Value.getValues());
            List<ServerValues.Value> differentValues = serverValuesCache.getDifferentValues(newValues);

            differentValues.forEach(serverValue -> {
                if (serverValue.getShouldCall().apply(serverValuesCache.getValue(serverValue), newValues.getValue(serverValue))) {
                    serverValue.getConsumer().accept(newValues.getValue(serverValue));
                }
            });

            serverValuesCache = newValues;
            return TaskSchedule.seconds(1);
        });
    }

    public static void clearCache(SkyBlockPlayer player) {
        playerValuesCache.remove(player);
    }

    public static <K, V> Map.Entry<K, V> safeEntry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }
}
