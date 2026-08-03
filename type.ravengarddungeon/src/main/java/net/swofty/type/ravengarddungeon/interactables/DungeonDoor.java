package net.swofty.type.ravengarddungeon.interactables;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
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

    private final List<Leaf> leaves = new ArrayList<>();
    private final List<Entity> hitboxes = new ArrayList<>();
    private final List<Pos> hitboxHomes = new ArrayList<>();
    private boolean swinging;

    private record Leaf(Entity display, Pos closedPos, int swingSign) {
    }

    private DungeonDoor(Instance instance) {
        super(instance);
    }

    public static void spawnAll(Instance instance, List<RavengardDungeonConfig.DungeonObject> objects) {
        List<RavengardDungeonConfig.DungeonObject> pending = new ArrayList<>();
        for (RavengardDungeonConfig.DungeonObject object : objects) {
            if (object.type().equals("door_1_top")) {
                new StaticPiece(instance).spawnDisplay(
                        new Pos(object.x(), object.y(), object.z(), object.yaw(), 0f), TOP_MODEL);
            } else if (object.type().equals("door_1")) {
                pending.add(object);
            }
        }

        boolean[] used = new boolean[pending.size()];
        for (int a = 0; a < pending.size(); a++) {
            if (used[a]) continue;
            used[a] = true;
            List<RavengardDungeonConfig.DungeonObject> cluster = new ArrayList<>();
            cluster.add(pending.get(a));
            for (int b = a + 1; b < pending.size(); b++) {
                if (used[b]) continue;
                RavengardDungeonConfig.DungeonObject other = pending.get(b);
                if (Math.abs(other.x() - pending.get(a).x()) <= 2.5
                        && Math.abs(other.z() - pending.get(a).z()) <= 2.5
                        && Math.abs(other.y() - pending.get(a).y()) <= 2.5) {
                    used[b] = true;
                    cluster.add(other);
                }
            }
            spawnDoorway(instance, cluster);
        }
    }

    private static void spawnDoorway(Instance instance,
                                     List<RavengardDungeonConfig.DungeonObject> cluster) {
        DungeonDoor door = new DungeonDoor(instance);
        for (RavengardDungeonConfig.DungeonObject object : cluster) {
            Pos pos = new Pos(object.x(), object.y(), object.z(), object.yaw(), 0f);
            Entity display = door.spawnDisplay(pos, LEAF_MODEL);
            door.leaves.add(new Leaf(display, pos, 1));

            Pos base = pos.sub(0, 1.5, 0);
            Entity hitbox = door.spawnInteraction(base, 1.05f, 3f);
            door.hitboxes.add(hitbox);
            door.hitboxHomes.add(base);
            if (door.interaction == null) {
                door.interaction = hitbox;
                InteractableRegistry.register(door);
            } else {
                InteractableRegistry.registerExtraInteraction(door, hitbox);
            }
        }
    }

    @Override
    public String castLabel() {
        return "Opening Door";
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
        for (int step = 1; step <= SWING_TICKS; step++) {
            int tick = step;
            MinecraftServer.getSchedulerManager().buildTask(() -> {
                double fraction = open ? (double) tick / SWING_TICKS
                        : (double) (SWING_TICKS - tick) / SWING_TICKS;
                for (Leaf leaf : leaves) {
                    float yaw = (float) (leaf.closedPos().yaw() + 105.0 * fraction * leaf.swingSign());
                    leaf.display().teleport(leaf.closedPos().withYaw(yaw));
                }
                if (tick == SWING_TICKS) {
                    swinging = false;
                    for (int index = 0; index < hitboxes.size(); index++) {
                        Pos home = hitboxHomes.get(index);
                        hitboxes.get(index).teleport(open ? home.sub(0, 500, 0) : home);
                    }
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
