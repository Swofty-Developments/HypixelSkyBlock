package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientInputPacket;
import net.swofty.anticheat.event.packet.SteerVehiclePacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerSteerVehiclePacket
        extends LoaderPacketHandler<ClientInputPacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, ClientInputPacket packet) {

        float sideways = 0f;
        if (packet.left()) sideways -= 1f;
        if (packet.right()) sideways += 1f;

        float forward = 0f;
        if (packet.forward()) forward += 1f;
        if (packet.backward()) forward -= 1f;

        boolean jump = packet.jump();
        boolean unmount = packet.shift();

        return new SteerVehiclePacket(uuid, sideways, forward, jump, unmount);
    }

    @Override
    public ClientInputPacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return null;
    }

    @Override
    public Class<ClientInputPacket> getHandledPacketClass() {
        return ClientInputPacket.class;
    }
}