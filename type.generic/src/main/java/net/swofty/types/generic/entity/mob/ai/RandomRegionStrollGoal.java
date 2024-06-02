package net.swofty.types.generic.entity.mob.ai;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.instance.block.Block;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.region.SkyBlockRegion;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomRegionStrollGoal extends GoalSelector {

    private static final long DELAY = 2500;

    private final List<Vec> closePositions;
    private final Random random = new Random();
    private final RegionType type;

    private long lastStroll;

    public RandomRegionStrollGoal(@NotNull EntityCreature entityCreature, int radius, RegionType type) {
        super(entityCreature);
        this.type = type;
        this.closePositions = getNearbyBlocks(radius);
    }

    @Override
    public boolean shouldStart() {
        return System.currentTimeMillis() - lastStroll >= DELAY;
    }

    @Override
    public void start() {
        int remainingAttempt = closePositions.size();

        if (remainingAttempt == 0) {
            end();
            return;
        }

        while (remainingAttempt-- > 0) {
            final int index = random.nextInt(closePositions.size());
            final Vec position = closePositions.get(index);

            final var target = entityCreature.getPosition().add(position);
            final boolean result = entityCreature.getNavigator().setPathTo(target);
            if (result) {
                break;
            }
        }
    }

    @Override
    public void tick(long time) {}

    @Override
    public boolean shouldEnd() {
        return true;
    }

    @Override
    public void end() {
        this.lastStroll = System.currentTimeMillis();
    }

    private @NotNull List<Vec> getNearbyBlocks(int radius) {
        List<Vec> blocks = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                int entityX = getEntityCreature().getPosition().blockX() + x;
                int y = getEntityCreature().getPosition().blockY();
                int entityZ = getEntityCreature().getPosition().blockZ()  + z;

                if (entityCreature.getInstance() != null) {
                    if (!entityCreature.getInstance().isChunkLoaded(new Pos(entityX, y, entityZ)))
                        entityCreature.getInstance().loadChunk(new Pos(entityX, y, entityZ)).join();
                    Block block = entityCreature.getInstance().getBlock(entityX, y, entityZ);

                    if (!block.isAir()) continue;
                }

                SkyBlockRegion region = SkyBlockRegion.getRegionOfPosition(new Pos(entityX, y, entityZ));

                if (region == null)
                    continue;

                if (region.getType() == type)
                    blocks.add(new Vec(x, y, z));
            }
        }

        return blocks;
    }
}