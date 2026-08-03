package net.swofty.type.ravengarddungeon.interactables;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.ravengarddungeon.config.RavengardDungeonConfig;

import java.util.List;

public final class DungeonDoor extends DungeonInteractable {
    private static final String DOOR_MODEL = "hypixel_ravengard:item/gameplay/doors/door_1";
    private static final String TOP_MODEL = "hypixel_ravengard:item/gameplay/doors/door_1_top";
    private static final int SWING_TICKS = 6;
    private static final int OPEN_DURATION_TICKS = 100;

    private final Pos closedDisplayPos;
    private final Entity door;
    private final Entity secondInteraction;
    private final Vec alongPlane;
    private boolean swinging;

    private DungeonDoor(Instance instance, Pos displayPos) {
        super(instance);
        this.closedDisplayPos = displayPos;
        this.door = spawnDisplay(displayPos, DOOR_MODEL);
        double radians = Math.toRadians(displayPos.yaw());
        this.alongPlane = new Vec(Math.cos(radians), 0, Math.sin(radians));
        Pos base = displayPos.sub(0, 1.5, 0);
        this.interaction = spawnInteraction(base.add(alongPlane.mul(0.5)), 1.05f, 3f);
        this.secondInteraction = spawnInteraction(base.sub(alongPlane.mul(0.5)), 1.05f, 3f);
    }

    public static void spawnAll(Instance instance, List<RavengardDungeonConfig.DungeonObject> objects) {
        for (RavengardDungeonConfig.DungeonObject object : objects) {
            Pos pos = new Pos(object.x(), object.y(), object.z(), object.yaw(), 0f);
            if (object.type().equals("door_1_top")) {
                new StaticPiece(instance).spawnDisplay(pos, TOP_MODEL);
            } else if (object.type().equals("door_1")) {
                DungeonDoor door = new DungeonDoor(instance, pos);
                InteractableRegistry.register(door);
                InteractableRegistry.registerExtraInteraction(door, door.secondInteraction);
            }
        }
    }

    @Override
    public void open(Player player) {
        if (opened || swinging) return;
        swing(true);
        MinecraftServer.getSchedulerManager().buildTask(() -> swing(false))
                .delay(TaskSchedule.tick(OPEN_DURATION_TICKS)).schedule();
    }

    private void swing(boolean open) {
        opened = open;
        swinging = true;
        Vec hinge = alongPlane.mul(1.0);
        Pos hingePos = closedDisplayPos.add(hinge);
        for (int step = 1; step <= SWING_TICKS; step++) {
            int tick = step;
            MinecraftServer.getSchedulerManager().buildTask(() -> {
                double fraction = open ? (double) tick / SWING_TICKS
                        : (double) (SWING_TICKS - tick) / SWING_TICKS;
                double angle = Math.toRadians(90.0 * fraction);
                Vec arm = hinge.mul(-1);
                double cos = Math.cos(angle), sin = Math.sin(angle);
                Vec rotated = new Vec(arm.x() * cos - arm.z() * sin, 0,
                        arm.x() * sin + arm.z() * cos);
                float yaw = (float) (closedDisplayPos.yaw() + Math.toDegrees(angle));
                door.teleport(hingePos.add(rotated).withYaw(yaw));
                if (tick == SWING_TICKS) {
                    swinging = false;
                    Pos base = hingePos.add(rotated).sub(0, 1.5, 0).withYaw(yaw);
                    Vec openPlane = new Vec(rotated.x(), 0, rotated.z()).normalize();
                    interaction.teleport(base.add(openPlane.mul(0.5)));
                    secondInteraction.teleport(base.sub(openPlane.mul(0.5)));
                }
            }).delay(TaskSchedule.tick(step)).schedule();
        }
    }

    private static final class StaticPiece extends DungeonInteractable {
        private StaticPiece(Instance instance) {
            super(instance);
        }

        @Override
        public void open(Player player) {
        }
    }
}
