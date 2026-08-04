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
        boolean axisAlongX = cluster.size() < 2
                || Math.abs(cluster.get(1).x() - cluster.get(0).x())
                        >= Math.abs(cluster.get(1).z() - cluster.get(0).z());
        Float referenceYaw = null;
        double sumX = 0, sumY = 0, sumZ = 0;
        double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;
        for (RavengardDungeonConfig.DungeonObject object : cluster) {
            float closedYaw = closedYaw(object.yaw(), axisAlongX);
            if (referenceYaw == null) {
                referenceYaw = closedYaw;
            }
            Pos pos = new Pos(object.x(), object.y(), object.z(), closedYaw, 0f);
            Entity display = door.spawnDisplay(pos, LEAF_MODEL);
            door.leaves.add(new Leaf(display, pos, closedYaw == referenceYaw ? 1 : -1));
            sumX += object.x(); sumY += object.y(); sumZ += object.z();
            minX = Math.min(minX, object.x()); maxX = Math.max(maxX, object.x());
            minZ = Math.min(minZ, object.z()); maxZ = Math.max(maxZ, object.z());
        }
        double span = Math.max(maxX - minX, maxZ - minZ);
        float width = (float) Math.max(2.2, span + 1.6);
        Pos center = new Pos(sumX / cluster.size(), sumY / cluster.size() - 1.5, sumZ / cluster.size());
        door.interaction = door.spawnInteraction(center, width, 3.2f);
        InteractableRegistry.register(door);
    }

    private static float closedYaw(float configYaw, boolean axisAlongX) {
        float[] options = axisAlongX ? new float[]{0f, 180f} : new float[]{90f, 270f};
        float normalized = ((configYaw % 360f) + 360f) % 360f;
        float best = options[0];
        float bestDelta = 360f;
        for (float option : options) {
            float delta = Math.abs(((normalized - option + 540f) % 360f) - 180f);
            if (delta < bestDelta) {
                bestDelta = delta;
                best = option;
            }
        }
        return best;
    }

    @Override
    public String castLabel() {
        return opened ? "Closing Door" : "Opening Door";
    }

    @Override
    public boolean allowReactivation() {
        return true;
    }

    @Override
    public void open(Player player) {
        if (swinging) return;
        swing(!opened);
    }

    private final List<net.minestom.server.coordinate.Point> clearedBarriers = new ArrayList<>();

    private void toggleBarriers(boolean open) {
        if (open) {
            double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
            double minZ = Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;
            double baseY = Double.MAX_VALUE;
            for (Leaf leaf : leaves) {
                minX = Math.min(minX, leaf.closedPos().x());
                maxX = Math.max(maxX, leaf.closedPos().x());
                minZ = Math.min(minZ, leaf.closedPos().z());
                maxZ = Math.max(maxZ, leaf.closedPos().z());
                baseY = Math.min(baseY, leaf.closedPos().y() - 1.5);
            }
            for (int blockX = (int) Math.floor(minX - 0.6); blockX <= (int) Math.floor(maxX + 0.6); blockX++) {
                for (int blockZ = (int) Math.floor(minZ - 0.6); blockZ <= (int) Math.floor(maxZ + 0.6); blockZ++) {
                    for (int blockY = (int) baseY; blockY <= (int) baseY + 3; blockY++) {
                        try {
                            if (instance.getBlock(blockX, blockY, blockZ)
                                    == net.minestom.server.instance.block.Block.BARRIER) {
                                instance.setBlock(blockX, blockY, blockZ,
                                        net.minestom.server.instance.block.Block.AIR);
                                clearedBarriers.add(new net.minestom.server.coordinate.Vec(blockX, blockY, blockZ));
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        } else {
            for (var point : clearedBarriers) {
                instance.setBlock(point.blockX(), point.blockY(), point.blockZ(),
                        net.minestom.server.instance.block.Block.BARRIER);
            }
            clearedBarriers.clear();
        }
    }

    private void swing(boolean open) {
        opened = open;
        swinging = true;
        toggleBarriers(open);
        for (int step = 1; step <= SWING_TICKS; step++) {
            int tick = step;
            MinecraftServer.getSchedulerManager().buildTask(() -> {
                double fraction = open ? (double) tick / SWING_TICKS
                        : (double) (SWING_TICKS - tick) / SWING_TICKS;
                for (Leaf leaf : leaves) {
                    float yaw = (float) (leaf.closedPos().yaw() + 90.0 * fraction * leaf.swingSign());
                    leaf.display().teleport(leaf.closedPos().withYaw(yaw));
                }
                if (tick == SWING_TICKS) {
                    swinging = false;
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
