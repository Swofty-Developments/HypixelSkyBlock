package net.swofty.type.bedwarsgame.events;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.tag.Tag;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.MapTeam;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

import java.util.List;
import java.util.Map;

public class ActionGameMove implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
	public void run(PlayerMoveEvent event) {
		BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();

		if (!player.isOnline()) {
			return;
		}

		Game game = player.getGame();
		if (game == null || game.getGameStatus() != GameStatus.IN_PROGRESS) {
			return;
		}

		if (player.getPosition().y() <= 0) {
			if (player.getLastDamageSource() != null && player.getLastDamageSource().getSource() instanceof Player p) {
				player.damage(Damage.fromPlayer(p, 1.0f));
			} else {
				player.damage(Damage.fromPosition(DamageType.OUT_OF_WORLD, player.getPosition(), 1.0f));
			}
			player.kill();
			return;
		}

		TeamKey playerTeamKey = player.getTeamKey();
		if (playerTeamKey == null) {
			return;
		}

		Point playerPos = player.getPosition();

		// check what team this is
		for (Map.Entry<TeamKey, MapTeam> entry : game.getMapEntry().getConfiguration().getTeams().entrySet()) {
			TeamKey teamKey = entry.getKey();
			MapTeam team = entry.getValue();

			// Don't trigger own team's traps
			if (teamKey.equals(playerTeamKey)) {
				continue;
			}

			BedWarsMapsConfig.TwoBlockPosition bedPos = team.getBed();
			if (bedPos == null || bedPos.feet() == null) {
				continue;
			}

			Point bedLocation = new Pos(bedPos.feet().x(), bedPos.feet().y(), bedPos.feet().z());
			if (playerPos.distance(bedLocation) <= 10) {
				List<String> teamTraps = game.getTeamManager().getTeamTraps(teamKey);
				if (teamTraps.isEmpty()) {
					continue;
				}

				var trap = TypeBedWarsGameLoader.getTrapManager().getTrap(teamTraps.getFirst());
				if (trap != null) {
					trap.trigger(game, teamKey, player);
				}
			}
		}
	}
}
