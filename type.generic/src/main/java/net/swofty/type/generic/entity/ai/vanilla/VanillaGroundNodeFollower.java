package net.swofty.type.generic.entity.ai.vanilla;

import net.minestom.server.collision.BoundingBox;
import net.minestom.server.collision.CollisionUtils;
import net.minestom.server.collision.PhysicsResult;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.pathfinding.followers.NodeFollower;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.position.PositionUtils;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link NodeFollower} that moves an entity along a path with vanilla ground movement. It mirrors
 * Minestom's own {@code GroundNodeFollower} (horizontal {@code Vec} toward the target,
 * {@link CollisionUtils#handlePhysics} then {@code refreshPosition} with yaw/pitch from the look
 * point) but adds vanilla auto-step: when the horizontal move is blocked and the entity is on the
 * ground it retries the move raised by up to {@link #STEP_HEIGHT} and settles back down, so it
 * climbs 0.5-1.0 block ledges smoothly instead of only being able to jump them.
 */
public class VanillaGroundNodeFollower implements NodeFollower {
   /** Vanilla maximum auto-step height for a ground mob. */
   private static final double STEP_HEIGHT = 0.6;
   /** Horizontal "arrived" radius. */
   private static final double AT_POINT_RADIUS = 0.2;
   /** Vanilla jump impulse: ~0.42 blocks/tick, expressed in Minestom's blocks/second velocity. */
   private static final double JUMP_VELOCITY = 0.42 * 20.0;

   private final Entity entity;

   public VanillaGroundNodeFollower(Entity entity) {
      this.entity = entity;
   }

   @Override
   public void moveTowards(Point target, double speed, Point lookAt) {
      Pos position = this.entity.getPosition();
      double dx = target.x() - position.x();
      double dy = target.y() - position.y();
      double dz = target.z() - position.z();
      double dxLook = lookAt.x() - position.x();
      double dyLook = lookAt.y() - position.y();
      double dzLook = lookAt.z() - position.z();

      // Slow down as the entity reaches its destination (mirrors GroundNodeFollower).
      double distSquared = dx * dx + dy * dy + dz * dz;
      if (speed > distSquared) {
         speed = distSquared;
      }

      double radians = Math.atan2(dz, dx);
      double speedX = Math.cos(radians) * speed;
      double speedZ = Math.sin(radians) * speed;
      float yaw = PositionUtils.getLookYaw(dxLook, dzLook);
      float pitch = PositionUtils.getLookPitch(dxLook, dyLook, dzLook);

      Vec move = new Vec(speedX, 0.0, speedZ);
      PhysicsResult ground = CollisionUtils.handlePhysics(this.entity, move);
      Pos newPosition = ground.newPosition();

      // Vanilla auto-step: if we hit a wall while on the ground, try climbing a low ledge.
      Instance instance = this.entity.getInstance();
      if (instance != null && this.entity.isOnGround() && (ground.collisionX() || ground.collisionZ())) {
         BoundingBox boundingBox = this.entity.getBoundingBox();
         Pos raised = position.withY(position.y() + STEP_HEIGHT);
         PhysicsResult stepped = CollisionUtils.handlePhysics(instance, boundingBox, raised, move, null, false);
         if (horizontalProgress(stepped.newPosition(), position) > horizontalProgress(ground.newPosition(), position) + 1.0E-4) {
            // Cleared the ledge raised; settle back onto its top so we don't float.
            PhysicsResult settle = CollisionUtils.handlePhysics(instance, boundingBox, stepped.newPosition(), new Vec(0.0, -STEP_HEIGHT, 0.0), null, false);
            newPosition = settle.newPosition();
         }
      }

      this.entity.refreshPosition(newPosition.withView(yaw, pitch));
   }

   private static double horizontalProgress(Point moved, Point origin) {
      double dx = moved.x() - origin.x();
      double dz = moved.z() - origin.z();
      return dx * dx + dz * dz;
   }

   @Override
   public void jump(@Nullable Point from, @Nullable Point to) {
      if (this.entity.isOnGround()) {
         this.entity.setVelocity(new Vec(0.0, JUMP_VELOCITY, 0.0));
      }
   }

   @Override
   public boolean isAtPoint(Point point) {
      Pos position = this.entity.getPosition();
      double dx = position.x() - point.x();
      double dz = position.z() - point.z();
      return dx * dx + dz * dz <= AT_POINT_RADIUS * AT_POINT_RADIUS;
   }

   @Override
   public double movementSpeed() {
      return this.entity instanceof LivingEntity living ? living.getAttribute(Attribute.MOVEMENT_SPEED).getValue() : 0.1;
   }
}
