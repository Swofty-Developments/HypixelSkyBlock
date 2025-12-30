package net.swofty.type.thepark;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.SkyBlockTypeLoader;
import net.swofty.type.generic.entity.InteractionEntity;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.entity.TextDisplayEntity;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.jungle.MissionPlaceTraps;
import net.swofty.type.skyblockgeneric.tabmodules.AccountInformationModule;
import net.swofty.type.skyblockgeneric.tabmodules.SkyBlockPlayersOnlineModule;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.thepark.tab.TheParkServerModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeTheParkLoader implements SkyBlockTypeLoader {

    @Override
    public ServerType getType() {
        return ServerType.SKYBLOCK_THE_PARK;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        Logger.info("TypeTheParkLoader initialized!");
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        List<Point> trapBlocks = List.of(
                new BlockVec(-466, 119, -54),
                new BlockVec(-449, 120, -65),
                new BlockVec(-440, 122, -92)
        );

        for (Point trap : trapBlocks) {
            InteractionEntity hitbox = new InteractionEntity(1f, 1f, (p, event) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
                if (!player.getMissionData().isCurrentlyActive(MissionPlaceTraps.class)) {
                    return;
                }

            });

            TextDisplayEntity text = new TextDisplayEntity(Component.text("Place Trap Here", NamedTextColor.GREEN), meta -> {});
            hitbox.setInstance(HypixelConst.getInstanceContainer(), trap);
            text.setInstance(HypixelConst.getInstanceContainer(), trap.add(0, 1, 0));
        }

        MinecraftServer.getSchedulerManager().submitTask(() -> {
            for (SkyBlockPlayer player : SkyBlockGenericLoader.getLoadedPlayers()) {
                //if (!player.getMissionData().isCurrentlyActive(MissionPlaceTraps.class)) continue;
                for (Point position : trapBlocks) {
                    if (!(player.getPosition().distance(position) <= 25)) continue;
                    List<SendablePacket> packets = TrapParticles.getParticlePackets(position);
                    player.sendPackets(packets);
                }
            }
            return TaskSchedule.millis(400);
        }, ExecutionType.TICK_START);

        TrialOfFire.init();
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                (type) -> switch (type) {
                    case SKYBLOCK_GALATEA -> new Pos(-483.5, 117, -41.5, -120, 0);
                    default -> new Pos(-265.5, 79, -17.5, 90, 0);
                }, // Spawn position
                true // Announce death messages
        );
    }

    public TablistManager getTablistManager() {
        return new TablistManager() {
            @Override
            public List<TablistModule> getModules() {
                return new ArrayList<>(List.of(
                        new SkyBlockPlayersOnlineModule(1),
                        new SkyBlockPlayersOnlineModule(2),
                        new TheParkServerModule(),
                        new AccountInformationModule()
                ));
            }
        };
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.thepark.events",
                HypixelEventClass.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return new ArrayList<>();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return new ArrayList<>(SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.thepark.npcs",
                HypixelNPC.class
        ).toList());
    }

    @Override
    public List<ServiceToClient> getServiceRedisListeners() {
        return List.of();
    }

    @Override
    public List<ProxyToClient> getProxyRedisListeners() {
        return List.of();
    }

    @Override
    public List<ServiceType> getRequiredServices() {
        return new ArrayList<>(List.of(ServiceType.DATA_MUTEX));
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.SKYBLOCK_THE_PARK;
    }
}
