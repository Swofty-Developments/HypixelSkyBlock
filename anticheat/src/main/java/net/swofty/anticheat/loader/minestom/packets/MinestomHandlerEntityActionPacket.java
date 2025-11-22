package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientPlayerPacket;
import net.swofty.anticheat.event.packet.EntityActionPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerEntityActionPacket
        extends LoaderPacketHandler<ClientPlayerPacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, ClientPlayerPacket packet) {
        EntityActionPacket.Action action = mapAction(packet);
        int jumpBoost = 0;

        if (action == EntityActionPacket.Action.START_JUMP_WITH_HORSE) {
            jumpBoost = 100;
        }

        return new EntityActionPacket(uuid, action, jumpBoost);
    }

    @Override
    public ClientPlayerPacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return null;
    }

    @Override
    public Class<ClientPlayerPacket> getHandledPacketClass() {
        return ClientPlayerPacket.class;
    }

    private EntityActionPacket.Action mapAction(ClientPlayerPacket packet) {
        if (packet.sprinting()) return EntityActionPacket.Action.START_SPRINTING;
        if (packet.sneaking()) return EntityActionPacket.Action.START_SNEAKING;
        return EntityActionPacket.Action.STOP_SNEAKING;
    }
}
