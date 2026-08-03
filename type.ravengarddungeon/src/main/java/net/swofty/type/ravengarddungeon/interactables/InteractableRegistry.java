package net.swofty.type.ravengarddungeon.interactables;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.timer.TaskSchedule;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class InteractableRegistry {
    private static final double MAX_DISTANCE = 4.5;
    private static final double LOOK_DOT = 0.95;
    private static final int CHANNEL_TICKS = 18;

    private static final Map<Integer, DungeonInteractable> BY_INTERACTION = new ConcurrentHashMap<>();
    private static final Map<UUID, Channel> CHANNELS = new ConcurrentHashMap<>();
    private static final Map<UUID, BossBar> BARS = new ConcurrentHashMap<>();
    private static final Map<Inventory, Consumer<Player>> CLOSE_HANDLERS = new ConcurrentHashMap<>();

    private record Channel(DungeonInteractable target, float progress) {
    }

    private InteractableRegistry() {
    }

    public static void register(DungeonInteractable interactable) {
        BY_INTERACTION.put(interactable.getInteraction().getEntityId(), interactable);
    }

    public static void registerExtraInteraction(DungeonInteractable interactable, Entity extra) {
        BY_INTERACTION.put(extra.getEntityId(), interactable);
    }

    public static void unregister(DungeonInteractable interactable) {
        if (interactable.getInteraction() != null) {
            BY_INTERACTION.remove(interactable.getInteraction().getEntityId());
        }
    }

    public static void onClick(Player player, Entity target) {
        DungeonInteractable interactable = BY_INTERACTION.get(target == null ? -1 : target.getEntityId());
        if (interactable == null || interactable.isOpened()) {
            return;
        }
        Channel current = CHANNELS.get(player.getUuid());
        if (current == null || current.target() != interactable) {
            CHANNELS.put(player.getUuid(), new Channel(interactable, 0f));
            bar(player, interactable, 0f);
        }
    }

    public static void watchClose(Inventory inventory, Consumer<Player> handler) {
        CLOSE_HANDLERS.put(inventory, handler);
    }

    public static void onInventoryClose(Player player, Inventory inventory) {
        Consumer<Player> handler = inventory == null ? null : CLOSE_HANDLERS.remove(inventory);
        if (handler != null) {
            handler.accept(player);
        }
    }

    public static void startTask() {
        MinecraftServer.getSchedulerManager().buildTask(InteractableRegistry::tick)
                .repeat(TaskSchedule.tick(1)).schedule();
    }

    private static void tick() {
        for (Map.Entry<UUID, Channel> entry : CHANNELS.entrySet()) {
            Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(entry.getKey());
            Channel channel = entry.getValue();
            if (player == null || channel.target().isOpened()
                    || !isLookingAt(player, channel.target())) {
                CHANNELS.remove(entry.getKey());
                if (player != null) {
                    hideBar(player);
                }
                continue;
            }
            float progress = channel.progress() + 1f / CHANNEL_TICKS;
            if (progress >= 1f) {
                CHANNELS.remove(entry.getKey());
                hideBar(player);
                channel.target().open(player);
            } else {
                CHANNELS.put(entry.getKey(), new Channel(channel.target(), progress));
                bar(player, channel.target(), progress);
            }
        }
    }

    private static boolean isLookingAt(Player player, DungeonInteractable interactable) {
        if (interactable.getInteraction().getInstance() != player.getInstance()) {
            return false;
        }
        Pos eye = player.getPosition().add(0, player.getEyeHeight(), 0);
        Pos focus = interactable.focusPoint();
        if (eye.distance(focus) > MAX_DISTANCE) {
            return false;
        }
        Vec toward = Vec.fromPoint(focus.sub(eye)).normalize();
        return toward.dot(player.getPosition().direction()) >= LOOK_DOT;
    }

    private static void bar(Player player, DungeonInteractable target, float progress) {
        BossBar bar = BARS.computeIfAbsent(player.getUuid(), ignored -> {
            BossBar created = BossBar.bossBar(Component.empty(), 0f,
                    BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
            player.showBossBar(created);
            return created;
        });
        bar.name(net.kyori.adventure.text.Component.text(target.castLabel(),
                net.kyori.adventure.text.format.TextColor.color(0xFEFD02)));
        bar.progress(Math.min(1f, progress));
    }

    private static void hideBar(Player player) {
        BossBar bar = BARS.remove(player.getUuid());
        if (bar != null) {
            player.hideBossBar(bar);
        }
    }
}
