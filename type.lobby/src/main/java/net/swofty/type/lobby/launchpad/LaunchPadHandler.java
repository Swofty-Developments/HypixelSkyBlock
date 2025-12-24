package net.swofty.type.lobby.launchpad;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LaunchPadHandler {
    private static final List<LaunchPad> launchPads = new ArrayList<>();

    public static void register(Scheduler scheduler, List<LaunchPad> pads) {
        launchPads.clear();
        launchPads.addAll(pads);

        if (launchPads.isEmpty()) return;

        scheduler.scheduleTask(() -> {
            for (LaunchPad launchPad : launchPads) {
                for (Pos slimeBlock : launchPad.getSlimeBlocks()) {
                    HypixelGenericLoader.getLoadedPlayers().forEach(player -> {
                        if (slimeBlock.distance(player.getPosition()) <= 15) {
                            player.sendPacket(new ParticlePacket(
                                    Particle.COMPOSTER,
                                    false,
                                    false,
                                    slimeBlock.x(),
                                    slimeBlock.y() + 1,
                                    slimeBlock.z(),
                                    0.1f,
                                    0.1f,
                                    0.1f,
                                    0f,
                                    3
                            ));
                        }
                    });
                }
            }
        }, TaskSchedule.seconds(2), TaskSchedule.tick(5), ExecutionType.TICK_END);
    }

    public static @Nullable LaunchPad getLaunchPadInRange(Pos pos, double range) {
        if (launchPads.isEmpty()) return null;

        for (LaunchPad launchPad : launchPads) {
            for (Pos slimeBlock : launchPad.getSlimeBlocks()) {
                if (slimeBlock.distance(pos) <= range) {
                    return launchPad;
                }
            }
        }
        return null;
    }

    public static List<Pos> getSlimeBlocksNear(Pos pos) {
        Instance instance = HypixelConst.getInstanceContainer();
        if (instance == null) return new ArrayList<>();
        List<Pos> toReturn = new ArrayList<>();

        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                for (int y = -5; y <= 5; y++) {
                    Pos check = new Pos(pos.x() + x, pos.y() + y, pos.z() + z);
                    instance.loadChunk(check).join();

                    if (instance.getBlock(check) == Block.SLIME_BLOCK)
                        toReturn.add(check);
                    if (instance.getBlock(check) == Block.BARRIER)
                        instance.setBlock(check, Block.AIR);
                }
            }
        }

        return toReturn;
    }
}
