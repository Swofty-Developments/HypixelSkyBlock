package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientPlayerAbilitiesPacket;
import net.swofty.anticheat.event.packet.AbilitiesPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerAbilitiesPacket
        extends LoaderPacketHandler<ClientPlayerAbilitiesPacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, ClientPlayerAbilitiesPacket packet) {
        byte flags = packet.flags();
        boolean invulnerable = (flags & 0x01) != 0;
        boolean flying = (flags & 0x02) != 0;
        boolean allowFlight = (flags & 0x04) != 0;
        boolean creativeMode = (flags & 0x08) != 0;

        return new AbilitiesPacket(uuid, invulnerable, flying, allowFlight, creativeMode, 0.05f, 0.1f);
    }

    @Override
    public ClientPlayerAbilitiesPacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return null;
    }

    @Override
    public Class<ClientPlayerAbilitiesPacket> getHandledPacketClass() {
        return ClientPlayerAbilitiesPacket.class;
    }
}
