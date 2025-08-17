package net.swofty.type.generic.packet;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class HypixelPacketClientListener {
    private static final ArrayList<HypixelPacketClientListener> cachedEvents = new ArrayList<>();

    public abstract Class<? extends ClientPacket> getPacket();

    public abstract void run(PlayerPacketEvent event, ClientPacket packet, HypixelPlayer player);

    public abstract boolean overrideMinestomProcessing();

    public void cacheListener() {
        cachedEvents.add(this);
    }

    public static void register(GlobalEventHandler eventHandler) {
        EventNode<Event> eventNode = EventNode.all("packet-handler-client");

        Map<Class<? extends ClientPacket>, List<HypixelPacketClientListener>> toOverride = new HashMap<>();
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
                    packetEvent.run(null, packet, (HypixelPlayer) player);
                });
            });
        });

        eventNode.addListener(PlayerPacketEvent.class, rawEvent -> {
            cachedEvents.forEach((packetEvent) -> {
                if (packetEvent.overrideMinestomProcessing()) return;
                Class<?> packetType = packetEvent.getPacket();

                if (packetType.isInstance(rawEvent.getPacket())) {
                    if (HypixelGenericLoader.getFromUUID(rawEvent.getPlayer().getUuid()) == null) return;

                    packetEvent.run(rawEvent, rawEvent.getPacket(), (HypixelPlayer) rawEvent.getPlayer());
                }
            });
        });
        eventHandler.addChild(eventNode);
    }
}
