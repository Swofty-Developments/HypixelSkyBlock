package net.swofty.type.bedwarsgame.events;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.player.PlayerDeathEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.map.MapsConfig;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

import java.util.List;

public class ActionGameMove implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
	public void run(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.getPosition().y() <= 0) {
			if (player.getLastDamageSource() != null && player.getLastDamageSource().getSource() instanceof Player p) {
				player.damage(Damage.fromPlayer(p, 1.0f));
			} else {
				player.damage(Damage.fromPosition(DamageType.OUT_OF_WORLD, player.getPosition(), 1.0f));
			}
			player.kill();
			return;
		}
		MinecraftServer.getSchedulerManager().buildTask(() -> {
			if (!player.isOnline() || !player.hasTag(Tag.String("gameId"))) {
				return;
			}

			String gameId = player.getTag(Tag.String("gameId"));
			Game game = TypeBedWarsGameLoader.getGameById(gameId);

			if (game == null || game.getGameStatus() != GameStatus.IN_PROGRESS) {
				return;
			}

			String playerTeamName = player.getTag(Tag.String("team"));
			if (playerTeamName == null) {
				return;
			}

			Point playerPos = player.getPosition();

			// check what team this is
			for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : game.getMapEntry().getConfiguration().getTeams()) {
				// Don't trigger own team's traps
				if (team.getName().equals(playerTeamName)) {
					continue;
				}

				MapsConfig.TwoBlockPosition bedPos = team.getBed();
				if (bedPos == null || bedPos.feet() == null) {
					continue;
				}

				Point bedLocation = new Pos(bedPos.feet().x(), bedPos.feet().y(), bedPos.feet().z());
				if (playerPos.distance(bedLocation) <= 10) {
					List<String> teamTraps = game.getTeamTraps(team.getName());
					if (teamTraps.isEmpty()) {
						continue;
					}

					var trap = TypeBedWarsGameLoader.getTrapService().getTrap(teamTraps.getFirst());
					if (trap != null) {
						trap.trigger(game, team.getName(), player);
					}
				}
			}
		});
	}

}
