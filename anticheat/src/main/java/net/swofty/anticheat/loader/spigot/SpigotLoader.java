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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpigotLoader extends Loader {
    private final Plugin plugin;
    private final ProtocolManager protocolManager;
    private final Map<PacketType, LoaderPacketHandler> packetTypeMap = new HashMap<>();

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
        registerPacketHandler(EntityActionPacket.class,
                new SpigotHandlerEntityActionPacket());
        registerPacketHandler(AbilitiesPacket.class,
                new SpigotHandlerAbilitiesPacket());
        registerPacketHandler(HeldItemChangePacket.class,
                new SpigotHandlerHeldItemChangePacket());
        registerPacketHandler(BlockDigPacket.class,
                new SpigotHandlerBlockDigPacket());
        registerPacketHandler(BlockPlacePacket.class,
                new SpigotHandlerBlockPlacePacket());
        registerPacketHandler(UseEntityPacket.class,
                new SpigotHandlerUseEntityPacket());
        registerPacketHandler(WindowClickPacket.class,
                new SpigotHandlerWindowClickPacket());
        registerPacketHandler(AnimationPacket.class,
                new SpigotHandlerAnimationPacket());
        registerPacketHandler(SteerVehiclePacket.class,
                new SpigotHandlerSteerVehiclePacket());

        // Map PacketTypes to handlers for Spigot
        packetTypeMap.put(PacketType.Play.Client.POSITION, getPacketHandler(PositionPacket.class));
        packetTypeMap.put(PacketType.Play.Client.POSITION_LOOK, getPacketHandler(PositionAndRotationPacket.class));
        packetTypeMap.put(PacketType.Play.Client.LOOK, getPacketHandler(RotationPacket.class));
        packetTypeMap.put(PacketType.Play.Server.PING, getPacketHandler(RequestPingPacket.class));
        packetTypeMap.put(PacketType.Play.Client.PONG, getPacketHandler(PingResponsePacket.class));
        packetTypeMap.put(PacketType.Play.Client.ENTITY_ACTION, getPacketHandler(EntityActionPacket.class));
        packetTypeMap.put(PacketType.Play.Client.ABILITIES, getPacketHandler(AbilitiesPacket.class));
        packetTypeMap.put(PacketType.Play.Client.HELD_ITEM_SLOT, getPacketHandler(HeldItemChangePacket.class));
        packetTypeMap.put(PacketType.Play.Client.BLOCK_DIG, getPacketHandler(BlockDigPacket.class));
        packetTypeMap.put(PacketType.Play.Client.BLOCK_PLACE, getPacketHandler(BlockPlacePacket.class));
        packetTypeMap.put(PacketType.Play.Client.USE_ENTITY, getPacketHandler(UseEntityPacket.class));
        packetTypeMap.put(PacketType.Play.Client.WINDOW_CLICK, getPacketHandler(WindowClickPacket.class));
        packetTypeMap.put(PacketType.Play.Client.ARM_ANIMATION, getPacketHandler(AnimationPacket.class));
        packetTypeMap.put(PacketType.Play.Client.STEER_VEHICLE, getPacketHandler(SteerVehiclePacket.class));

        // Listen for all incoming client packets
        protocolManager.addPacketListener(new PacketAdapter(plugin,
                PacketType.Play.Client.POSITION,
                PacketType.Play.Client.POSITION_LOOK,
                PacketType.Play.Client.LOOK,
                PacketType.Play.Client.ENTITY_ACTION,
                PacketType.Play.Client.ABILITIES,
                PacketType.Play.Client.HELD_ITEM_SLOT,
                PacketType.Play.Client.BLOCK_DIG,
                PacketType.Play.Client.BLOCK_PLACE,
                PacketType.Play.Client.USE_ENTITY,
                PacketType.Play.Client.WINDOW_CLICK,
                PacketType.Play.Client.ARM_ANIMATION,
                PacketType.Play.Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                UUID playerId = event.getPlayer().getUniqueId();

                LoaderPacketHandler handler = packetTypeMap.get(packet.getType());
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

                LoaderPacketHandler handler = packetTypeMap.get(packet.getType());
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

                LoaderPacketHandler handler = packetTypeMap.get(packet.getType());
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
