package net.swofty.types.generic.packet;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.server.ServerPacket;

import java.util.ArrayList;

public abstract class SkyBlockPacketServerListener {
    private static final ArrayList<SkyBlockPacketServerListener> cachedEvents = new ArrayList<>();

    public abstract Class<? extends ServerPacket> getPacket();

    public abstract void run(ServerPacket packet);

    public void cacheListener() {
        cachedEvents.add(this);
    }

    public static void register(GlobalEventHandler eventHandler) {
        EventNode<Event> eventNode = EventNode.all("packet-handler-server");

        cachedEvents.forEach((packetEvent) -> {
            eventNode.addListener(PlayerPacketOutEvent.class, rawEvent -> {
                Class<?> packetType = packetEvent.getPacket();

                if (packetType.isInstance(rawEvent.getPacket())) {
                    packetEvent.run(rawEvent.getPacket());
                }
            });

            eventHandler.addChild(eventNode);
        });
    }
}

