package net.swofty.pvp.feature.config;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.event.player.PlayerRespawnEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;

import java.util.ArrayList;
import java.util.List;

public class CombatFeatureRegistry {
	private static final EventNode<Event> initNode = EventNode.all("combat-feature-init");
	private static final List<DefinedFeature<?>> features = new ArrayList<>();

	public static void init(DefinedFeature<?> feature) {
		if (!features.contains(feature)) {
			features.add(feature);
			if (feature.playerInit() != null) {
				initNode.addListener(AddEntityToInstanceEvent.class, event -> {
					var entity = event.getEntity();
					if (entity instanceof Player player)
						feature.playerInit().init(player, true);
				});
				initNode.addListener(PlayerSpawnEvent.class, event -> feature.playerInit().init(event.getPlayer(), false));
				initNode.addListener(PlayerRespawnEvent.class, event -> feature.playerInit().init(event.getPlayer(), false));
			}
		}
	}

	public static void init() {
		MinecraftServer.getGlobalEventHandler().addChild(initNode);
	}
}
