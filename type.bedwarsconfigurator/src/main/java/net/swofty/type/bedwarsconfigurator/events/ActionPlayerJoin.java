package net.swofty.type.bedwarsconfigurator.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
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

		event.setSpawningInstance(HypixelConst.getEmptyInstance());
		Logger.info("Player " + player.getUsername() + " joined the server from origin server " + player.getOriginServer());
		player.setRespawnPoint(HypixelConst.getTypeLoader()
				.getLoaderValues()
				.spawnPosition()
				.apply(player.getOriginServer())
		);
	}
}

