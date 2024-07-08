package net.swofty.anticheat.engine;

import javassist.tools.reflect.Reflection;
import net.swofty.anticheat.event.SwoftyEventHandler;
import net.swofty.anticheat.flag.FlagType;
import net.swofty.anticheat.loader.Loader;
import net.swofty.anticheat.loader.managers.SwoftySchedulerManager;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
                        return;
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

            players.forEach(SwoftyPlayer::moveTickOn);
        }, 1, 1);
    }

    public static void registerEvents() {
        SwoftyEventHandler.registerEventMethods(new MovementEvents());

        Arrays.stream(FlagType.values()).forEach(flagType -> {
            SwoftyEventHandler.registerEventMethods(flagType.getFlagSupplier().get());
        });
    }
}
