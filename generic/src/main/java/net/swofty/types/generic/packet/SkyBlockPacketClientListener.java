package net.swofty.types.generic.packet;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SkyBlockPacketClientListener {
    private static final ArrayList<SkyBlockPacketClientListener> cachedEvents = new ArrayList<>();

    public abstract Class<? extends ClientPacket> getPacket();

    public abstract void run(PlayerPacketEvent event, ClientPacket packet, SkyBlockPlayer player);

    public abstract boolean overrideMinestomProcessing();

    public void cacheListener() {
        cachedEvents.add(this);
    }

    public static void register(GlobalEventHandler eventHandler) {
        EventNode<Event> eventNode = EventNode.all("packet-handler-client");

        Map<Class<? extends ClientPacket>, List<SkyBlockPacketClientListener>> toOverride = new HashMap<>();
        cachedEvents.forEach((packetEvent) -> {
            if (!packetEvent.overrideMinestomProcessing()) return;

            Class<? extends ClientPacket> packetType = packetEvent.getPacket();

            if (!toOverride.containsKey(packetType)) {
                toOverride.put(packetType, new ArrayList<>());
            }

            toOverride.get(packetType).add(packetEvent);
        });
        toOverride.forEach((packetType, packetEvents) -> {
            MinecraftServer.getPacketListenerManager().setPlayListener(packetType, (packet, player) -> {
                packetEvents.forEach((packetEvent) -> {
                    packetEvent.run(null, packet, (SkyBlockPlayer) player);
                });
            });
        });

        eventNode.addListener(PlayerPacketEvent.class, rawEvent -> {
            cachedEvents.forEach((packetEvent) -> {
                if (packetEvent.overrideMinestomProcessing()) return;
                Class<?> packetType = packetEvent.getPacket();

                if (packetType.isInstance(rawEvent.getPacket())) {
                    if (SkyBlockGenericLoader.getFromUUID(rawEvent.getPlayer().getUuid()) == null) return;
                    if (SkyBlockConst.isIslandServer() &&
                            !((SkyBlockPlayer) rawEvent.getPlayer()).getSkyBlockIsland().getCreated()) return;

                    packetEvent.run(rawEvent, rawEvent.getPacket(), (SkyBlockPlayer) rawEvent.getPlayer());
                }
            });
        });
        eventHandler.addChild(eventNode);
    }
}
