package net.swofty.type.bedwarsgame.shop.upgrades;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.map.MapsConfig;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.TeamUpgrade;
import net.swofty.type.bedwarsgame.shop.TeamUpgradeTier;

import java.util.List;

public class HealPoolUpgrade extends TeamUpgrade {

	public HealPoolUpgrade() {
		super(
				"heal_pool",
				"Heal Pool",
				"Your team permanently gains a heal pool at your base.",
				ItemStack.of(Material.BEACON),
				List.of(
						new TeamUpgradeTier(1, "Healing I", 1, Currency.DIAMOND)
				)
		);
	}

	@Override
	public void applyEffect(Game game, String teamName, int level) {
		MapsConfig.MapEntry.MapConfiguration.MapTeam team = game.getMapEntry().getConfiguration().getTeams().stream()
				.filter(t -> t.getName().equalsIgnoreCase(teamName))
				.findFirst()
				.orElse(null);

		if (team == null) {
			return;
		}

		MapsConfig.PitchYawPosition spawnPos = team.getSpawn();
		Pos teamSpawn = new Pos(spawnPos.x(), spawnPos.y(), spawnPos.z(), spawnPos.pitch(), spawnPos.yaw());

		MinecraftServer.getSchedulerManager().buildTask(() -> {
			if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
				return;
			}

			for (Player player : game.getPlayers()) {
				String playerTeamName = player.getTag(Tag.String("team"));
				if (teamName.equals(playerTeamName)) {
					if (player.getPosition().distance(teamSpawn) <= 15) {
						// Heal player by 1 heart (2 health)
						double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();
						if (player.getHealth() < maxHealth) {
							player.setHealth(Math.min(player.getHealth() + 2.0f, (float) maxHealth));
						}
					}
				}
			}
		}).repeat(TaskSchedule.seconds(7)).schedule();
	}

}
