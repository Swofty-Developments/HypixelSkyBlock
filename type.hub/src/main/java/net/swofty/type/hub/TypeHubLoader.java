package net.swofty.type.hub;

import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.data.DustParticleData;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.hub.mobs.MobGraveyardZombie;
import net.swofty.type.hub.mobs.MobGraveyardZombieVillager;
import net.swofty.type.hub.mobs.MobRuinsWolf;
import net.swofty.type.hub.mobs.MobRuinsWolfOld;
import net.swofty.type.hub.runes.RuneEntityImpl;
import net.swofty.type.hub.tab.HubServerModule;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.SkyBlockTypeLoader;
import net.swofty.types.generic.bazaar.BazaarCategories;
import net.swofty.types.generic.entity.animalnpc.SkyBlockAnimalNPC;
import net.swofty.types.generic.entity.mob.MobRegistry;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.museum.MuseumDisplays;
import net.swofty.types.generic.protocol.bazaar.ProtocolInitializeBazaarCheck;
import net.swofty.types.generic.tab.TablistManager;
import net.swofty.types.generic.tab.TablistModule;
import net.swofty.types.generic.tab.modules.AccountInformationModule;
import net.swofty.types.generic.tab.modules.PlayersOnlineModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class TypeHubLoader implements SkyBlockTypeLoader {

    @Override
    public ServerType getType() {
        return ServerType.HUB;
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
                            Particle.DUST.id(),
                            true,
                            (float) pos.x(),
                            (float) pos.y(),
                            (float) pos.z(),
                            0,
                            0,
                            0,
                            0,
                            1,
                            new DustParticleData(new Color(153, 0, 255), 1)
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
        Map<String, Object> request = new HashMap<>();
        request.put("init-request", BazaarCategories.getInitializationRequest());
        bazaarService.callEndpoint(new ProtocolInitializeBazaarCheck(), request);

        /**
         * Register museum chunks
         */
        Logger.info("Registering museum chunks");
        MuseumDisplays.getAllPositions().forEach(displayPosition -> {
            SkyBlockConst.getInstanceContainer().loadChunk(displayPosition).join();
        });
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                (type) -> switch (type) {
                    default -> new Pos(-2.5, 72.5, -69.5, 180, 0);
                    case THE_FARMING_ISLANDS -> new Pos(74, 72, -180, 35, 0);
                }, // Spawn position
                true // Announce death messages
        );
    }

    public TablistManager getTablistManager() {
        return new TablistManager() {
            @Override
            public List<TablistModule> getModules() {
                return new ArrayList<>(List.of(
                        new PlayersOnlineModule(1),
                        new PlayersOnlineModule(2),
                        new HubServerModule(),
                        new AccountInformationModule()
                ));
            }
        };
    }

    @Override
    public List<SkyBlockEventClass> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.hub.events",
                SkyBlockEventClass.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<MobRegistry> getMobs() {
        return new ArrayList<>(List.of(
                new MobRegistry(EntityType.ZOMBIE, MobGraveyardZombie.class),
                new MobRegistry(EntityType.ZOMBIE_VILLAGER, MobGraveyardZombieVillager.class),
                new MobRegistry(EntityType.WOLF, MobRuinsWolfOld.class),
                new MobRegistry(EntityType.WOLF, MobRuinsWolf.class)
        ));
    }

    @Override
    public List<SkyBlockEventClass> getCustomEvents() {
        return new ArrayList<>();
    }

    @Override
    public List<SkyBlockNPC> getNPCs() {
        List<SkyBlockNPC> npcs = new ArrayList<>();

        npcs.addAll(SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.hub.npcs",
                SkyBlockNPC.class
        ).toList());

        return npcs;
    }

    @Override
    public List<ServiceType> getRequiredServices() {
        return List.of(ServiceType.AUCTION_HOUSE, ServiceType.BAZAAR, ServiceType.ITEM_TRACKER);
    }

    @Override
    public List<SkyBlockVillagerNPC> getVillagerNPCs() {
        return new ArrayList<>(SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.hub.villagers",
                SkyBlockVillagerNPC.class
        ).toList());
    }

    @Override
    public List<SkyBlockAnimalNPC> getAnimalNPCs() {
        return new ArrayList<>(SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.hub.animalnpcs",
                SkyBlockAnimalNPC.class
        ).toList());
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.HUB;
    }

}
