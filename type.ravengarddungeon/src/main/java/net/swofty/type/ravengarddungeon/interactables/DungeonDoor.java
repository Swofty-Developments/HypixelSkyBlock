package net.swofty.type.ravengarddungeon.interactables;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.ravengarddungeon.config.RavengardDungeonConfig;

import java.util.ArrayList;
import java.util.List;

public final class DungeonDoor extends DungeonInteractable {
    private static final String LEAF_MODEL = "hypixel_ravengard:item/gameplay/doors/door_1";
    private static final String TOP_MODEL = "hypixel_ravengard:item/gameplay/doors/door_1_top";
    private static final int SWING_TICKS = 6;
    private static final int OPEN_DURATION_TICKS = 100;

    private final Pos closedDisplayPos;
    private final Pos closedInteractionPos;
    private final Entity leaf;
    private final Vec hingeOffset;
    private final int swingSign;
    private DungeonDoor partner;

    private DungeonDoor(Instance instance, Pos displayPos, Vec hingeOffset, int swingSign) {
        super(instance);
        this.closedDisplayPos = displayPos;
        this.hingeOffset = hingeOffset;
        this.swingSign = swingSign;
        this.leaf = spawnDisplay(displayPos, LEAF_MODEL);
        this.closedInteractionPos = displayPos.sub(0, 1.5, 0);
        this.interaction = spawnInteraction(closedInteractionPos, 1.05f, 3f);
    }

    public static void spawnAll(Instance instance, List<RavengardDungeonConfig.DungeonObject> objects) {
        List<DungeonDoor> leaves = new ArrayList<>();
        for (RavengardDungeonConfig.DungeonObject object : objects) {
            Pos pos = new Pos(object.x(), object.y(), object.z(), (float) object.yaw(), 0f);
            if (object.type().equals("door_1_top")) {
                Entity top = new DungeonDoorTop(instance).spawnDisplay(pos, TOP_MODEL);
                continue;
            }
            if (!object.type().equals("door_1")) {
                continue;
            }
            leaves.add(new DungeonDoor(instance, pos, Vec.ZERO, 1));
        }
        for (int a = 0; a < leaves.size(); a++) {
            DungeonDoor door = leaves.get(a);
            if (door.partner != null) continue;
            DungeonDoor closest = null;
            double best = 1.6;
            for (int b = a + 1; b < leaves.size(); b++) {
                DungeonDoor other = leaves.get(b);
                if (other.partner != null) continue;
                double distance = door.closedDisplayPos.distance(other.closedDisplayPos);
                if (distance < best) {
                    best = distance;
                    closest = other;
                }
            }
            if (closest != null) {
                door.partner = closest;
                closest.partner = door;
            }
        }
        for (DungeonDoor door : leaves) {
            InteractableRegistry.register(door);
        }
    }

    private Vec hingeFromPartner() {
        if (partner == null) {
            double radians = Math.toRadians(closedDisplayPos.yaw());
            return new Vec(Math.cos(radians) * 0.5, 0, Math.sin(radians) * 0.5);
        }
        Vec away = Vec.fromPoint(closedDisplayPos.sub(partner.closedDisplayPos)).withY(0);
        return away.normalize().mul(0.5);
    }

    private int swingDirection() {
        if (partner == null) return 1;
        return closedDisplayPos.x() + closedDisplayPos.z()
                > partner.closedDisplayPos.x() + partner.closedDisplayPos.z() ? 1 : -1;
    }

    @Override
    public void open(Player player) {
        if (opened) return;
        swing(true);
        if (partner != null && !partner.opened) {
            partner.swing(true);
        }
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            swing(false);
            if (partner != null) {
                partner.swing(false);
            }
        }).delay(TaskSchedule.tick(OPEN_DURATION_TICKS)).schedule();
    }

    private void swing(boolean open) {
        opened = open;
        Vec hinge = hingeFromPartner();
        Pos hingePos = closedDisplayPos.add(hinge);
        int direction = swingDirection();
        for (int step = 1; step <= SWING_TICKS; step++) {
            int tick = step;
            MinecraftServer.getSchedulerManager().buildTask(() -> {
                double angle = Math.toRadians(90.0 * tick / SWING_TICKS * direction)
                        * (open ? 1 : -1) + (open ? 0 : Math.toRadians(90.0 * direction));
                double progressAngle = open
                        ? Math.toRadians(90.0 * tick / SWING_TICKS) * direction
                        : Math.toRadians(90.0 * (SWING_TICKS - tick) / SWING_TICKS) * direction;
                Vec arm = hinge.mul(-1);
                double cos = Math.cos(progressAngle), sin = Math.sin(progressAngle);
                Vec rotated = new Vec(arm.x() * cos - arm.z() * sin, 0,
                        arm.x() * sin + arm.z() * cos);
                float yaw = (float) (closedDisplayPos.yaw()
                        + Math.toDegrees(progressAngle));
                leaf.teleport(hingePos.add(rotated).withYaw(yaw));
                if (tick == SWING_TICKS) {
                    interaction.teleport(hingePos.add(rotated).sub(0, 1.5, 0).withYaw(yaw));
                }
            }).delay(TaskSchedule.tick(step)).schedule();
        }
    }

    private static final class DungeonDoorTop extends DungeonInteractable {
        private DungeonDoorTop(Instance instance) {
            super(instance);
        }

        @Override
        public void open(Player player) {
        }
    }
}
