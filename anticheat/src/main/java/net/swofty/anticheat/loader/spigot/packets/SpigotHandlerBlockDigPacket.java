package net.swofty.anticheat.loader.spigot.packets;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.swofty.anticheat.event.packet.BlockDigPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;
import net.swofty.anticheat.math.Pos;

import java.util.UUID;

public class SpigotHandlerBlockDigPacket extends LoaderPacketHandler {

    @Override
    public Class<? extends SwoftyPacket> getHandledPacketClass() {
        return BlockDigPacket.class;
    }

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, Object loaderPacket) {
        if (!(loaderPacket instanceof PacketContainer packet)) return null;

        EnumWrappers.PlayerDigType digType = packet.getPlayerDigTypes().read(0);
        BlockPosition blockPos = packet.getBlockPositionModifier().read(0);
        EnumWrappers.Direction direction = packet.getDirections().read(0);
        int sequence = packet.getIntegers().size() > 0 ? packet.getIntegers().read(0) : 0;

        BlockDigPacket.Status status = mapStatus(digType);
        BlockDigPacket.Direction dir = mapDirection(direction);
        Pos position = new Pos(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        return new BlockDigPacket(uuid, status, position, dir, sequence);
    }

    @Override
    public Object buildLoaderPacket(UUID uuid, SwoftyPacket swoftyPacket) {
        return null;
    }

    private BlockDigPacket.Status mapStatus(EnumWrappers.PlayerDigType digType) {
        return switch (digType) {
            case START_DESTROY_BLOCK -> BlockDigPacket.Status.STARTED_DIGGING;
            case ABORT_DESTROY_BLOCK -> BlockDigPacket.Status.CANCELLED_DIGGING;
            case STOP_DESTROY_BLOCK -> BlockDigPacket.Status.FINISHED_DIGGING;
            case DROP_ALL_ITEMS -> BlockDigPacket.Status.DROP_ITEM_STACK;
            case DROP_ITEM -> BlockDigPacket.Status.DROP_ITEM;
            case RELEASE_USE_ITEM -> BlockDigPacket.Status.UPDATE_HELD_ITEM;
            case SWAP_HELD_ITEMS -> BlockDigPacket.Status.SWAP_ITEM_IN_HAND;
        };
    }

    private BlockDigPacket.Direction mapDirection(EnumWrappers.Direction direction) {
        return switch (direction) {
            case DOWN -> BlockDigPacket.Direction.DOWN;
            case UP -> BlockDigPacket.Direction.UP;
            case NORTH -> BlockDigPacket.Direction.NORTH;
            case SOUTH -> BlockDigPacket.Direction.SOUTH;
            case WEST -> BlockDigPacket.Direction.WEST;
            case EAST -> BlockDigPacket.Direction.EAST;
        };
    }
}
