package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientInteractEntityPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.event.packet.UseEntityPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;
import net.swofty.anticheat.math.Pos;

import java.util.UUID;

public class MinestomHandlerUseEntityPacket
        extends LoaderPacketHandler<ClientInteractEntityPacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, ClientInteractEntityPacket packet) {
        return new UseEntityPacket(
                uuid,
                packet.targetId(),
            Pos.fromVec(packet.location()),
            packet.hand()
        );
    }

    @Override
    public ClientInteractEntityPacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return null;
    }

    @Override
    public Class<ClientInteractEntityPacket> getHandledPacketClass() {
        return ClientInteractEntityPacket.class;
    }
}
