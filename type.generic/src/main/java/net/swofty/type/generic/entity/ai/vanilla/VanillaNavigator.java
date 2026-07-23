package net.swofty.type.generic.entity.ai.vanilla;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.pathfinding.PNode;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.Nullable;

/**
 * A fully self-contained, vanilla-flavoured navigator that replaces Minestom's own
 * {@code Navigator}. It owns its A* search and its path-follow loop, and it drives movement
 * entirely through the reused vanilla primitives — {@link VanillaGroundNodeGenerator} for
 * walkable-neighbour expansion and {@link VanillaGroundNodeFollower} for vanilla physics
 * ({@code CollisionUtils.handlePhysics} + 0.6 auto-step + jump impulse).
 *
 * <p>Nothing here touches Minestom's {@code Navigator}/{@code PathGenerator}; the only Minestom
 * pieces used are the block/entity access APIs and the {@link PNode} value type. Goal/target
 * selection still runs through Minestom's {@code GoalSelector}/{@code TargetSelector}, which call
 * {@link #pathTo(Point)} / {@link #tick()} instead of the built-in navigator.
 */
public class VanillaNavigator {
   /** Hard ceiling on A* node expansions, regardless of distance. */
   private static final int HARD_ITERATION_CAP = 4000;
   /** Iterations budgeted per block of straight-line distance. */
   private static final int ITERATIONS_PER_BLOCK = 200;
   /** Floor for the iteration budget so short paths still get a fair search. */
   private static final int MIN_ITERATIONS = 200;
   /** Deepest gravity snap used when resolving the start/target onto solid ground. */
   private static final double MAX_SNAP = 100.0;
   /** A waypoint this much higher than the entity's feet needs a jump, not just an auto-step. */
   private static final double JUMP_STEP_THRESHOLD = 0.9;

   private final Entity entity;
   private final VanillaGroundNodeGenerator generator = new VanillaGroundNodeGenerator();
   private final VanillaGroundNodeFollower follower;

   private List<Point> waypoints = new ArrayList<>();
   private int currentIndex;
   private boolean complete = true;
   private @Nullable Point targetPosition;

   public VanillaNavigator(Entity entity) {
      this.entity = entity;
      this.follower = new VanillaGroundNodeFollower(entity);
   }

   /** Convenience overload; see {@link #pathTo(Point)}. */
   public boolean pathTo(double x, double y, double z) {
      return pathTo(new Vec(x, y, z));
   }

   /**
    * Run our own A* from the entity's gravity-snapped block position to {@code target} and store the
    * resulting waypoint list. Any prior path is discarded first.
    *
    * @return {@code true} if a usable (non-empty) path — full or best-effort partial — was produced.
    */
   public boolean pathTo(@Nullable Point target) {
      // Always reset any prior path first.
      stop();
      if (target == null) {
         return false;
      }
      Instance instance = entity.getInstance();
      if (instance == null) {
         return false;
      }

      BoundingBox boundingBox = entity.getBoundingBox();
      Pos position = entity.getPosition();

      // Gravity-snap the start and target onto the ground the mob would actually stand on.
      Point start = snap(instance, position, boundingBox);
      Point goal = snap(instance, target, boundingBox);

      double centerToCorner = Math.sqrt(boundingBox.width() * boundingBox.width()
         + boundingBox.depth() * boundingBox.depth()) / 2.0;
      double closeDistance = Math.max(0.8, centerToCorner);

      // Already at the destination — nothing to path.
      if (position.distanceSquared(target) < closeDistance * closeDistance) {
         return false;
      }

      double straight = generator.heuristic(start, goal);
      int iterationCap = (int) Math.min(HARD_ITERATION_CAP,
         Math.max(MIN_ITERATIONS, ITERATIONS_PER_BLOCK * straight));

      PNode startNode = new PNode(start, 0.0, generator.heuristic(start, goal), PNode.Type.WALK, null);

      // Open set ordered by f = g + h; closed set doubles as the generator's "already seen" filter
      // (PNode.equals/hashCode key on block coordinates, so this dedupes by block like vanilla).
      PriorityQueue<PNode> open = new PriorityQueue<>(Comparator.comparingDouble(n -> n.g() + n.h()));
      Set<PNode> closed = new HashSet<>();
      open.add(startNode);

      PNode closest = startNode;
      double closestH = startNode.h();
      PNode reached = null;
      int iterations = 0;

      while (!open.isEmpty() && iterations < iterationCap) {
         iterations++;
         PNode current = open.poll();

         if (withinDistance(current, goal, closeDistance)) {
            reached = current;
            break;
         }
         if (current.h() < closestH) {
            closestH = current.h();
            closest = current;
         }

         Collection<? extends PNode> neighbours =
            generator.getWalkable(instance, closed, current, goal, boundingBox);
         for (PNode neighbour : neighbours) {
            open.add(neighbour);
            closed.add(neighbour);
         }
      }

      // Full path if we reached the goal, otherwise a best-effort partial toward the closest node.
      boolean goalReached = reached != null;
      PNode end = goalReached ? reached : closest;

      List<Point> path = new ArrayList<>();
      PNode node = end;
      // Walk the parent chain back to (but excluding) the start node.
      while (node != null && node.parent() != null) {
         path.add(new Vec(node.x(), node.y(), node.z()));
         node = node.parent();
      }
      Collections.reverse(path);

      if (goalReached) {
         // Finish on the exact requested target so the mob walks all the way in.
         path.add(new Vec(target.x(), target.y(), target.z()));
      }

      if (path.isEmpty()) {
         // No progress could be made from the start node.
         return false;
      }

      this.waypoints = path;
      this.currentIndex = 0;
      this.complete = false;
      this.targetPosition = target;
      return true;
   }

