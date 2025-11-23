package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.network.packet.client.play.ClientAnimationPacket;
import net.swofty.anticheat.event.packet.AnimationPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerAnimationPacket
        extends LoaderPacketHandler<ClientAnimationPacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, ClientAnimationPacket packet) {
        AnimationPacket.Hand hand = packet.hand() == PlayerHand.MAIN ?
                AnimationPacket.Hand.MAIN_HAND : AnimationPacket.Hand.OFF_HAND;

        return new AnimationPacket(uuid, hand);
    }

    @Override
    public ClientAnimationPacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return null;
    }

    @Override
    public Class<ClientAnimationPacket> getHandledPacketClass() {
        return ClientAnimationPacket.class;
    }
}
