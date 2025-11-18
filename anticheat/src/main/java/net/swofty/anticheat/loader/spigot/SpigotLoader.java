package net.swofty.anticheat.loader.spigot;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.swofty.anticheat.event.SwoftyEventHandler;
import net.swofty.anticheat.event.events.AnticheatPacketEvent;
import net.swofty.anticheat.event.packet.*;
import net.swofty.anticheat.loader.Loader;
import net.swofty.anticheat.loader.LoaderPacketHandler;
import net.swofty.anticheat.loader.managers.SwoftySchedulerManager;
import net.swofty.anticheat.loader.spigot.packets.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpigotLoader extends Loader {
    private final Plugin plugin;
    private final ProtocolManager protocolManager;

    public SpigotLoader(Plugin plugin) {
        this.plugin = plugin;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public SwoftySchedulerManager getSchedulerManager() {
        return new SpigotSchedulerManager(plugin);
    }

    @Override
    public List<UUID> getOnlinePlayers() {
        List<UUID> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> players.add(player.getUniqueId()));
        return players;
    }

    @Override
    public void onInitialize() {
        // Register packet handlers
        registerPacketHandler(PositionAndRotationPacket.class,
                new SpigotHandlerPositionAndRotationPacket());
        registerPacketHandler(PositionPacket.class,
                new SpigotHandlerPositionPacket());
        registerPacketHandler(RotationPacket.class,
                new SpigotHandlerRotationPacket());
        registerPacketHandler(RequestPingPacket.class,
                new SpigotHandlerPingRequestPacket());
        registerPacketHandler(PingResponsePacket.class,
                new SpigotHandlerPingResponsePacket());

        // Listen for incoming movement packets
        protocolManager.addPacketListener(new PacketAdapter(plugin,
                PacketType.Play.Client.POSITION,
                PacketType.Play.Client.POSITION_LOOK,
                PacketType.Play.Client.LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                UUID playerId = event.getPlayer().getUniqueId();

                LoaderPacketHandler handler = getPacketHandler(packet.getType());
                if (handler == null) return;

                SwoftyPacket swoftyPacket = handler.buildSwoftyPacket(playerId, packet);
                if (swoftyPacket == null) return;

                SwoftyEventHandler.callEvent(new AnticheatPacketEvent(swoftyPacket));
            }
        });

        // Listen for pong packets (ping response)
        protocolManager.addPacketListener(new PacketAdapter(plugin,
                PacketType.Play.Client.PONG) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                UUID playerId = event.getPlayer().getUniqueId();

                LoaderPacketHandler handler = getPacketHandler(packet.getType());
                if (handler == null) return;

                SwoftyPacket swoftyPacket = handler.buildSwoftyPacket(playerId, packet);
                if (swoftyPacket == null) return;

                SwoftyEventHandler.callEvent(new AnticheatPacketEvent(swoftyPacket));
            }
        });

        // Listen for outgoing packets (ping requests)
        protocolManager.addPacketListener(new PacketAdapter(plugin,
                PacketType.Play.Server.PING) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                UUID playerId = event.getPlayer().getUniqueId();

                LoaderPacketHandler handler = getPacketHandler(packet.getType());
                if (handler == null) return;

                SwoftyPacket swoftyPacket = handler.buildSwoftyPacket(playerId, packet);
                if (swoftyPacket == null) return;

                SwoftyEventHandler.callEvent(new AnticheatPacketEvent(swoftyPacket));
            }
        });
    }

    @Override
    public void sendPacket(UUID uuid, SwoftyPacket packet) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        try {
            PacketContainer container = (PacketContainer) getPacketHandler(packet).buildLoaderPacket(uuid, packet);
            protocolManager.sendServerPacket(player, container);
        } catch (Exception e) {
            plugin.getLogger().severe("Error sending packet " + packet.getClass().getSimpleName() + " to " + uuid);
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        player.sendMessage(message);
    }
}
