package net.swofty.anticheat.loader.spigot.packets;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.event.packet.UseEntityPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;
import net.swofty.anticheat.math.Pos;
import org.bukkit.util.Vector;

import java.util.UUID;

public class SpigotHandlerUseEntityPacket extends LoaderPacketHandler {

    @Override
    public Class<? extends SwoftyPacket> getHandledPacketClass() {
        return UseEntityPacket.class;
    }

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, Object loaderPacket) {
        if (!(loaderPacket instanceof PacketContainer packet)) return null;

        int entityId = packet.getIntegers().read(0);
        EnumWrappers.EntityUseAction action = packet.getEntityUseActions().read(0);

        UseEntityPacket.Type type = mapType(action);
        EnumWrappers.Hand hand = packet.getHands().size() > 0 ?
                packet.getHands().read(0) : EnumWrappers.Hand.MAIN_HAND;

        UseEntityPacket.Hand handType = hand == EnumWrappers.Hand.MAIN_HAND ?
                UseEntityPacket.Hand.MAIN_HAND : UseEntityPacket.Hand.OFF_HAND;

        Pos targetPos = null;
        if (packet.getVectors().size() > 0) {
            Vector vec = packet.getVectors().read(0);
            targetPos = new Pos(vec.getX(), vec.getY(), vec.getZ());
        }

        boolean sneaking = packet.getBooleans().size() > 0 && packet.getBooleans().read(0);

        return new UseEntityPacket(uuid, entityId, type, targetPos, handType, sneaking);
    }

    @Override
    public Object buildLoaderPacket(UUID uuid, SwoftyPacket swoftyPacket) {
        return null;
    }

    private UseEntityPacket.Type mapType(EnumWrappers.EntityUseAction action) {
        return switch (action) {
            case INTERACT -> UseEntityPacket.Type.INTERACT;
            case ATTACK -> UseEntityPacket.Type.ATTACK;
            case INTERACT_AT -> UseEntityPacket.Type.INTERACT_AT;
        };
    }
}
