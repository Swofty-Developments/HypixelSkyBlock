package net.swofty.type.bedwarslobby.events;

import lombok.SneakyThrows;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.bedwarslobby.TypeBedWarsLobbyLoader;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.hologram.HologramEntity;
import net.swofty.type.generic.entity.npc.impl.NPCEntityImpl;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

public class ActionPlayerJoin implements HypixelEventClass {

	@SneakyThrows
	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(AsyncPlayerConfigurationEvent event) {
		final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

		event.setSpawningInstance(HypixelConst.getInstanceContainer());
		Logger.info("Player " + player.getUsername() + " joined the server from origin server " + player.getOriginServer());
		player.setRespawnPoint(HypixelConst.getTypeLoader()
				.getLoaderValues()
				.spawnPosition()
				.apply(player.getOriginServer())
		);

		// hotbar items
		player.getInventory().setItemStack(
				0,
				TypeBedWarsLobbyLoader.getItemHandler().getItem("play_compass").getItemStack()
		);

		player.getInventory().setItemStack(
				1,
				TypeBedWarsLobbyLoader.getItemHandler().getItem("profile").getItemStack(player)
		);

		player.getInventory().setItemStack(
				2,
				TypeBedWarsLobbyLoader.getItemHandler().getItem("bedwars_menu").getItemStack()
		);

		player.getInventory().setItemStack(
				4,
				TypeBedWarsLobbyLoader.getItemHandler().getItem("collectibles").getItemStack()
		);

		player.getInventory().setItemStack(
				7,
				TypeBedWarsLobbyLoader.getItemHandler().getItem("hide_players").getItemStack(player)
		);

		player.getInventory().setItemStack(
				8,
				TypeBedWarsLobbyLoader.getItemHandler().getItem("lobby_selector").getItemStack()
		);

		player.updateViewerRule((entity) -> {
			if (entity instanceof NPCEntityImpl) return true;
			if (entity instanceof HologramEntity) return true;
			return (entity instanceof Player) != player.getToggles().get(DatapointToggles.Toggles.ToggleType.LOBBY_SHOW_PLAYERS);
		});
	}
}

