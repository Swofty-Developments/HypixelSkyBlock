package net.swofty.type.hub;

import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.SkyBlockTypeLoader;
import net.swofty.type.generic.entity.animalnpc.HypixelAnimalNPC;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.villager.HypixelVillagerNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.hub.runes.RuneEntityImpl;
import net.swofty.type.hub.tab.HubServerModule;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.museum.MuseumDisplays;
import net.swofty.type.skyblockgeneric.tabmodules.AccountInformationModule;
import net.swofty.type.skyblockgeneric.tabmodules.SkyBlockPlayersOnlineModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class TypeHubLoader implements SkyBlockTypeLoader {

    @Override
    public ServerType getType() {
        return ServerType.SKYBLOCK_HUB;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        Logger.info("TypeHubLoader initialized!");
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        RuneEntityImpl firstStone = new RuneEntityImpl(new Pos(-37.2, 68.40, -129.15, 0, 0f), false);
        RuneEntityImpl secondStone = new RuneEntityImpl(new Pos( -37.82, 68.40, -129.15, 0, 0f), false);
        RuneEntityImpl thirdStone = new RuneEntityImpl(new Pos(-37.2, 68.40, -129.775, 0, 0f), false);
        RuneEntityImpl fourthStone = new RuneEntityImpl(new Pos(-37.82, 68.40, -129.775, 0, 0f), false);
        RuneEntityImpl head = new RuneEntityImpl(new Pos(-37.5, 69.20, -129.4, -70, 0f), true);
        Pos runePos = new Pos(-37.5, 70, -129.5, 0, 0f);

        AtomicReference<Double> i = new AtomicReference<>(0D);
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            List<Pos> locationsToDisplayParticle = new ArrayList<>();
            for (double x = 0; x < 1.25; x = x + 0.25) {
                locationsToDisplayParticle.add(new Pos(runePos.x() + Math.cos(i.get() - x) * 2.5, runePos.y() - x, runePos.z() + Math.sin(i.get() - x) * 2.5));
                locationsToDisplayParticle.add(new Pos(runePos.x() - Math.cos(i.get() - x) * 2.5, runePos.y() + x, runePos.z() - Math.sin(i.get() - x) * 2.5));
            }

            for (Pos pos : locationsToDisplayParticle) {
                SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                    player.sendPacket(new ParticlePacket(
                            Particle.DUST.withColor(new Color(153, 0, 255)),
                            false,
                            true,
                            (float) pos.x(),
                            (float) pos.y(),
                            (float) pos.z(),
                            0,
                            0,
                            0,
                            0,
                            1
                    ));
                });
            }
            i.updateAndGet(v -> v + 1 / 10f);
        }, TaskSchedule.seconds(2), TaskSchedule.tick(1));

        /**
         * Register bazaar cache
         */
        Logger.info("Registering bazaar cache");
        ProxyService bazaarService = new ProxyService(ServiceType.BAZAAR);

        /**
         * Register museum chunks
         */
        Logger.info("Registering museum chunks");
        MuseumDisplays.getAllPositions().forEach(displayPosition -> {
            HypixelConst.getInstanceContainer().loadChunk(displayPosition).join();
        });
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                (type) -> switch (type) {
                    case THE_FARMING_ISLANDS -> new Pos(74, 72, -180, 35, 0);
                    case DUNGEON_HUB -> new Pos(-44, 88, 11.5, 0, 0);
                    default -> new Pos(-2.5, 72.5, -69.5, 180, 0);
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
                        new HubServerModule(),
                        new AccountInformationModule()
                ));
            }
        };
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.hub.events",
                HypixelEventClass.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return new ArrayList<>();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        List<HypixelNPC> npcs = new ArrayList<>();

        npcs.addAll(SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.hub.npcs",
                HypixelNPC.class
        ).toList());

        return npcs;
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
        return List.of(ServiceType.AUCTION_HOUSE, ServiceType.BAZAAR, ServiceType.ITEM_TRACKER, ServiceType.DATA_MUTEX);
    }

    @Override
    public List<HypixelVillagerNPC> getVillagerNPCs() {
        return new ArrayList<>(SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.hub.villagers",
                HypixelVillagerNPC.class
        ).toList());
    }

    @Override
    public List<HypixelAnimalNPC> getAnimalNPCs() {
        return new ArrayList<>(SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.hub.animalnpcs",
                HypixelAnimalNPC.class
        ).toList());
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.SKYBLOCK_HUB;
    }
}
