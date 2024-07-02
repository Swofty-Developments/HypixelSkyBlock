package net.swofty.anticheat.loader;

import net.swofty.anticheat.loader.managers.SwoftyPlayerManager;
import net.swofty.anticheat.loader.managers.SwoftySchedulerManager;

import java.util.List;
import java.util.UUID;

public abstract class Loader {
    public abstract SwoftySchedulerManager getSchedulerManager();
    public abstract SwoftyPlayerManager getPlayerManager(UUID uuid);
    public abstract List<UUID> getOnlinePlayers();
}
