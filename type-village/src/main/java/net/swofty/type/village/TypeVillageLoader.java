package net.swofty.type.village;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.village.mobs.hub.MobGraveyardZombie;
import net.swofty.type.village.mobs.hub.MobGraveyardZombieVillager;
import net.swofty.type.village.mobs.hub.MobRuinsWolf;
import net.swofty.type.village.mobs.hub.MobRuinsWolfOld;
import net.swofty.type.village.mobs.park.HowlingSpirit;
import net.swofty.type.village.mobs.park.PackSpirit;
import net.swofty.type.village.mobs.park.SoulOfTheAlpha;
import net.swofty.type.village.mobs.spiderden.*;
import net.swofty.type.village.runes.RuneEntityImpl;
import net.swofty.type.village.tab.VillageServerModule;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.SkyBlockTypeLoader;
import net.swofty.types.generic.bazaar.BazaarCategories;
import net.swofty.types.generic.entity.mob.MobRegistry;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.protocol.bazaar.ProtocolInitializeBazaarCheck;
import net.swofty.types.generic.tab.TablistManager;
import net.swofty.types.generic.tab.TablistModule;
import net.swofty.types.generic.tab.modules.AccountInformationModule;
import net.swofty.types.generic.tab.modules.PlayersOnlineModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class TypeVillageLoader implements SkyBlockTypeLoader {

    @Override
    public ServerType getType() {
        return ServerType.VILLAGE;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        Logger.info("TypeVillageLoader initialized!");
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        RuneEntityImpl firstStone = new RuneEntityImpl(new Pos(-37.2, 68.40, -129.15, 0, 0f), false);
        RuneEntityImpl secondStone = new RuneEntityImpl(new Pos( -37.82, 68.40, -129.15, 0, 0f), false);
        RuneEntityImpl thirdStone = new RuneEntityImpl(new Pos(-37.2, 68.40, -129.775, 0, 0f), false);
        RuneEntityImpl fourthStone = new RuneEntityImpl(new Pos(-37.82, 68.40, -129.775, 0, 0f), false);
        RuneEntityImpl head = new RuneEntityImpl(new Pos(-37.5, 69.20, -129.4, -70, 0f), true);

        /**
         * Register bazaar cache
         */
        Logger.info("Registering bazaar cache");
        ProxyService bazaarService = new ProxyService(ServiceType.BAZAAR);
        Map<String, Object> request = new HashMap<>();
        request.put("init-request", BazaarCategories.getInitializationRequest());
        bazaarService.callEndpoint(new ProtocolInitializeBazaarCheck(), request);
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                new Pos(-2.5, 72.5, -69.5, 180, 0), // Spawn position
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
                        new VillageServerModule(),
                        new AccountInformationModule()
                ));
            }
        };
    }

    @Override
    public List<SkyBlockEvent> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.village.events",
                SkyBlockEvent.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<MobRegistry> getMobs() {
        return new ArrayList<>(List.of(
                //hub
                new MobRegistry(EntityType.ZOMBIE, MobGraveyardZombie.class),
                new MobRegistry(EntityType.ZOMBIE_VILLAGER, MobGraveyardZombieVillager.class),
                new MobRegistry(EntityType.WOLF, MobRuinsWolfOld.class),
                new MobRegistry(EntityType.WOLF, MobRuinsWolf.class),

                //park
                new MobRegistry(EntityType.WOLF, HowlingSpirit.class),
                new MobRegistry(EntityType.WOLF, PackSpirit.class),
                new MobRegistry(EntityType.WOLF, SoulOfTheAlpha.class),

                //spiderden
                new MobRegistry(EntityType.SPIDER, DasherSpider.class),
                new MobRegistry(EntityType.SPIDER, SplitterSpider.class),
                new MobRegistry(EntityType.SPIDER, WeaverSpider.class),
                new MobRegistry(EntityType.SPIDER, VoraciousSpider.class),
                new MobRegistry(EntityType.SKELETON, Skeleton.class),
                new MobRegistry(EntityType.SILVERFISH, SilverFish.class),
                new MobRegistry(EntityType.SLIME, RainSlime.class)
        ));
    }

    @Override
    public List<SkyBlockEvent> getCustomEvents() {
        return new ArrayList<>();
    }

    @Override
    public List<SkyBlockNPC> getNPCs() {
        List<SkyBlockNPC> npcs = new ArrayList<>();

        npcs.addAll(SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.village.npcs",
                SkyBlockNPC.class
        ).toList());

        return npcs;
    }

    @Override
    public List<ServiceType> getRequiredServices() {
        return List.of(ServiceType.AUCTION_HOUSE, ServiceType.BAZAAR);
    }

    @Override
    public List<SkyBlockVillagerNPC> getVillagerNPCs() {
        List<SkyBlockVillagerNPC> npcs = new ArrayList<>();

        npcs.addAll(SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.village.villagers",
                NPCVillagerDialogue.class
        ).toList());

        return npcs;
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.HUB;
    }

}
