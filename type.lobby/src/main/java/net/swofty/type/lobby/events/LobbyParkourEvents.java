package net.swofty.type.lobby.events;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.MathUtility;
import net.swofty.type.lobby.LobbyTypeLoader;
import net.swofty.type.lobby.parkour.LobbyParkourManager;

import java.util.HashMap;
import java.util.UUID;

public class LobbyParkourEvents implements HypixelEventClass {

	private final HashMap<UUID, Long> lastClickedTimes = new HashMap<>();

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void onPlayerJoin(PlayerSpawnEvent event) {
		HypixelPlayer player = (HypixelPlayer) event.getPlayer();
		LobbyTypeLoader loader = (LobbyTypeLoader) HypixelConst.getTypeLoader();

		LobbyParkourManager parkourManager = loader.getParkourManager();
		if (parkourManager == null) return;

		parkourManager.updateForPlayer(HypixelConst.getInstanceContainer(), player);
	}

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		HypixelPlayer player = (HypixelPlayer) event.getPlayer();
		lastClickedTimes.remove(player.getUuid());

		LobbyTypeLoader loader = (LobbyTypeLoader) HypixelConst.getTypeLoader();
		LobbyParkourManager parkourManager = loader.getParkourManager();
		if (parkourManager == null) return;
		if (!parkourManager.getPerPlayerStartTime().containsKey(player.getUuid())) return;
		parkourManager.cancelParkour(player);
	}

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
	public void run(PlayerMoveEvent event) {
		LobbyTypeLoader loader = (LobbyTypeLoader) HypixelConst.getTypeLoader();
		final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

		if (lastClickedTimes.containsKey(player.getUuid()) && lastClickedTimes.get(player.getUuid()) >= System.currentTimeMillis() - 5000) return;

		LobbyParkourManager parkourManager = loader.getParkourManager();
		if (parkourManager == null) return;

		Pos playerPos = event.getPlayer().getPosition();

		if (playerPos.samePoint(parkourManager.getParkour().getCheckpoints().getFirst(), 0.5)) {
			player.playSound(Sound.sound(
					Key.key("block.metal_pressure_plate.click_on"), Sound.Source.NEUTRAL,
					.3f, 0.75f
			));

			MathUtility.delay(() -> player.playSound(Sound.sound(
					Key.key("block.metal_pressure_plate.click_off"), Sound.Source.NEUTRAL,
					.3f, 0.65f
			)), 30);

			parkourManager.startParkour(player);
			lastClickedTimes.put(player.getUuid(), System.currentTimeMillis());
		}

		for (int i = 1; i < parkourManager.getParkour().getCheckpoints().size() - 1; i++) {
			if (playerPos.samePoint(parkourManager.getParkour().getCheckpoints().get(i), 0.5)) {
				player.playSound(Sound.sound(
						Key.key("block.metal_pressure_plate.click_on"), Sound.Source.NEUTRAL,
						.3f, 0.75f
				));

				MathUtility.delay(() -> player.playSound(Sound.sound(
						Key.key("block.metal_pressure_plate.click_off"), Sound.Source.NEUTRAL,
						.3f, 0.65f
				)), 30);

				parkourManager.checkpointPlayer(player, i);
				lastClickedTimes.put(player.getUuid(), System.currentTimeMillis());
				break;
			}
		}

		if (playerPos.samePoint(parkourManager.getParkour().getCheckpoints().getLast(), 0.5)) {
			player.playSound(Sound.sound(
					Key.key("block.metal_pressure_plate.click_on"), Sound.Source.NEUTRAL,
					.3f, 0.75f
			));

			MathUtility.delay(() -> player.playSound(Sound.sound(
					Key.key("block.metal_pressure_plate.click_off"), Sound.Source.NEUTRAL,
					.3f, 0.65f
			)), 30);

			parkourManager.finishedParkour(player);
			lastClickedTimes.put(player.getUuid(), System.currentTimeMillis());
		}
	}

}
