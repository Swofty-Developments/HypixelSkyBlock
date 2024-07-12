package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientPlayerPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.packet.IsOnGroundPacket;
import net.swofty.anticheat.event.packet.RotationPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerClientPacket
        extends LoaderPacketHandler<ClientPlayerPacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, ClientPlayerPacket packet) {
        return new IsOnGroundPacket(
                SwoftyPlayer.players.get(uuid),
                packet.onGround()
        );
    }

    @Override
    public ClientPlayerPacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return new ClientPlayerPacket(
                ((IsOnGroundPacket) packet).isOnGround()
        );
    }

    @Override
    public Class<ClientPlayerPacket> getHandledPacketClass() {
        return ClientPlayerPacket.class;
    }
}
