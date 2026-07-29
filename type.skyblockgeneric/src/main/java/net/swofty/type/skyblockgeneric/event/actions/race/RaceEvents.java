package net.swofty.type.skyblockgeneric.event.actions.race;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skyblockgeneric.race.RaceInstance;
import net.swofty.type.skyblockgeneric.race.RaceManager;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.UUID;

public class RaceEvents implements HypixelEventClass {

	private final HashMap<UUID, Long> lastClickedTimes = new HashMap<>();

	@PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
	public void onPlayerJoin(PlayerSpawnEvent event) {
		SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
		if (!(HypixelConst.getTypeLoader() instanceof RaceInstance raceInstance)) {
			return;
		}
		RaceManager race = raceInstance.getRace();
		if (race == null) return;

		race.updateForPlayer(HypixelConst.getInstanceContainer(), player);
	}

	@PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
		if (!(HypixelConst.getTypeLoader() instanceof RaceInstance raceInstance)) {
			return;
		}
		RaceManager race = raceInstance.getRace();
		if (race == null) return;
		if (!race.getPerPlayerStartTime().containsKey(player.getUuid())) return;
		race.cancelRace(player);
	}

	@PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
	public void onPlayerMove(PlayerMoveEvent event) {
		SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
		if (!(HypixelConst.getTypeLoader() instanceof RaceInstance raceInstance)) {
			return;
		}
		RaceManager race = raceInstance.getRace();

		if (lastClickedTimes.containsKey(player.getUuid()) && lastClickedTimes.get(player.getUuid()) >= System.currentTimeMillis() - 500) return;
		if (race == null) return;

		Pos playerPos = event.getPlayer().getPosition();

		if (playerPos.samePoint(race.getRace().startPosition(), 0.5)) {
			if (!race.getPerPlayerStartTime().containsKey(player.getUuid()) || race.getPerPlayerStartTime().get(player.getUuid()).lastCheckpointIndex() == -1) {
				player.playSound(Sound.sound(
						Key.key("block.note_block.pling"), Sound.Source.NEUTRAL,
						.3f, 0.75f
				));

				race.startRace(player);
				lastClickedTimes.put(player.getUuid(), System.currentTimeMillis());
				return;
			}
		}

		for (int i = 0; i < race.getRace().getCheckpoints().size(); i++) {
			if (playerPos.samePoint(race.getRace().getCheckpoints().get(i), 0.5)) {
				race.checkpointPlayer(player, i);
				lastClickedTimes.put(player.getUuid(), System.currentTimeMillis());
				break;
			}
		}

		if (playerPos.samePoint(race.getRace().endPosition(), 0.5)) {
			race.finishedRace(player);
			lastClickedTimes.put(player.getUuid(), System.currentTimeMillis());
		}
	}

}
