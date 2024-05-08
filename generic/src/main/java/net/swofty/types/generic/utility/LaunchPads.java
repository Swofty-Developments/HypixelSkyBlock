package net.swofty.types.generic.utility;

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
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.entity.hologram.PlayerHolograms;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
public enum LaunchPads {
    VILLAGE_TO_FARMING(getSlimeBlocksNear(new Pos(79, 71, -185)), ServerType.HUB,
            new Pos(116.5, 74, -210.5), (player) -> {
                player.sendTo(ServerType.THE_FARMING_ISLANDS);
            }, (player) -> player.getSkills().getCurrentLevel(SkillCategories.FARMING) >= 5,
            "§cYou must be at least Farming Level V to join this island!",
            (player) -> {
                boolean hasRequirement = player.getSkills().getCurrentLevel(SkillCategories.FARMING) >= 5;
                if (!hasRequirement) {
                    return PlayerHolograms.ExternalPlayerHologram.builder()
                            .pos(new Pos(80, 73.3, -184.5))
                            .player(player)
                            .text(new String[]{"§bTravel To:", "§aThe Farming Islands", "§cRequires Farming Level 5"})
                            .build();
                } else {
                    return PlayerHolograms.ExternalPlayerHologram.builder()
                            .pos(new Pos(80, 73.3, -184.5))
                            .player(player)
                            .text(new String[]{"§bTravel To:", "§aThe Farming Islands"})
                            .build();
                }
    }),
    FARMING_TO_VILLAGE(getSlimeBlocksNear(new Pos(111, 71, -202)), ServerType.THE_FARMING_ISLANDS,
            new Pos(74, 72, -180), (player) -> {
                player.sendTo(ServerType.HUB);
            }, (player) -> true,
            "",
            (player) -> {
                return PlayerHolograms.ExternalPlayerHologram.builder()
                        .pos(new Pos(111.5, 71.7, -202.5))
                        .player(player)
                        .text(new String[]{"§bTravel To:", "§aThe Village"})
                        .build();
            });
    ;

    private static final List<LaunchPads> launchPads = new ArrayList<>();

    private final List<Pos> slimeBlocks;
    private final ServerType serverType;
    private final Pos destination;
    private final Consumer<SkyBlockPlayer> afterFinished;
    private final Function<SkyBlockPlayer, Boolean> shouldAllow;
    private final String rejectionMessage;
    private final Function<SkyBlockPlayer, PlayerHolograms.ExternalPlayerHologram> hologramDisplay;

    LaunchPads(List<Pos> slimeBlocks, ServerType serverType, Pos destination,
               Consumer<@NonNull SkyBlockPlayer> afterFinished, Function<SkyBlockPlayer, Boolean> shouldAllow, String rejectionMessage,
               Function<@NonNull SkyBlockPlayer, PlayerHolograms.ExternalPlayerHologram> hologramDisplay) {
        this.slimeBlocks = slimeBlocks;
        this.serverType = serverType;
        this.destination = destination;
        this.afterFinished = afterFinished;
        this.shouldAllow = shouldAllow;
        this.rejectionMessage = rejectionMessage;
        this.hologramDisplay = hologramDisplay;
    }

    public static void register(Scheduler scheduler) {
        launchPads.addAll(Arrays.asList(LaunchPads.values()));
        launchPads.removeIf(launchPad -> launchPad.serverType != SkyBlockConst.getTypeLoader().getType());
        Map<UUID, PlayerHolograms.ExternalPlayerHologram> hologramMap = new HashMap<>();

        scheduler.scheduleTask(() -> {
            for (LaunchPads launchPad : launchPads) {
                List<UUID> updated = new ArrayList<>();
                SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                    if (hologramMap.containsKey(player.getUuid()))
                        PlayerHolograms.removeExternalPlayerHologram(hologramMap.get(player.getUuid()));

                    PlayerHolograms.ExternalPlayerHologram hologram = launchPad.hologramDisplay.apply(player);
                    hologramMap.put(player.getUuid(), hologram);
                    PlayerHolograms.addExternalPlayerHologram(hologram);
                    updated.add(player.getUuid());
                });

                hologramMap.keySet().removeIf(uuid -> !updated.contains(uuid));
            }
        }, TaskSchedule.seconds(2), TaskSchedule.seconds(2), ExecutionType.ASYNC);

        scheduler.scheduleTask(() -> {
            for (LaunchPads launchPad : launchPads) {
                for (Pos slimeBlock : launchPad.getSlimeBlocks()) {
                    SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                        if (slimeBlock.distance(player.getPosition()) <= 15) {
                            player.sendPacket(new ParticlePacket(
                                    Particle.COMPOSTER.id(),
                                    false,
                                    slimeBlock.x(),
                                    slimeBlock.y() + 1,
                                    slimeBlock.z(),
                                    0.1f,
                                    0.1f,
                                    0.1f,
                                    0f,
                                    3,
                                    null
                            ));
                        }
                    });
                }
            }
        }, TaskSchedule.seconds(2), TaskSchedule.tick(5), ExecutionType.ASYNC);
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
        Instance instance = SkyBlockConst.getInstanceContainer();
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
