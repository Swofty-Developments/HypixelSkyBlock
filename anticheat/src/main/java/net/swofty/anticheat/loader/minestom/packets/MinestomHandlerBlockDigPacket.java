package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientPlayerDiggingPacket;
import net.swofty.anticheat.event.packet.BlockDigPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;
import net.swofty.anticheat.math.Pos;

import java.util.UUID;

public class MinestomHandlerBlockDigPacket
        extends LoaderPacketHandler<ClientPlayerDiggingPacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, ClientPlayerDiggingPacket packet) {
        BlockDigPacket.Status status = mapStatus(packet.status());
        BlockDigPacket.Direction direction = mapDirection(packet.blockFace());
        Pos position = new Pos(
                packet.blockPosition().blockX(),
                packet.blockPosition().blockY(),
                packet.blockPosition().blockZ()
        );

        return new BlockDigPacket(uuid, status, position, direction, packet.sequence());
    }

    @Override
    public ClientPlayerDiggingPacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return null;
    }

    @Override
    public Class<ClientPlayerDiggingPacket> getHandledPacketClass() {
        return ClientPlayerDiggingPacket.class;
    }

    private BlockDigPacket.Status mapStatus(ClientPlayerDiggingPacket.Status status) {
        return switch (status) {
            case STARTED_DIGGING -> BlockDigPacket.Status.STARTED_DIGGING;
            case CANCELLED_DIGGING -> BlockDigPacket.Status.CANCELLED_DIGGING;
            case FINISHED_DIGGING -> BlockDigPacket.Status.FINISHED_DIGGING;
            case DROP_ITEM_STACK -> BlockDigPacket.Status.DROP_ITEM_STACK;
            case DROP_ITEM -> BlockDigPacket.Status.DROP_ITEM;
            case UPDATE_ITEM_STATE -> BlockDigPacket.Status.UPDATE_HELD_ITEM;
            case SWAP_ITEM_HAND -> BlockDigPacket.Status.SWAP_ITEM_IN_HAND;
        };
    }

    private BlockDigPacket.Direction mapDirection(net.minestom.server.instance.block.BlockFace face) {
        return switch (face) {
            case BOTTOM -> BlockDigPacket.Direction.DOWN;
            case TOP -> BlockDigPacket.Direction.UP;
            case NORTH -> BlockDigPacket.Direction.NORTH;
            case SOUTH -> BlockDigPacket.Direction.SOUTH;
            case WEST -> BlockDigPacket.Direction.WEST;
            case EAST -> BlockDigPacket.Direction.EAST;
        };
    }
}
