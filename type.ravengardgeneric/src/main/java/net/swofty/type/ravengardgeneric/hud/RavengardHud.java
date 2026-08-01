package net.swofty.type.ravengardgeneric.hud;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.user.HypixelPlayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class RavengardHud {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yy");
    private static final DateTimeFormatter CLOCK_FORMAT = DateTimeFormatter.ofPattern("mm:ss");

    private static final Map<UUID, RavengardHudState> STATES = new ConcurrentHashMap<>();
    private static final Map<UUID, Sidebar> SIDEBARS = new ConcurrentHashMap<>();
    private static final Map<UUID, Map<RavengardHudLayer, Component>> LAST_SENT = new ConcurrentHashMap<>();

    private RavengardHud() {
    }

    public static RavengardHudState state(HypixelPlayer player) {
        return STATES.computeIfAbsent(player.getUuid(), ignored -> new RavengardHudState());
    }

    public static void start() {
        MinecraftServer.getSchedulerManager()
                .buildTask(RavengardHud::tick)
                .repeat(TaskSchedule.tick(1))
                .schedule();
    }

    public static void attach(HypixelPlayer player) {
        Sidebar sidebar = new Sidebar(RavengardSidebarTitle.frame(0));

        for (RavengardHudLayer layer : RavengardHudLayer.values()) {
            sidebar.createLine(new Sidebar.ScoreboardLine(
                    layer.name(),
                    Component.empty(),
                    layer.getScore(),
                    Sidebar.NumberFormat.blank()));
        }

        SIDEBARS.put(player.getUuid(), sidebar);
        LAST_SENT.put(player.getUuid(), new ConcurrentHashMap<>());
        sidebar.addViewer(player);
    }

    public static void detach(HypixelPlayer player) {
        Sidebar sidebar = SIDEBARS.remove(player.getUuid());
        if (sidebar != null) {
            sidebar.removeViewer(player);
        }
        STATES.remove(player.getUuid());
        LAST_SENT.remove(player.getUuid());
    }

    private static final java.util.concurrent.atomic.AtomicInteger TITLE_FRAME =
            new java.util.concurrent.atomic.AtomicInteger();

    private static void tick() {
        TITLE_FRAME.incrementAndGet();
        for (HypixelPlayer player : HypixelGenericLoader.getLoadedPlayers()) {
            Sidebar sidebar = SIDEBARS.get(player.getUuid());
            Map<RavengardHudLayer, Component> previous = LAST_SENT.get(player.getUuid());
            if (sidebar == null || previous == null) {
                continue;
            }

            RavengardHudState state = state(player);
            refresh(player, state);
            sidebar.setTitle(RavengardSidebarTitle.frame(TITLE_FRAME.get()));

            sendTabMap(player, state);
            hideTabList(player);

            Map<RavengardHudLayer, Component> composed = RavengardHudComposer.compose(state);
            for (Map.Entry<RavengardHudLayer, Component> entry : composed.entrySet()) {
                if (entry.getValue().equals(previous.get(entry.getKey()))) {
                    continue;
                }
                previous.put(entry.getKey(), entry.getValue());
                sidebar.updateLineContent(entry.getKey().name(), entry.getValue());
            }
        }

        SIDEBARS.keySet().removeIf(uuid ->
                MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid) == null);
        STATES.keySet().removeIf(uuid ->
                MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid) == null);
        LAST_SENT.keySet().removeIf(uuid ->
                MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid) == null);
    }

    private static final String FULLSCREEN_TILES = "\uE120\uE121\uE122\uE123\uE124\uE125";
    private static final String PLAYER_MARKER = "\uE102";
    private static final double TAB_MAP_ORIGIN_X = 226.69;
    private static final double TAB_MAP_ORIGIN_Z = 217.21;
    private static final double YAW_PER_STEP = 360.0 / 64.0;

    private static void sendTabMap(HypixelPlayer player, RavengardHudState state) {
        int iconX = clamp9((int) Math.round(state.getWorldX() + TAB_MAP_ORIGIN_X));
        int iconY = clamp9((int) Math.round(state.getWorldZ() + TAB_MAP_ORIGIN_Z));

        float yaw = state.getYaw();
        while (yaw < 0) {
            yaw += 360f;
        }
        int rotation = ((int) Math.round(yaw / YAW_PER_STEP)) & 0x3F;
        int tint = (iconX << 15) | (iconY << 6) | rotation;

        Component header = Component.empty()
                .append(Component.text(FULLSCREEN_TILES)
                        .color(net.kyori.adventure.text.format.NamedTextColor.WHITE)
                        .shadowColor(net.kyori.adventure.text.format.ShadowColor.shadowColor(0)))
                .append(Component.newline())
                .append(Component.text(PLAYER_MARKER)
                        .color(net.kyori.adventure.text.format.TextColor.color(tint))
                        .shadowColor(net.kyori.adventure.text.format.ShadowColor.shadowColor(0)))
                .append(Component.newline());

        player.sendPlayerListHeaderAndFooter(header, Component.empty());
    }

    private static void hideTabList(HypixelPlayer player) {
        for (var online : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            player.sendPacket(new net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket(
                    net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket.Action.UPDATE_LISTED,
                    new net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket.Entry(
                            online.getUuid(), online.getUsername(), java.util.List.of(),
                            false, 0, net.minestom.server.entity.GameMode.SURVIVAL,
                            Component.empty(), null, 0, false)));
        }
    }

    private static int clamp9(int value) {
        return Math.max(0, Math.min(0x1FF, value));
    }

    private static void refresh(HypixelPlayer player, RavengardHudState state) {
        float fraction = player.getAttributeValue(Attribute.MAX_HEALTH) <= 0
                ? 1f
                : player.getHealth() / (float) player.getAttributeValue(Attribute.MAX_HEALTH);
        state.setHealth(Math.round(state.getMaxHealth() * fraction));

        // the pack redraws the vanilla exp bar as the stamina bar, so its fill has to be pushed there
        float staminaFraction = state.getMaxStamina() <= 0
                ? 0f
                : state.getStamina() / (float) state.getMaxStamina();
        staminaFraction = Math.max(0f, Math.min(1f, staminaFraction));
        if (player.getExp() != staminaFraction) {
            player.setExp(staminaFraction);
        }
        if (player.getLevel() != 0) {
            player.setLevel(0);
        }

        state.setWorldX(player.getPosition().x());
        state.setWorldZ(player.getPosition().z());
        state.setYaw(player.getPosition().yaw());
        state.setClock(LocalTime.now().format(CLOCK_FORMAT));
        state.setDate(LocalDate.now().format(DATE_FORMAT));
        state.setPlayerCount(MinecraftServer.getConnectionManager().getOnlinePlayers().size());
        if (state.getServerId().isEmpty()) {
            state.setServerId(HypixelConst.getShortenedServerName());
        }
    }
}
