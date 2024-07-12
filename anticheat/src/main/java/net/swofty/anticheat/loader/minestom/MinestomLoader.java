package net.swofty.anticheat.loader.minestom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.listener.manager.PacketPlayListenerConsumer;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.common.ClientPingRequestPacket;
import net.minestom.server.network.packet.client.common.ClientPongPacket;
import net.minestom.server.network.packet.client.play.*;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.common.PingPacket;
import net.swofty.anticheat.event.SwoftyEventHandler;
import net.swofty.anticheat.event.events.AnticheatPacketEvent;
import net.swofty.anticheat.event.packet.*;
import net.swofty.anticheat.loader.LoaderPacketHandler;
import net.swofty.anticheat.loader.managers.SwoftySchedulerManager;
import net.swofty.anticheat.loader.Loader;
import net.swofty.anticheat.loader.minestom.packets.*;
import org.tinylog.Logger;

import java.util.*;

public class MinestomLoader extends Loader {
    @Override
    public SwoftySchedulerManager getSchedulerManager() {
        return new MinestomSchedulerManager();
    }

    @Override
    public List<UUID> getOnlinePlayers() {
        List<UUID> players = new ArrayList<>();
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player -> players.add(player.getUuid()));
        return players;
    }

    @Override
    public void onInitialize() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        registerPacketHandler(PositionAndRotationPacket.class,
                new MinestomHandlerPositionAndRotationPacket());
        registerPacketHandler(IsOnGroundPacket.class,
                new MinestomHandlerClientPacket());
        registerPacketHandler(PositionPacket.class,
                new MinestomHandlerPositionPacket());
        registerPacketHandler(RotationPacket.class,
                new MinestomHandlerRotationPacket());
        registerPacketHandler(RequestPingPacket.class,
                new MinestomHandlerPingRequestPacket());
        registerPacketHandler(PingResponsePacket.class,
                new MinestomHandlerPingResponsePacket());

        globalEventHandler.addListener(PlayerPacketOutEvent.class, (event) -> {
            ServerPacket packet = event.getPacket();
            LoaderPacketHandler handler = getPacketHandler(packet.getClass());
            if (handler == null) return;

            SwoftyPacket swoftyPacket = handler.buildSwoftyPacket(event.getPlayer().getUuid(), packet);
            if (swoftyPacket == null) return;

            SwoftyEventHandler.callEvent(new AnticheatPacketEvent(swoftyPacket));
        });

        MinecraftServer.getPacketListenerManager().setPlayListener(
                ClientPongPacket.class, (packet, player) -> {
                    LoaderPacketHandler handler = getPacketHandler(ClientPongPacket.class);
                    SwoftyPacket swoftyPacket = handler.buildSwoftyPacket(player.getUuid(), packet);
                    if (swoftyPacket == null) return;

                    SwoftyEventHandler.callEvent(new AnticheatPacketEvent(swoftyPacket));
                });

        globalEventHandler.addListener(PlayerPacketEvent.class, (event) -> {
            ClientPacket packet = event.getPacket();
            LoaderPacketHandler handler = getPacketHandler(packet.getClass());
            if (handler == null) return;

            SwoftyPacket swoftyPacket = handler.buildSwoftyPacket(event.getPlayer().getUuid(), packet);
            if (swoftyPacket == null) return;

            SwoftyEventHandler.callEvent(new AnticheatPacketEvent(swoftyPacket));
        });
    }

    @Override
    public void sendPacket(UUID uuid, SwoftyPacket packet) {
        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
        if (player == null) return;

        try {
            player.sendPacket((SendablePacket) getPacketHandler(packet).buildLoaderPacket(uuid, packet));
        } catch (Exception e) {
            Logger.error("Error when attempting to send packet " + packet.getClass().getSimpleName() + " to " + uuid);
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
        if (player == null) return;

        player.sendMessage(message);
    }
}
