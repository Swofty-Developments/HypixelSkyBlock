package net.swofty.pvp.utils;

import net.minestom.server.collision.BoundingBox;
import net.minestom.server.collision.CollisionUtils;
import net.minestom.server.collision.PhysicsResult;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.WorldBorder;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Copied from Minestom, added singleCollision parameter and removed velocity update
public class ProjectileUtil {
	public static @NotNull PhysicsResult simulateMovement(@NotNull Pos entityPosition, @NotNull Vec entityVelocityPerTick,
	                                                      @NotNull BoundingBox entityBoundingBox, @NotNull WorldBorder worldBorder,
	                                                      @NotNull Block.Getter blockGetter, boolean entityHasPhysics,
	                                                      @Nullable PhysicsResult previousPhysicsResult,
	                                                      boolean singleCollision) {
		final PhysicsResult physicsResult = entityHasPhysics ?
				CollisionUtils.handlePhysics(blockGetter, entityBoundingBox, entityPosition, entityVelocityPerTick, previousPhysicsResult, singleCollision) :
				CollisionUtils.blocklessCollision(entityPosition, entityVelocityPerTick);
		
		Pos newPosition = physicsResult.newPosition();
		Vec newVelocity = physicsResult.newVelocity();
		
		Pos positionWithinBorder = CollisionUtils.applyWorldBorder(worldBorder, entityPosition, newPosition);
		// Originally there was a call to update velocity here, but since projectiles handle it themselves it is not needed
		return new PhysicsResult(positionWithinBorder, newVelocity, physicsResult.isOnGround(), physicsResult.collisionX(), physicsResult.collisionY(), physicsResult.collisionZ(),
				physicsResult.originalDelta(), physicsResult.collisionPoints(), physicsResult.collisionShapes(), physicsResult.collisionShapePositions(), physicsResult.hasCollision(), physicsResult.res());
	}
}
