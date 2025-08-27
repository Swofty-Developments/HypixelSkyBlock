package net.swofty.type.bedwarslobby.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.bedwarslobby.TypeBedWarsLobbyLoader;
import net.swofty.type.generic.HypixelConst;
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
				8,
				TypeBedWarsLobbyLoader.getItemHandler().getItem("lobby_selector").getItemStack()
		);
	}
}

