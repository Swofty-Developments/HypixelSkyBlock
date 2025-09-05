package net.swofty.type.bedwarsgame.events;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.entity.TntEntity;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgeneric.game.MapsConfig;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.utility.MathUtility;

public class ActionGamePlace implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(PlayerBlockPlaceEvent event) {
		BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
		if (!player.hasTag(Tag.String("gameId"))) {
			event.setCancelled(true); // Prevent placing if not in a game
			return;
		}

		if (event.getBlockPosition().y() >= 105) {
			player.sendMini("<red>You cannot place blocks this high!</red>");
			event.setCancelled(true);
			return;
		}

		String gameId = player.getTag(Tag.String("gameId"));
		Game game = TypeBedWarsGameLoader.getGameById(gameId);

		if (game == null || game.getGameStatus() != GameStatus.IN_PROGRESS) {
			event.setCancelled(true);
			return;
		}

		Point blockPosition = event.getBlockPosition();
		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : game.getMapEntry().getConfiguration().getTeams()) {
			MapsConfig.PitchYawPosition spawnPos = team.getSpawn();
			if (spawnPos != null) {
				Point spawnPoint = new Pos(spawnPos.x(), spawnPos.y(), spawnPos.z());
				if (blockPosition.distance(spawnPoint) <= 6) {
					player.sendMini("<red>You cannot build here.</red>");
					event.setCancelled(true);
					return;
				}
			}
		}

		if (event.getBlock().registry().material() == Material.TNT) {
			TntEntity entity = new TntEntity(event.getPlayer());
			entity.setFuse(50);
			entity.setInstance(event.getInstance(), blockPosition.add(0.5, 0, 0.5));
			entity.getViewersAsAudience().playSound(Sound.sound(
					SoundEvent.ENTITY_TNT_PRIMED, Sound.Source.BLOCK,
					1.0f, 1.0f
			), entity);
			MathUtility.delay(
					() -> player.getInstance().setBlock(blockPosition, Block.AIR),
					3
			);
			return;
		}

		event.setBlock(event.getBlock().withTag(TypeBedWarsGameLoader.PLAYER_PLACED_TAG, true));
	}

}
