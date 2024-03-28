package net.swofty.types.generic.entity;

import net.minestom.server.MinecraftServer;
import net.minestom.server.collision.CollisionUtils;
import net.minestom.server.collision.PhysicsResult;
import net.minestom.server.collision.ShapeImpl;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.projectile.ArrowMeta;
import net.minestom.server.entity.metadata.projectile.ProjectileMeta;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithBlockEvent;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamBuilder;
import net.minestom.server.scoreboard.TeamManager;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class ArrowEntityImpl extends LivingEntity {
    private long cooldown = 0;
    private final Entity shooter;

    public ArrowEntityImpl(Entity player) {
        super(EntityType.ARROW);

        this.hasCollision = false;
        this.hasPhysics = false;

        this.shooter = player;

        ArrowMeta arrowMeta = (ArrowMeta) getEntityMeta();
        arrowMeta.setShooter(player);
        arrowMeta.setPiercingLevel((byte) 5);
    }

    @Override
    public void spawn() {
        super.spawn();
        cooldown = System.currentTimeMillis();

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (!isRemoved()) {
                kill();
                remove();
            }
        }, TaskSchedule.tick(30), TaskSchedule.stop());
    }

    @Override
    public void tick(long time) {
        final Pos posBefore = getPosition();
        super.tick(time);
        final Pos posNow = getPosition();

        Vec diff = Vec.fromPoint(posNow.sub(posBefore));
        PhysicsResult result = CollisionUtils.handlePhysics(
                instance, this.getChunk(),
                this.getBoundingBox(),
                posBefore, diff,
                null, true
        );

        if (cooldown + 200 < System.currentTimeMillis()) {
            float yaw = (float) Math.toDegrees(Math.atan2(diff.x(), diff.z()));
            float pitch = (float) Math.toDegrees(Math.atan2(diff.y(), Math.sqrt(diff.x() * diff.x() + diff.z() * diff.z())));
            super.refreshPosition(new Pos(posNow.x(), posNow.y(), posNow.z(), yaw, pitch));
            cooldown = System.currentTimeMillis();
        }

        PhysicsResult collided = CollisionUtils.checkEntityCollisions(instance, this.getBoundingBox(), posBefore, diff, 3, (e) -> e != this, result);
        if (collided != null && collided.collisionShapes()[0] != shooter) {
            if (collided.collisionShapes()[0] instanceof Entity entity) {
                EntityType entityType = entity.getEntityType();
                if (entityType == EntityType.PLAYER ||
                        entityType == EntityType.ARMOR_STAND) {
                    return;
                }

                var e = new ProjectileCollideWithEntityEvent(this, collided.newPosition(), entity);
                MinecraftServer.getGlobalEventHandler().call(e);
                if (!e.isCancelled()) {
                    remove();
                    kill();
                }
                return;
            }
        }

        if (result.hasCollision()) {
            Block hitBlock = null;
            if (result.collisionShapes()[0] instanceof ShapeImpl block) {
                hitBlock = block.block();
            }
            if (result.collisionShapes()[1] instanceof ShapeImpl block) {
                hitBlock = block.block();
            }
            if (result.collisionShapes()[2] instanceof ShapeImpl block) {
                hitBlock = block.block();
            }

            if (hitBlock == null) return;

            // We've hit a block
            MathUtility.delay(() -> {
                if (isDead() || isRemoved()) return;
                remove();
                kill();
            }, 20);
        }
    }
}
