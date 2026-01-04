package net.swofty.type.skywarsgame.entity;

import net.minestom.server.ServerFlag;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;

public class DragonEntity extends LivingEntity {

    private Pos targetPosition = null;
    private double moveSpeed = 0.8;

    public DragonEntity() {
        super(EntityType.ENDER_DRAGON);
        setNoGravity(true);
        hasPhysics = false;
    }

    public void setTarget(Pos target, double speed) {
        this.targetPosition = target;
        this.moveSpeed = speed;
    }

    public void clearTarget() {
        this.targetPosition = null;
    }

    @Override
    protected void movementTick() {
        if (targetPosition == null) {
            setVelocity(Vec.ZERO);
            return;
        }

        Pos current = getPosition();
        double dx = targetPosition.x() - current.x();
        double dy = targetPosition.y() - current.y();
        double dz = targetPosition.z() - current.z();
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (dist < 0.5) {
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
