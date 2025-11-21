package net.swofty.anticheat.loader;

import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.managers.SwoftySchedulerManager;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class Loader {
    private final Map<String, LoaderPacketHandler> packetHandlerMap = new HashMap<>();

    public abstract SwoftySchedulerManager getSchedulerManager();
    public abstract List<UUID> getOnlinePlayers();
    public abstract void onInitialize();
    public abstract void sendPacket(UUID uuid, SwoftyPacket packet);
    public abstract void sendMessage(UUID uuid, String message);

    public void registerPacketHandler(Class<? extends SwoftyPacket> clazz,
                                      LoaderPacketHandler packetHandler) {
        packetHandlerMap.put(clazz.getSimpleName(), packetHandler);
    }

    public @Nullable LoaderPacketHandler getPacketHandler(Class clazz) {
        for (Map.Entry<String, LoaderPacketHandler> entry : packetHandlerMap.entrySet()) {
            if (entry.getValue().getHandledPacketClass().equals(clazz)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public @Nullable LoaderPacketHandler getPacketHandler(SwoftyPacket packet) {
        return packetHandlerMap.get(packet.getClass().getSimpleName());
    }
}
