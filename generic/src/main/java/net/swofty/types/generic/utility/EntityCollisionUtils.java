package net.swofty.types.generic.utility;

import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.tag.Tag;

import java.util.Arrays;
import java.util.stream.Stream;

public class EntityCollisionUtils {
    final static Vec power = new Vec(0.05, 0.05, 0.05);
    final static boolean[] isCollidable = new boolean[EntityType.values().size()];

    static {
        Arrays.fill(isCollidable, true);
        isCollidable[EntityType.ITEM_FRAME.id()] = false;
        isCollidable[EntityType.PAINTING.id()] = false;
        isCollidable[EntityType.GLOW_ITEM_FRAME.id()] = false;
        isCollidable[EntityType.VILLAGER.id()] = false;
        isCollidable[EntityType.LLAMA_SPIT.id()] = false;
        isCollidable[EntityType.EXPERIENCE_ORB.id()] = false;
        isCollidable[EntityType.PAINTING.id()] = false;
        isCollidable[EntityType.ARMOR_STAND.id()] = false;
        isCollidable[EntityType.END_CRYSTAL.id()] = false;
        isCollidable[EntityType.AREA_EFFECT_CLOUD.id()] = false;
        isCollidable[EntityType.LIGHTNING_BOLT.id()] = false;
        isCollidable[EntityType.ARROW.id()] = false;
        isCollidable[EntityType.SPECTRAL_ARROW.id()] = false;
        isCollidable[EntityType.SHULKER_BULLET.id()] = false;
        isCollidable[EntityType.SNOWBALL.id()] = false;
        isCollidable[EntityType.FIREBALL.id()] = false;
        isCollidable[EntityType.DRAGON_FIREBALL.id()] = false;
        isCollidable[EntityType.SMALL_FIREBALL.id()] = false;
        isCollidable[EntityType.EGG.id()] = false;
        isCollidable[EntityType.TNT.id()] = false;
        isCollidable[EntityType.ENDER_PEARL.id()] = false;
        isCollidable[EntityType.EYE_OF_ENDER.id()] = false;
        isCollidable[EntityType.FALLING_BLOCK.id()] = false;
        isCollidable[EntityType.FISHING_BOBBER.id()] = false;
        isCollidable[EntityType.ITEM_DISPLAY.id()] = false;
        isCollidable[EntityType.INTERACTION.id()] = false;
    }

    private final static Tag<String> wseeTag = Tag.String("WSEE");

    public static Vec calculateEntityCollisions(Entity entity) {
        if (!isCollidable[entity.getEntityType().id()]
                || entity.getEntityType() == EntityType.GLOW_ITEM_FRAME
                || entity.hasTag(wseeTag)
                || entity instanceof Player
                || (entity instanceof LivingEntity l && l.isDead())
                || entity.isRemoved()
                || !entity.hasCollision())
            return Vec.ZERO;

        if (entity.getInstance() == null) return Vec.ZERO;

        BoundingBox bb = entity.getBoundingBox();
        double bbFurthestCorner = Math.sqrt(bb.depth() + bb.height() + bb.width());

        Stream<Entity> foundNearby = entity.getInstance().getNearbyEntities(entity.getPosition(), bbFurthestCorner).stream()
                .filter(e ->
                        isCollidable[e.getEntityType().id()] &&
                                e.hasCollision() &&
                                !e.isRemoved()
                );

        int[] vecAcc = foundNearby.reduce(new int[3], (acc, nearby) -> {
            if (nearby == entity) return acc;

            BoundingBox collisionCheckBB = nearby.getBoundingBox();
            if (collisionCheckBB.intersectBox(nearby.getPosition().sub(entity.getPosition()), bb)) {

                // Find shortest resolution to collision by calculating the two faces with the shortest distance

                // Only solve collision for X and Z. Y doesn't matter because gravity
                // double overlapX, overlapZ;
                double currentDistanceX, currentDistanceZ;

                // X
                {
                    // Nearby left of entity
                    currentDistanceX = entity.getPosition().x() - nearby.getPosition().x();

                    // // Min distance without overlap
                    // double minDistance = collisionCheckBB.width() / 2 + bb.width() / 2;

                    // // Could be used to calculate how much of a movement to make
                    // overlapX = minDistance - Math.abs(currentDistanceX);
                }

                // If y is implemented, min distance calculation isn't h1 / 2 + h2 / 2, because entity position is from bottom of bounding box, not centre

                // Z
                {
                    // Nearby left of entity
                    currentDistanceZ = entity.getPosition().z() - nearby.getPosition().z();

                    // // Min distance without overlap
                    // double minDistance = collisionCheckBB.depth() / 2 + bb.depth() / 2;

                    // // Could be used to calculate how much of a movement to make
                    // overlapZ = minDistance - Math.abs(currentDistanceZ);
                }

                if (Math.abs(currentDistanceX) > Math.abs(currentDistanceZ)) {
                    acc[0] += (currentDistanceX > 0 ? 1 : -1);
                } else {
                    acc[2] += (currentDistanceZ > 0 ? 1 : -1);
                }
            }

            return acc;
        }, (a, b) -> {
            a[0] += b[0];
            a[1] += b[1];
            a[2] += b[2];
            return a;
        });

        if (vecAcc[0] == 0 && vecAcc[2] == 0) return Vec.ZERO;
        return new Vec(vecAcc[0], vecAcc[1], vecAcc[2]).normalize().mul(power);
    }
}
