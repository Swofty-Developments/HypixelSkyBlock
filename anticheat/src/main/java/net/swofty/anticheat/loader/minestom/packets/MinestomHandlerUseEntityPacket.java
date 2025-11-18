package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.entity.Player;
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
        UseEntityPacket.Type type = mapType(packet.type());
        UseEntityPacket.Hand hand = packet.hand() == Player.Hand.MAIN ?
                UseEntityPacket.Hand.MAIN_HAND : UseEntityPacket.Hand.OFF_HAND;

        Pos targetPos = null;
        if (packet.type() == ClientInteractEntityPacket.Type.INTERACT_AT && packet.targetPosition() != null) {
            targetPos = new Pos(packet.targetPosition().x(), packet.targetPosition().y(), packet.targetPosition().z());
        }

        return new UseEntityPacket(uuid, packet.targetId(), type, targetPos, hand, packet.playerSneaking());
    }

    @Override
    public ClientInteractEntityPacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return null;
    }

    @Override
    public Class<ClientInteractEntityPacket> getHandledPacketClass() {
        return ClientInteractEntityPacket.class;
    }

    private UseEntityPacket.Type mapType(ClientInteractEntityPacket.Type type) {
        return switch (type) {
            case INTERACT -> UseEntityPacket.Type.INTERACT;
            case ATTACK -> UseEntityPacket.Type.ATTACK;
            case INTERACT_AT -> UseEntityPacket.Type.INTERACT_AT;
        };
    }
}
