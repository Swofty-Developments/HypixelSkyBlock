package net.swofty.types.generic;

import lombok.Getter;
import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import net.swofty.commons.Configuration;
import net.swofty.commons.CustomWorlds;
import net.swofty.types.generic.calendar.SkyBlockCalendar;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.mongodb.*;
import net.swofty.types.generic.entity.DroppedItemEntityImpl;
import net.swofty.types.generic.entity.hologram.PlayerHolograms;
import net.swofty.types.generic.entity.hologram.ServerHolograms;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.set.impl.SetRepeatable;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.MissionRepeater;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.packet.SkyBlockPacketClientListener;
import net.swofty.types.generic.region.SkyBlockMiningConfiguration;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.server.attribute.SkyBlockServerAttributes;
import net.swofty.types.generic.server.eventcaller.CustomEventCaller;
import net.swofty.types.generic.user.SkyBlockIsland;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.SkyBlockScoreboard;
import net.swofty.types.generic.user.categories.CustomGroups;
import net.swofty.types.generic.user.fairysouls.FairySoul;
import net.swofty.types.generic.user.statistics.PlayerStatistics;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public record SkyBlockGenericLoader(SkyBlockTypeLoader typeLoader) {
    @Getter
    private static MinecraftServer server;

    @SneakyThrows
    public void initialize(MinecraftServer server) {
        SkyBlockGenericLoader.server = server;
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        SkyBlockConst.setTypeLoader(typeLoader);

        /**
         * Handle instances
         */
        InstanceContainer temporaryInstance = instanceManager.createInstanceContainer();
        temporaryInstance.setChunkLoader(new AnvilLoader(CustomWorlds.HUB.getFolderName()));

        SkyBlockConst.setInstanceContainer(instanceManager.createSharedInstance(temporaryInstance));
        SkyBlockConst.setEventHandler(MinecraftServer.getGlobalEventHandler());

        /**
         * Register database
         */
        new ProfilesDatabase("_placeHolder").connect(Configuration.get("mongodb"));
        new RegionDatabase("_placeHolder").connect(Configuration.get("mongodb"));
        new IslandDatabase("_placeHolder").connect(Configuration.get("mongodb"));
        FairySoulDatabase.connect(Configuration.get("mongodb"));
        AttributeDatabase.connect(Configuration.get("mongodb"));
        UserDatabase.connect(Configuration.get("mongodb"));
        CoopDatabase.connect(Configuration.get("mongodb"));

        /**
         * Register commands
         */
        MinecraftServer.getCommandManager().setUnknownCommandCallback((sender, command) -> {
            // Large amount of Clients (such as Lunar) send a `/tip all` when joining
            // due to the scoreboard containing `hypixel.net`
            if (command.startsWith("tip ")) return;
            sender.sendMessage("§fUnknown command. Type \"/help\" for help. ('" + command + "')");
        });
        loopThroughPackage("net.swofty.types.generic.command.commands", SkyBlockCommand.class).forEach(command -> {
            MinecraftServer.getCommandManager().register(command.getCommand());
        });

        /**
         * Register NPCs
         */
        loopThroughPackage("net.swofty.types.generic.entity.npc.npcs", SkyBlockNPC.class)
                .forEach(SkyBlockNPC::register);
        loopThroughPackage("net.swofty.types.generic.entity.npc.npcs", NPCDialogue.class)
                .forEach(SkyBlockNPC::register);
        loopThroughPackage("net.swofty.types.generic.entity.villager.villagers", SkyBlockVillagerNPC.class)
                .forEach(SkyBlockVillagerNPC::register);
        loopThroughPackage("net.swofty.types.generic.entity.villager.villagers", NPCVillagerDialogue.class)
                .forEach(SkyBlockVillagerNPC::register);

        /**
         * Handle server attributes
         */
        SkyBlockCalendar.tick(MinecraftServer.getSchedulerManager());
        SkyBlockServerAttributes.loadAttributes(AttributeDatabase.getDocument("attributes"));
        SkyBlockServerAttributes.saveAttributeLoop();

        /**
         * Register packet events
         */
        loopThroughPackage("net.swofty.types.generic.packet.packets.client", SkyBlockPacketClientListener.class)
                .forEach(SkyBlockPacketClientListener::cacheListener);
        loopThroughPackage("net.swofty.types.generic.packet.packets.server", SkyBlockPacketClientListener.class)
                .forEach(SkyBlockPacketClientListener::cacheListener);
        SkyBlockPacketClientListener.register(SkyBlockConst.getEventHandler());
        SkyBlockPacketClientListener.register(SkyBlockConst.getEventHandler());

        /**
         * Load regions
         */
        SkyBlockRegion.cacheRegions();
        SkyBlockMiningConfiguration.startRepeater(MinecraftServer.getSchedulerManager());
        MinecraftServer.getDimensionTypeManager().addDimension(
                DimensionType.builder(NamespaceID.from("skyblock:island"))
                        .ambientLight(2)
                        .build());
        SkyBlockIsland.runVacantLoop(MinecraftServer.getSchedulerManager());

        /**
         * Load fairy souls
         */
        FairySoul.cacheFairySouls();

        /**
         * Create audiences
         */
        CustomGroups.registerAudiences();
        PlayerStatistics.run();

        /**
         * Start repeaters
         */
        SkyBlockScoreboard.start();
        PlayerHolograms.updateAll(MinecraftServer.getSchedulerManager());

        /**
         * Register holograms and fairy souls
         */
        ServerHolograms.spawnAll(SkyBlockConst.getInstanceContainer());
        FairySoul.spawnEntities(SkyBlockConst.getInstanceContainer());

        /**
         * Register items
         */
        ItemAttribute.registerItemAttributes();
        PlayerItemUpdater.updateLoop(MinecraftServer.getSchedulerManager());
        DroppedItemEntityImpl.spinLoop();

        /**
         * Register events
         */
        loopThroughPackage("net.swofty.types.generic.event.custom", SkyBlockEvent.class).forEach(SkyBlockEvent::cacheEvent);
        loopThroughPackage("net.swofty.types.generic.event.actions", SkyBlockEvent.class).forEach(SkyBlockEvent::cacheEvent);
        typeLoader.getTraditionalEvents().forEach(SkyBlockEvent::cacheEvent);
        typeLoader.getCustomEvents().forEach(SkyBlockEvent::cacheEvent);

        // Register missions
        loopThroughPackage("net.swofty.types.generic.mission.missions", SkyBlockMission.class)
                .forEach((event) -> {
                    try {
                        event.cacheEvent();
                        MissionData.registerMission(event.getClass());
                    } catch (Exception e) {
                    }
                });
        loopThroughPackage("net.swofty.types.generic.mission.missions", MissionRepeater.class)
                .forEach((event) -> {
                    try {
                        event.getTask(MinecraftServer.getSchedulerManager());
                    } catch (Exception e) {
                    }
                });
        loopThroughPackage("net.swofty.types.generic.item.set.sets", SetRepeatable.class)
                .forEach((event) -> {
                    try {
                        event.getTask(MinecraftServer.getSchedulerManager());
                    } catch (Exception e) {
                    }
                });
        CustomEventCaller.start();
        SkyBlockEvent.register(SkyBlockConst.getEventHandler());

        loopThroughPackage("net.swofty.types.generic.enchantment.impl", SkyBlockValueEvent.class)
                .forEach((event) -> {
                    try {
                        event.cacheEvent();
                    } catch (Exception e) {
                    }
                });
        loopThroughPackage("net.swofty.types.generic.item.set.sets", SkyBlockValueEvent.class)
                .forEach((event) -> {
                    try {
                        event.cacheEvent();
                    } catch (Exception e) {
                    }
                });
        SkyBlockValueEvent.register();

        /**
         * Load item recipes
         */
        loopThroughPackage("net.swofty.types.generic.item.items", Craftable.class)
                .forEach(recipe -> {
                    try {
                        recipe.getRecipe().init();
                    } catch (Exception e) {
                    }
                });

        /**
         * Handle ConnectionManager
         */
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyBlockPlayer::new);
    }

    public static List<SkyBlockPlayer> getLoadedPlayers() {
        List<SkyBlockPlayer> players = new ArrayList<>();
        MinecraftServer.getConnectionManager().getOnlinePlayers()
                .stream()
                .filter(player -> DataHandler.getUser(player) != null)
                .filter(player -> player.getInstance() != null)
                .forEach(player -> players.add((SkyBlockPlayer) player));
        return players;
    }

    public static SkyBlockPlayer getFromUUID(UUID uuid) {
        return getLoadedPlayers().stream().filter(player -> player.getUuid().toString().equalsIgnoreCase(uuid.toString())).findFirst().orElse(null);
    }

    public static SkyBlockPlayer getPlayerFromProfileUUID(UUID uuid) {
        return getLoadedPlayers().stream().filter(player -> player.getProfiles().getCurrentlySelected().equals(uuid)).findFirst().orElse(null);
    }

    public static <T> Stream<T> loopThroughPackage(String packageName, Class<T> clazz) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(clazz);

        return subTypes.stream()
                .map(subClass -> {
                    try {
                        return clazz.cast(subClass.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull);
    }
}
