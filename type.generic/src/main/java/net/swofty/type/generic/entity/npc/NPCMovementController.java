package net.swofty.type.generic.entity.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.attribute.Attribute;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Moves one of the per-player NPC entities through an ordered set of waypoints.
 */
public final class NPCMovementController {
    /**
     * The vanilla base movement-speed attribute for a walking player.
     */
    public static final double WALKING_SPEED = 0.1;

    private final EntityCreature entity;
    private long movementId;
    private CompletableFuture<Void> currentMovement;
    private List<Pos> waypoints = List.of();
    private int waypointIndex;
    private boolean previousNoGravity;

    public NPCMovementController(EntityCreature entity) {
        this.entity = Objects.requireNonNull(entity, "entity");
    }

    /**
     * Replaces any current route and walks through each supplied point in order.
     * The returned future completes after the NPC reaches the final point.
     */
    public synchronized CompletableFuture<Void> walkPath(List<Pos> points) {
        Objects.requireNonNull(points, "points");
        List<Pos> route = List.copyOf(points);

        cancelCurrentMovement();
        if (route.isEmpty()) return CompletableFuture.completedFuture(null);

        long id = ++movementId;
        this.waypoints = route;
        this.waypointIndex = 0;
        this.currentMovement = new CompletableFuture<>();
        this.previousNoGravity = entity.hasNoGravity();
        CompletableFuture<Void> movement = this.currentMovement;

        entity.setNoGravity(false);
        entity.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(WALKING_SPEED);
        moveToNextWaypoint(id);
        return movement;
    }

    /**
     * Stops the active route at the NPC's current position.
     */
    public synchronized void stop() {
        cancelCurrentMovement();
        movementId++;
    }

    public synchronized boolean isMoving() {
        return currentMovement != null && !currentMovement.isDone();
    }

    private void moveToNextWaypoint(long id) {
        if (id != movementId || currentMovement == null || currentMovement.isDone()) return;
        if (waypointIndex >= waypoints.size()) {
            finishMovement();
            return;
        }
        if (entity.getInstance() == null || entity.isRemoved()) {
            failMovement(new IllegalStateException("Cannot move an NPC that is not spawned"));
            return;
        }

        Pos target = waypoints.get(waypointIndex);
        if (entity.getPosition().sameBlock(target)) {
            reachWaypoint(id, target);
            return;
        }

        try {
            if (!entity.getInstance().isChunkLoaded(target)) {
                entity.getInstance().loadChunk(target).join();
            }

            AtomicBoolean callbackRan = new AtomicBoolean();
            boolean pathStarted = entity.getNavigator().setPathTo(target, 0.1, () -> {
                callbackRan.set(true);
                synchronized (NPCMovementController.this) {
                    reachWaypoint(id, target);
                }
            });

            if (!pathStarted && !callbackRan.get()) {
                failMovement(new IllegalStateException("No path found to NPC waypoint " + target));
            }
        } catch (RuntimeException exception) {
            failMovement(exception);
        }
    }

    private void reachWaypoint(long id, Pos target) {
        if (id != movementId || currentMovement == null || currentMovement.isDone()) return;
        if (!entity.getPosition().sameBlock(target)) {
            failMovement(new IllegalStateException("NPC could not reach waypoint " + target));
            return;
        }

        // Navigator stops within the target block. Preserve the exact coordinate and view
        // supplied by mission/scene authors before continuing to the next waypoint.
        entity.refreshPosition(target);
        waypointIndex++;
        moveToNextWaypoint(id);
    }

    private void finishMovement() {
        entity.getNavigator().reset();
        entity.setNoGravity(previousNoGravity);
        CompletableFuture<Void> completed = currentMovement;
        clearRoute();
        completed.complete(null);
    }

    private void failMovement(Throwable throwable) {
        entity.getNavigator().reset();
        entity.setNoGravity(previousNoGravity);
        CompletableFuture<Void> failed = currentMovement;
        clearRoute();
        if (failed != null) failed.completeExceptionally(throwable);
    }

    private void cancelCurrentMovement() {
        if (currentMovement == null || currentMovement.isDone()) return;

        entity.getNavigator().reset();
        entity.setNoGravity(previousNoGravity);
        CompletableFuture<Void> cancelled = currentMovement;
        clearRoute();
        cancelled.completeExceptionally(new CancellationException("NPC movement was replaced or stopped"));
    }

    private void clearRoute() {
        currentMovement = null;
        waypoints = List.of();
        waypointIndex = 0;
    }
}
