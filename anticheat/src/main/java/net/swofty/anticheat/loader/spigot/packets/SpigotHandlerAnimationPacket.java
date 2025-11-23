package net.swofty.anticheat.loader.spigot.packets;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.swofty.anticheat.event.packet.AnimationPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class SpigotHandlerAnimationPacket extends LoaderPacketHandler {

    @Override
    public Class<? extends SwoftyPacket> getHandledPacketClass() {
        return AnimationPacket.class;
    }

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, Object loaderPacket) {
        if (!(loaderPacket instanceof PacketContainer packet)) return null;

        EnumWrappers.Hand hand = packet.getHands().size() > 0 ?
                packet.getHands().read(0) : EnumWrappers.Hand.MAIN_HAND;

        AnimationPacket.Hand handType = hand == EnumWrappers.Hand.MAIN_HAND ?
                AnimationPacket.Hand.MAIN_HAND : AnimationPacket.Hand.OFF_HAND;

        return new AnimationPacket(uuid, handType);
    }

    @Override
    public Object buildLoaderPacket(UUID uuid, SwoftyPacket swoftyPacket) {
        return null;
    }
}
