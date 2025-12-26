package net.swofty.type.bedwarsgame.events;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.MapTeam;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.stats.BedWarsStatsRecorder;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

import java.util.Map;
import java.util.Objects;

public class ActionGameBreak implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
	public void run(PlayerBlockBreakEvent event) {
		BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
		Block blockBeingBroken = event.getBlock();

		if (!player.hasTag(Tag.String("gameId"))) {
			event.setCancelled(true);
			return;
		}
		String gameId = player.getTag(Tag.String("gameId"));
		Game game = TypeBedWarsGameLoader.getGameById(gameId);
		if (game == null || game.getGameStatus() != GameStatus.IN_PROGRESS) {
			event.setCancelled(true);
			return;
		}

		boolean isTeamBedPart = false;
		String playerTeamName = player.getTag(Tag.String("team"));
		Point brokenBlockPosition = event.getBlockPosition();

		// Check if it's a part of any team's bed first
		for (Map.Entry<TeamKey, MapTeam> entry : game.getMapEntry().getConfiguration().getTeams().entrySet()) {
			TeamKey teamKey = entry.getKey();
			MapTeam team = entry.getValue();
			BedWarsMapsConfig.TwoBlockPosition bedPos = team.getBed();
			if (bedPos == null || bedPos.feet() == null || bedPos.head() == null) continue;

			Point feetPoint = new Pos(bedPos.feet().x(), bedPos.feet().y(), bedPos.feet().z());
			Point headPoint = new Pos(bedPos.head().x(), bedPos.head().y(), bedPos.head().z());

			if (brokenBlockPosition.sameBlock(feetPoint) || brokenBlockPosition.sameBlock(headPoint)) {
				// This is team X's bed
				if (teamKey.getName().equalsIgnoreCase(playerTeamName)) {
					player.getAchievementHandler().completeAchievement("bedwars.you_cant_do_that");
					player.sendMessage("§cYou cannot break your own team's bed!");
					event.setCancelled(true);
					return;
				}
				if (!game.getTeamManager().isBedAlive(teamKey)) {
					// Bed already destroyed logically, block might linger if not cleared perfectly
					event.setCancelled(true);
					return;
				}
				game.recordBedDestroyed(teamKey);
				BedWarsStatsRecorder.recordBedBroken(player, game.getBedwarsGameType());
				player.getInstance().setBlock(feetPoint, Block.AIR);
				player.getInstance().setBlock(headPoint, Block.AIR);

				for (BedWarsPlayer p : game.getPlayers()) {
					p.sendMessage(String.format("§c§lBED DESTRUCTION §r§cTeam %s's bed was destroyed by %s%s!",
							teamKey.getName(), teamKey.chatColor(), player.getUsername()));
				}
				event.setCancelled(true); // handled the bed destruction and block removal
				return;
			}
		}

		// If it's not a team bed part, then check if it's any other player-placed block
		if (!isTeamBedPart) {
			if (Boolean.TRUE.equals(blockBeingBroken.getTag(TypeBedWarsGameLoader.PLAYER_PLACED_TAG))) {
				new ItemEntity(ItemStack.of(Objects.requireNonNull(blockBeingBroken.registry().material()))).setInstance(player.getInstance(), event.getBlockPosition());
				event.setCancelled(false);
			} else {
				// Not a team bed and not a player-placed block
				player.sendMessage("§cYou can only break blocks placed by players!");
				event.setCancelled(true);
			}
		}
	}
}
