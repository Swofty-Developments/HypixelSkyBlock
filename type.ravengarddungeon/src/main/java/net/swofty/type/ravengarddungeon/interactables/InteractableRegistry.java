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

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class InteractableRegistry {
    private static final double MAX_DISTANCE = 4.5;
    private static final double LOOK_DOT = 0.97;
    private static final float LOOK_FILL_PER_TICK = 0.07f;
    private static final float CLICK_BOOST = 0.34f;

    private static final List<DungeonInteractable> INTERACTABLES = new CopyOnWriteArrayList<>();
    private static final Map<UUID, Float> PROGRESS = new ConcurrentHashMap<>();
    private static final Map<UUID, BossBar> BARS = new ConcurrentHashMap<>();
    private static final Map<Integer, DungeonInteractable> BY_INTERACTION = new ConcurrentHashMap<>();
    private static final Map<Inventory, Consumer<Player>> CLOSE_HANDLERS = new ConcurrentHashMap<>();

    private InteractableRegistry() {
    }

    public static void register(DungeonInteractable interactable) {
        INTERACTABLES.add(interactable);
        BY_INTERACTION.put(interactable.getInteraction().getEntityId(), interactable);
    }

    public static void registerExtraInteraction(DungeonInteractable interactable, Entity extra) {
        BY_INTERACTION.put(extra.getEntityId(), interactable);
    }

    public static void unregister(DungeonInteractable interactable) {
        INTERACTABLES.remove(interactable);
        if (interactable.getInteraction() != null) {
            BY_INTERACTION.remove(interactable.getInteraction().getEntityId());
        }
    }

    public static DungeonInteractable byInteraction(Entity entity) {
        return entity == null ? null : BY_INTERACTION.get(entity.getEntityId());
    }

    public static void onClick(Player player, Entity target) {
        DungeonInteractable interactable = byInteraction(target);
        if (interactable == null || interactable.isOpened()) {
            return;
        }
        float progress = PROGRESS.merge(player.getUuid(), CLICK_BOOST, Float::sum);
        if (progress >= 1f) {
            complete(player, interactable);
        } else {
            bar(player, progress);
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
        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            DungeonInteractable target = targetOf(player);
            if (target == null) {
                fade(player);
                continue;
            }
            float progress = PROGRESS.merge(player.getUuid(),
                    LOOK_FILL_PER_TICK, Float::sum);
            if (progress >= 1f && target.openOnLook()) {
                complete(player, target);
            } else {
                bar(player, Math.min(1f, progress));
            }
        }
    }

    private static void complete(Player player, DungeonInteractable interactable) {
        PROGRESS.remove(player.getUuid());
        hideBar(player);
        if (!interactable.isOpened()) {
            interactable.open(player);
        }
    }

    private static DungeonInteractable targetOf(Player player) {
        Pos eye = player.getPosition().add(0, player.getEyeHeight(), 0);
        Vec look = player.getPosition().direction();
        DungeonInteractable best = null;
        double bestDistance = MAX_DISTANCE;
        for (DungeonInteractable interactable : INTERACTABLES) {
            if (interactable.isOpened()) continue;
            if (interactable.getInteraction().getInstance() != player.getInstance()) continue;
            Pos focus = interactable.focusPoint();
            double distance = eye.distance(focus);
            if (distance > bestDistance) continue;
            Vec toward = Vec.fromPoint(focus.sub(eye)).normalize();
            if (toward.dot(look) < LOOK_DOT) continue;
            best = interactable;
            bestDistance = distance;
        }
        return best;
    }

    private static void fade(Player player) {
        Float progress = PROGRESS.get(player.getUuid());
        if (progress == null) return;
        float next = progress - LOOK_FILL_PER_TICK * 2;
        if (next <= 0f) {
            PROGRESS.remove(player.getUuid());
            hideBar(player);
        } else {
            PROGRESS.put(player.getUuid(), next);
            bar(player, next);
        }
    }

    private static void bar(Player player, float progress) {
        BossBar bar = BARS.computeIfAbsent(player.getUuid(), ignored -> {
            BossBar created = BossBar.bossBar(Component.empty(), 0f,
                    BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
            player.showBossBar(created);
            return created;
        });
        bar.progress(Math.min(1f, progress));
    }

    private static void hideBar(Player player) {
        BossBar bar = BARS.remove(player.getUuid());
        if (bar != null) {
            player.hideBossBar(bar);
        }
    }
}
