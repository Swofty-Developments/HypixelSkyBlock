package net.swofty.anticheat.loader.spigot.packets;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.swofty.anticheat.event.packet.BlockPlacePacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;
import net.swofty.anticheat.math.Pos;

import java.util.UUID;

public class SpigotHandlerBlockPlacePacket extends LoaderPacketHandler {

    @Override
    public Class<? extends SwoftyPacket> getHandledPacketClass() {
        return BlockPlacePacket.class;
    }

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, Object loaderPacket) {
        if (!(loaderPacket instanceof PacketContainer packet)) return null;

        EnumWrappers.Hand hand = packet.getHands().read(0);
        BlockPosition blockPos = packet.getBlockPositionModifier().read(0);
        EnumWrappers.Direction direction = packet.getDirections().read(0);

        float cursorX = packet.getFloat().size() > 0 ? packet.getFloat().read(0) : 0.5f;
        float cursorY = packet.getFloat().size() > 1 ? packet.getFloat().read(1) : 0.5f;
        float cursorZ = packet.getFloat().size() > 2 ? packet.getFloat().read(2) : 0.5f;

        boolean insideBlock = packet.getBooleans().size() > 0 && packet.getBooleans().read(0);
        int sequence = packet.getIntegers().size() > 0 ? packet.getIntegers().read(0) : 0;

        BlockPlacePacket.Hand handType = hand == EnumWrappers.Hand.MAIN_HAND ?
                BlockPlacePacket.Hand.MAIN_HAND : BlockPlacePacket.Hand.OFF_HAND;
        BlockPlacePacket.Direction dir = mapDirection(direction);
        Pos position = new Pos(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        return new BlockPlacePacket(uuid, handType, position, dir, cursorX, cursorY, cursorZ, insideBlock, sequence);
    }

    @Override
    public Object buildLoaderPacket(UUID uuid, SwoftyPacket swoftyPacket) {
        return null;
    }

    private BlockPlacePacket.Direction mapDirection(EnumWrappers.Direction direction) {
        return switch (direction) {
            case DOWN -> BlockPlacePacket.Direction.DOWN;
            case UP -> BlockPlacePacket.Direction.UP;
            case NORTH -> BlockPlacePacket.Direction.NORTH;
            case SOUTH -> BlockPlacePacket.Direction.SOUTH;
            case WEST -> BlockPlacePacket.Direction.WEST;
            case EAST -> BlockPlacePacket.Direction.EAST;
        };
    }
}
