package net.swofty.type.ravengardgeneric.texturepack;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.EntityTeleportPacket;
import net.minestom.server.network.packet.server.play.MapDataPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.ravengardgeneric.texturepack.entities.ClientHudMapEntity;
import net.swofty.type.ravengardgeneric.texturepack.widgets.DefaultHudWidgets;
import net.swofty.type.ravengardgeneric.texturepack.widgets.HudMapWidget;
import net.swofty.type.ravengardgeneric.texturepack.widgets.HudMapWidgetContext;
import net.swofty.type.ravengardgeneric.texturepack.widgets.HudWidgetType;
import net.swofty.type.ravengardgeneric.texturepack.widgets.HudWidgetRegistry;
import org.tinylog.Logger;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TexturePackManager {
    private static final int UPDATE_INTERVAL_TICKS = 4;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final AtomicInteger NEXT_WIDGET_MAP_ID = new AtomicInteger(11000);

    @Getter
    private static TexturePackManager instance;

    private final Set<HypixelPlayer> enabledPlayers = ConcurrentHashMap.newKeySet();
    private final ConcurrentHashMap<HypixelPlayer, ConcurrentHashMap<String, ClientHudMapEntity>> spawnedWidgetEntities =
            new ConcurrentHashMap<>();
    private final ConcurrentHashMap<HypixelPlayer, Object> renderLocks = new ConcurrentHashMap<>();
    private final TexturePackRenderer renderer = new TexturePackRenderer();
    private final HudWidgetRegistry widgetRegistry = new HudWidgetRegistry();

    public TexturePackManager() {
        instance = this;
        DefaultHudWidgets.registerInto(widgetRegistry);
    }

    public void start() {
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            for (HypixelPlayer player : enabledPlayers) {
                if (!player.isOnline() || player.getInstance() == null) {
                    if (!player.isOnline()) {
                        disableFor(player);
                    }
                    continue;
                }

                Thread.startVirtualThread(() -> {
                    try {
                        synchronized (renderLock(player)) {
                            renderWidgetsFor(player);
                        }
                    } catch (Exception e) {
                        Logger.error(e, "Failed to render HUD texture pack for {}", player.getUsername());
                    }
                });
            }
        }).repeat(TaskSchedule.tick(UPDATE_INTERVAL_TICKS)).schedule();
    }

    public void enableFor(HypixelPlayer player) {
        enabledPlayers.add(player);
        synchronized (renderLock(player)) {
            if (player.getInstance() != null) {
                try {
                    renderWidgetsFor(player);
                } catch (Exception e) {
                    Logger.error(e, "Failed to send initial HUD frame for {}", player.getUsername());
                }
            }
        }
    }

    public void disableFor(HypixelPlayer player) {
        enabledPlayers.remove(player);
        synchronized (renderLock(player)) {
            destroyAllWidgetEntities(player);
        }
        renderLocks.remove(player);
    }

    public boolean isEnabled(HypixelPlayer player) {
        return enabledPlayers.contains(player);
    }

    public void toggle(HypixelPlayer player) {
        if (isEnabled(player)) {
            disableFor(player);
        } else {
            enableFor(player);
        }
    }

    public void registerWidget(HudMapWidget widget) {
        widgetRegistry.register(widget);
    }

    public void registerWidget(HudWidgetType type, HudMapWidget widget) {
        widgetRegistry.register(type, widget);
    }

    public void unregisterWidget(String widgetId) {
        if (widgetId == null) {
            return;
        }
        HudMapWidget removed = widgetRegistry.unregister(widgetId);
        if (removed == null) {
            return;
        }

        for (HypixelPlayer player : enabledPlayers) {
            synchronized (renderLock(player)) {
                removeWidgetEntityForPlayer(player, widgetId);
            }
        }
    }

    public void unregisterWidget(HudWidgetType type) {
        if (type == null) {
            return;
        }
        unregisterWidget(type.id());
    }

    public Collection<HudMapWidget> getWidgets() {
        return widgetRegistry.all();
    }

    public int nextWidgetMapId() {
        return NEXT_WIDGET_MAP_ID.getAndIncrement();
    }

    private void renderWidgetsFor(HypixelPlayer player) {
        if (player.getInstance() == null) {
            return;
        }

        String shortServerName = currentShortServerName();
        String date = LocalDate.now(ZoneOffset.UTC).format(DATE_FORMAT);
        HudMapWidgetContext context = new HudMapWidgetContext(
                player,
                player.getInstance(),
                player.getPosition(),
                shortServerName,
                date,
                renderer
        );

        ConcurrentHashMap<String, ClientHudMapEntity> playerEntities = spawnedWidgetEntities.computeIfAbsent(
                player, ignored -> new ConcurrentHashMap<>()
        );
        Set<String> activeWidgetIds = new HashSet<>();

        for (HudMapWidget widget : widgetRegistry.all()) {
            if (!widget.enabled(context)) {
                continue;
            }

            activeWidgetIds.add(widget.id());
            MapDataPacket packet = widget.render(context);
            player.sendPacket(packet);

            ClientHudMapEntity entity = playerEntities.get(widget.id());
            if (entity == null) {
                entity = spawnWidgetEntity(player, widget.mapId());
                playerEntities.put(widget.id(), entity);
            } else if (entity.mapId() != widget.mapId()) {
                entity.setMapId(widget.mapId());
                player.sendPacket(entity.getMetadataPacket());
            }

            syncHudEntityPosition(player, entity);
        }

        List<Integer> removedEntityIds = removeInactiveWidgetEntities(playerEntities, activeWidgetIds);
        if (!removedEntityIds.isEmpty()) {
            player.sendPacket(new DestroyEntitiesPacket(removedEntityIds));
        }
    }

    private ClientHudMapEntity spawnWidgetEntity(HypixelPlayer player, int mapId) {
        ClientHudMapEntity entity = new ClientHudMapEntity(mapId);
        player.sendPacket(entity.createSpawnPacket(hudCarrierPosition(player)));
        player.sendPacket(entity.getMetadataPacket());
        return entity;
    }

    private List<Integer> removeInactiveWidgetEntities(
            ConcurrentHashMap<String, ClientHudMapEntity> playerEntities,
            Set<String> activeWidgetIds
    ) {
        List<Integer> removedEntityIds = new ArrayList<>();
        for (Map.Entry<String, ClientHudMapEntity> entry : new ArrayList<>(playerEntities.entrySet())) {
            if (activeWidgetIds.contains(entry.getKey())) {
                continue;
            }
            playerEntities.remove(entry.getKey());
            removedEntityIds.add(entry.getValue().getEntityId());
        }
        return removedEntityIds;
    }

    private void removeWidgetEntityForPlayer(HypixelPlayer player, String widgetId) {
        ConcurrentHashMap<String, ClientHudMapEntity> playerEntities = spawnedWidgetEntities.get(player);
        if (playerEntities == null) {
            return;
        }

        ClientHudMapEntity removed = playerEntities.remove(widgetId);
        if (removed == null) {
            return;
        }

        if (playerEntities.isEmpty()) {
            spawnedWidgetEntities.remove(player);
        }

        if (!player.isOnline()) {
            return;
        }
        player.sendPacket(new DestroyEntitiesPacket(removed.getEntityId()));
    }

    private void destroyAllWidgetEntities(HypixelPlayer player) {
        ConcurrentHashMap<String, ClientHudMapEntity> playerEntities = spawnedWidgetEntities.remove(player);
        if (!player.isOnline()) {
            return;
        }
        if (playerEntities == null || playerEntities.isEmpty()) {
            return;
        }

        List<Integer> entityIds = new ArrayList<>(playerEntities.size());
        for (ClientHudMapEntity entity : playerEntities.values()) {
            entityIds.add(entity.getEntityId());
        }
        player.sendPacket(new DestroyEntitiesPacket(entityIds));
    }

    private void syncHudEntityPosition(HypixelPlayer player, ClientHudMapEntity entity) {
        Pos position = hudCarrierPosition(player);
        player.sendPacket(new EntityTeleportPacket(
                entity.getEntityId(),
                position,
                Vec.ZERO,
                0,
                false
        ));
    }

    private Pos hudCarrierPosition(HypixelPlayer player) {
        Pos base = player.getPosition().add(0.0, player.getEyeHeight(), 0.0);
        double yawRadians = Math.toRadians(base.yaw());
        double pitchRadians = Math.toRadians(base.pitch());
        double cosPitch = Math.cos(pitchRadians);

        double forwardX = -Math.sin(yawRadians) * cosPitch;
        double forwardY = -Math.sin(pitchRadians);
        double forwardZ = Math.cos(yawRadians) * cosPitch;

        return new Pos(
                base.x() + forwardX * 2.0,
                base.y() + forwardY * 2.0,
                base.z() + forwardZ * 2.0,
                base.yaw(),
                base.pitch()
        );
    }

    private Object renderLock(HypixelPlayer player) {
        return renderLocks.computeIfAbsent(player, ignored -> new Object());
    }

    private static String currentShortServerName() {
        String shortenedServerName = HypixelConst.getShortenedServerName();
        if (shortenedServerName == null || shortenedServerName.isBlank()) {
            return "unknown";
        }
        return shortenedServerName;
    }
}
