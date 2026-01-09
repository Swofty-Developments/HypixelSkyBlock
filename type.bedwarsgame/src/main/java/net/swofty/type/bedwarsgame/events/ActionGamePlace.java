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
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.MapTeam;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.utility.MathUtility;
import org.tinylog.Logger;

public class ActionGamePlace implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(PlayerBlockPlaceEvent event) {
		BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
		Game game = player.getGame();
		if (game == null) {
			Logger.info("Player {} tried to place a block but is not in a game!", player.getUsername());
			event.setCancelled(true); // Prevent placing if not in a game
			return;
		}

		if (event.getBlockPosition().y() >= 105) {
			player.sendMessage("§cYou cannot place blocks this high!");
			event.setCancelled(true);
			return;
		}

		if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
			event.setCancelled(true);
			return;
		}

		Point blockPosition = event.getBlockPosition();
		for (MapTeam team : game.getMapEntry().getConfiguration().getTeams().values()) {
			BedWarsMapsConfig.PitchYawPosition spawnPos = team.getSpawn();
			if (spawnPos != null) {
				Point spawnPoint = new Pos(spawnPos.x(), spawnPos.y(), spawnPos.z());
				if (blockPosition.distance(spawnPoint) <= 6) {
					player.sendMessage("§cYou cannot build here.");
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
		player.getAchievementHandler().addProgressByTrigger("bedwars.blocks_placed", 1);
	}

}