   /**
    * Advance one tick of path following: move toward the current waypoint with vanilla physics,
    * look toward the next one, jump when a waypoint steps a full block up, and advance the cursor
    * as waypoints are reached. Robust to a null instance, an empty path, or the entity drifting.
    */
   public void tick() {
      if (complete || waypoints.isEmpty()) {
         return;
      }
      if (entity.getInstance() == null) {
         return;
      }

      // Skip over any waypoints we're already standing on (handles overshoot / drift onto a later node).
      while (currentIndex < waypoints.size() && follower.isAtPoint(waypoints.get(currentIndex))) {
         currentIndex++;
      }
      if (currentIndex >= waypoints.size()) {
         complete = true;
         return;
      }

      Point currentWaypoint = waypoints.get(currentIndex);
      Point lookAt = currentIndex + 1 < waypoints.size() ? waypoints.get(currentIndex + 1) : currentWaypoint;

      follower.moveTowards(currentWaypoint, follower.movementSpeed(), lookAt);

      if (follower.isAtPoint(currentWaypoint)) {
         currentIndex++;
         if (currentIndex >= waypoints.size()) {
            complete = true;
         }
      } else if (currentWaypoint.y() - entity.getPosition().y() > JUMP_STEP_THRESHOLD) {
         // Next waypoint is a full block up: an auto-step can't reach it, so jump.
         follower.jump(entity.getPosition(), currentWaypoint);
      }
   }

   /** Discard the current path; {@link #isComplete()} becomes {@code true}. */
   public void stop() {
      this.waypoints = new ArrayList<>();
      this.currentIndex = 0;
      this.complete = true;
      this.targetPosition = null;
   }

   /** @return {@code true} when there is no active path or the path has been fully followed. */
   public boolean isComplete() {
      return complete || waypoints.isEmpty();
   }

   /** @return the last requested target, or {@code null} if there is no active path. */
   public @Nullable Point getTargetPosition() {
      return targetPosition;
   }

   /** @return the current waypoint list (empty when idle). */
   public List<Point> getNodes() {
      return waypoints;
   }

   private Point snap(Instance instance, Point point, BoundingBox boundingBox) {
      if (!generator.hasGravitySnap()) {
         return point;
      }
      return point.withY(generator
         .gravitySnap(instance, point.x(), point.y(), point.z(), boundingBox, MAX_SNAP)
         .orElse(point.y()));
   }

   private static boolean withinDistance(PNode node, Point target, double closeDistance) {
      double dx = node.x() - target.x();
      double dy = node.y() - target.y();
      double dz = node.z() - target.z();
      return dx * dx + dy * dy + dz * dz < closeDistance * closeDistance;
   }
}
