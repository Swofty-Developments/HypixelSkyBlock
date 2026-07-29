package net.swofty.type.skyblockgeneric.entity.mob.ai;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Drop-in replacement for Minestom's {@code RandomStrollGoal} that wanders to a random nearby block,
 * but drives movement through the mob's {@link net.swofty.type.generic.entity.ai.vanilla.VanillaNavigator}
 * (our own A* + vanilla follow loop) rather than Minestom's built-in navigator.
 */
public class VanillaRandomStrollGoal extends GoalSelector {

    private static final long DELAY = 2500;

    private final int radius;
    private final List<Vec> closePositions;
    private final Random random = new Random();

    private long lastStroll;

    public VanillaRandomStrollGoal(@NotNull EntityCreature entityCreature, int radius) {
        super(entityCreature);
        this.radius = radius;
        this.closePositions = getNearbyBlocks(radius);
    }

    @Override
    public boolean shouldStart() {
        return System.currentTimeMillis() - lastStroll >= DELAY;
    }

    @Override
    public void start() {
        int remainingAttempt = closePositions.size();
        while (remainingAttempt-- > 0) {
            final int index = random.nextInt(closePositions.size());
            final Vec position = closePositions.get(index);

            final var target = entityCreature.getPosition().add(position);
            final boolean result = ((SkyBlockMob) entityCreature).getVanillaNavigator().pathTo(target);
            if (result) {
                break;
            }
        }
    }

    @Override
    public void tick(long time) {
    }

    @Override
    public boolean shouldEnd() {
        return true;
    }

    @Override
    public void end() {
        this.lastStroll = System.currentTimeMillis();
    }

    public int getRadius() {
        return radius;
    }

    private static @NotNull List<Vec> getNearbyBlocks(int radius) {
        List<Vec> blocks = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks.add(new Vec(x, y, z));
                }
            }
        }
        return blocks;
    }
}
