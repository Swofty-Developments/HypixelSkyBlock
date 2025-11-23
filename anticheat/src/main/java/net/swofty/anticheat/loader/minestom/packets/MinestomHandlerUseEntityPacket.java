package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.entity.PlayerHand;
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
        ClientInteractEntityPacket.Type type = packet.type();

        UseEntityPacket.Type swoftyType;
        UseEntityPacket.Hand hand = null;
        Pos targetPos = null;

        switch (type) {
            case ClientInteractEntityPacket.Interact(PlayerHand playerHand) -> {
                swoftyType = UseEntityPacket.Type.INTERACT;
                hand = playerHand == PlayerHand.MAIN
                        ? UseEntityPacket.Hand.MAIN_HAND
                        : UseEntityPacket.Hand.OFF_HAND;
            }
            case ClientInteractEntityPacket.Attack attack -> swoftyType = UseEntityPacket.Type.ATTACK;
            case ClientInteractEntityPacket.InteractAt interactAt -> {
                swoftyType = UseEntityPacket.Type.INTERACT_AT;

                targetPos = new Pos(
                        interactAt.targetX(),
                        interactAt.targetY(),
                        interactAt.targetZ()
                );

                hand = interactAt.hand() == PlayerHand.MAIN
                        ? UseEntityPacket.Hand.MAIN_HAND
                        : UseEntityPacket.Hand.OFF_HAND;
            }
            default -> throw new IllegalStateException("Unknown interact type: " + type.getClass());
        }

        return new UseEntityPacket(
                uuid,
                packet.targetId(),
                swoftyType,
                targetPos,
                hand,
                packet.sneaking()
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
