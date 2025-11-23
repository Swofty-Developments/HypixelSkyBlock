package net.swofty.anticheat.loader.spigot.packets;

import com.comphenix.protocol.events.PacketContainer;
import net.swofty.anticheat.event.packet.EntityActionPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class SpigotHandlerEntityActionPacket extends LoaderPacketHandler {

    @Override
    public Class<? extends SwoftyPacket> getHandledPacketClass() {
        return EntityActionPacket.class;
    }

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, Object loaderPacket) {
        if (!(loaderPacket instanceof PacketContainer packet)) return null;

        int actionId = packet.getIntegers().read(1);
        int jumpBoost = packet.getIntegers().read(2);

        EntityActionPacket.Action action = mapAction(actionId);
        return new EntityActionPacket(uuid, action, jumpBoost);
    }

    @Override
    public Object buildLoaderPacket(UUID uuid, SwoftyPacket swoftyPacket) {
        return null;
    }

    private EntityActionPacket.Action mapAction(int actionId) {
        return switch (actionId) {
            case 0 -> EntityActionPacket.Action.START_SNEAKING;
            case 1 -> EntityActionPacket.Action.STOP_SNEAKING;
            case 2 -> EntityActionPacket.Action.LEAVE_BED;
            case 3 -> EntityActionPacket.Action.START_SPRINTING;
            case 4 -> EntityActionPacket.Action.STOP_SPRINTING;
            case 5 -> EntityActionPacket.Action.START_JUMP_WITH_HORSE;
            case 6 -> EntityActionPacket.Action.STOP_JUMP_WITH_HORSE;
            case 7 -> EntityActionPacket.Action.OPEN_HORSE_INVENTORY;
            case 8 -> EntityActionPacket.Action.START_FLYING_WITH_ELYTRA;
            default -> EntityActionPacket.Action.STOP_SNEAKING;
        };
    }
}
