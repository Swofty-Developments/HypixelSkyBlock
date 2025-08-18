package net.swofty.type.bedwarslobby.util;

import lombok.Getter;
import lombok.NonNull;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

@Getter
public enum LaunchPads {
    SPAWN(getSlimeBlocksNear(new Pos(-30, 66, 0)), ServerType.BEDWARS_LOBBY,
            new Pos(-10.5, 67, 0.5), (player) -> {}, "");
    ;

    private static final List<LaunchPads> launchPads = new ArrayList<>();

    private final List<Pos> slimeBlocks;
    private final ServerType serverType;
    private final Pos destination;
    private final Consumer<HypixelPlayer> afterFinished;
    private final String rejectionMessage;

    LaunchPads(List<Pos> slimeBlocks, ServerType serverType, Pos destination,
			   Consumer<@NonNull HypixelPlayer> afterFinished, String rejectionMessage) {
        this.slimeBlocks = slimeBlocks;
        this.serverType = serverType;
        this.destination = destination;
        this.afterFinished = afterFinished;
        this.rejectionMessage = rejectionMessage;
    }

    public static void register(Scheduler scheduler) {
        launchPads.addAll(Arrays.asList(LaunchPads.values()));
        launchPads.removeIf(launchPad -> launchPad.serverType != HypixelConst.getTypeLoader().getType());

        scheduler.scheduleTask(() -> {
            for (LaunchPads launchPad : launchPads) {
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

    public static @Nullable LaunchPads getLaunchPadInRange(Pos pos, double range) {
        if (launchPads.isEmpty()) return null;

        for (LaunchPads launchPad : launchPads) {
            for (Pos slimeBlock : launchPad.getSlimeBlocks()) {
                if (slimeBlock.distance(pos) <= range) {
                    return launchPad;
                }
            }
        }
        return null;
    }

    private static List<Pos> getSlimeBlocksNear(Pos pos) {
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
