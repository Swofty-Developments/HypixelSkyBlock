package net.swofty.type.bedwarslobby;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.bedwarsgeneric.item.BedWarsItem;
import net.swofty.type.bedwarsgeneric.item.BedWarsItemHandler;
import net.swofty.type.bedwarslobby.util.LaunchPads;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.HypixelTypeLoader;
import net.swofty.type.generic.entity.animalnpc.HypixelAnimalNPC;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.villager.HypixelVillagerNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.EmptyTabModule;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TypeBedWarsLobbyLoader implements HypixelTypeLoader {

	@Getter
	private static final BedWarsItemHandler itemHandler = new BedWarsItemHandler();

	@Override
	public ServerType getType() {
		return ServerType.BEDWARS_LOBBY;
	}

	@Override
	public void onInitialize(MinecraftServer server) {

	}

	@Override
	public void afterInitialize(MinecraftServer server) {
		BedWarsLobbyScoreboard.start();
		LaunchPads.register(MinecraftServer.getSchedulerManager());
		HypixelGenericLoader.loopThroughPackage("net.swofty.type.bedwarslobby.item.impl", BedWarsItem.class).forEach(itemHandler::add);
	}

	@Override
	public List<ServiceType> getRequiredServices() {
		return List.of();
	}

	@Override
	public TablistManager getTablistManager() {
		return new TablistManager() {
			@Override
			public List<TablistModule> getModules() {
				return List.of(
						new EmptyTabModule(),
						new EmptyTabModule(),
						new EmptyTabModule(),
						new EmptyTabModule()
				);
			}
		};
	}

	@Override
	public LoaderValues getLoaderValues() {
		return new LoaderValues(
				(type) -> new Pos(-39.5, 72, 0, -90, 0), // Spawn position
				false // Announce death messages
		);
	}

	@Override
	public List<HypixelEventClass> getTraditionalEvents() {
		return HypixelGenericLoader.loopThroughPackage(
				"net.swofty.type.bedwarslobby.events",
				HypixelEventClass.class
		).toList();
	}

	@Override
	public List<HypixelEventClass> getCustomEvents() {
		return HypixelGenericLoader.loopThroughPackage(
				"net.swofty.type.bedwarslobby.events.custom",
				HypixelEventClass.class
		).toList();
	}

	@Override
	public List<HypixelVillagerNPC> getVillagerNPCs() {
		return HypixelGenericLoader.loopThroughPackage(
				"net.swofty.type.bedwarslobby.villagers",
				HypixelVillagerNPC.class
		).toList();
	}

	@Override
	public List<HypixelAnimalNPC> getAnimalNPCs() {
		return HypixelGenericLoader.loopThroughPackage(
				"net.swofty.type.bedwarslobby.animalnpcs",
				HypixelAnimalNPC.class
		).toList();
	}

	@Override
	public List<HypixelNPC> getNPCs() {
		return HypixelGenericLoader.loopThroughPackage(
				"net.swofty.type.bedwarslobby.npcs",
				HypixelNPC.class
		).toList();
	}

	@Override
	public List<ServiceToClient> getServiceRedisListeners() {
		return HypixelGenericLoader.loopThroughPackage(
				"net.swofty.type.bedwarslobby.redis.service",
				ServiceToClient.class
		).toList();
	}

	@Override
	public List<ProxyToClient> getProxyRedisListeners() {
		return List.of();
	}

	@Override
	public @Nullable CustomWorlds getMainInstance() {
		return CustomWorlds.BEDWARS_LOBBY;
	}

}
