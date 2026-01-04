package net.swofty.type.generic.entity;

import net.minestom.server.ServerFlag;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class DragonEntity extends LivingEntity {

    private Pos targetPosition = null;
    private double moveSpeed = 0.8;
    private UUID followingPlayer = null;

    public DragonEntity() {
        super(EntityType.ENDER_DRAGON);
        setNoGravity(true);
    }

    public void setTarget(Pos target, double speed) {
        this.targetPosition = target;
        this.moveSpeed = speed;
        this.followingPlayer = null;
    }

    public void followPlayer(Player player, double speed) {
        this.followingPlayer = player.getUuid();
        this.moveSpeed = speed;
    }

    public void clearTarget() {
        this.targetPosition = null;
        this.followingPlayer = null;
    }

    public boolean isFollowingPlayer() {
        return followingPlayer != null;
    }

    public UUID getFollowingPlayer() {
        return followingPlayer;
    }

    @Override
    protected void movementTick() {
        Pos effectiveTarget = targetPosition;

        if (followingPlayer != null) {
            Player player = instance.getPlayers().stream()
                    .filter(p -> p.getUuid().equals(followingPlayer))
                    .findFirst()
                    .orElse(null);

            if (player == null) {
                clearTarget();
                setVelocity(Vec.ZERO);
                return;
            }

            effectiveTarget = player.getPosition().add(0, 5, 0);
        }

        if (effectiveTarget == null) {
            setVelocity(Vec.ZERO);
            return;
        }

        Pos current = getPosition();
        double dx = effectiveTarget.x() - current.x();
        double dy = effectiveTarget.y() - current.y();
        double dz = effectiveTarget.z() - current.z();
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (dist < 3.0) {
            setVelocity(Vec.ZERO);
            return;
        }

        double velocityMagnitude = moveSpeed * ServerFlag.SERVER_TICKS_PER_SECOND;

        double vx = (dx / dist) * velocityMagnitude;
        double vy = (dy / dist) * velocityMagnitude;
        double vz = (dz / dist) * velocityMagnitude;

        setVelocity(new Vec(vx, vy, vz));

        float yaw = current.yaw();
        if (Math.abs(dx) > 0.1 || Math.abs(dz) > 0.1) {
            yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        }

        float pitch = 0;
        double horizontalDist = Math.sqrt(dx * dx + dz * dz);
        if (horizontalDist > 0.1 || Math.abs(dy) > 0.1) {
            pitch = (float) Math.toDegrees(Math.atan2(-dy, horizontalDist));
            pitch = Math.max(-45, Math.min(45, pitch));
        }

        if (Math.abs(yaw - current.yaw()) > 1 || Math.abs(pitch - current.pitch()) > 1) {
            refreshPosition(current.withView(yaw, pitch), true, false);
        }

        if (hasVelocity()) {
            sendPacketToViewers(getVelocityPacket());
        }
    }
}
