package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.packet.RotationPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerRotationPacket
        extends LoaderPacketHandler<ClientPlayerRotationPacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, ClientPlayerRotationPacket packet) {
        return new RotationPacket(
                SwoftyPlayer.players.get(uuid),
                packet.yaw(),
                packet.pitch(),
                packet.onGround()
        );
    }

    @Override
    public ClientPlayerRotationPacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return new ClientPlayerRotationPacket(
                ((RotationPacket) packet).getYaw(),
                ((RotationPacket) packet).getPitch(),
                (byte) (((RotationPacket) packet).isOnGround() ? 1 : 0)
        );
    }

    @Override
    public Class<ClientPlayerRotationPacket> getHandledPacketClass() {
        return ClientPlayerRotationPacket.class;
    }
}
