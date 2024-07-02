package net.swofty.anticheat.loader;

import net.swofty.anticheat.engine.SwoftyPlayer;

import java.util.List;
import java.util.UUID;

public interface Loader {
    SwoftySchedulerManager getSchedulerManager();
    SwoftyPlayerManager getPlayerManager(UUID uuid);
    List<UUID> getOnlinePlayers();
}
