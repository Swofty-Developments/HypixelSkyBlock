package net.swofty.type.bedwarsgame.events;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgeneric.game.MapsConfig;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

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
		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : game.getMapEntry().getConfiguration().getTeams()) {
			MapsConfig.TwoBlockPosition bedPos = team.getBed();
			if (bedPos == null || bedPos.feet() == null || bedPos.head() == null) continue;

			Point feetPoint = new Pos(bedPos.feet().x(), bedPos.feet().y(), bedPos.feet().z());
			Point headPoint = new Pos(bedPos.head().x(), bedPos.head().y(), bedPos.head().z());

			if (brokenBlockPosition.sameBlock(feetPoint) || brokenBlockPosition.sameBlock(headPoint)) {
				isTeamBedPart = true;
				// This is team X's bed
				if (team.getName().equalsIgnoreCase(playerTeamName)) {
					player.sendMini("<red>You cannot break your own team's bed!</red>");
					event.setCancelled(true);
					return;
				}
				if (!game.getTeamManager().getTeamBedStatus().getOrDefault(team.getName(), false)) {
					// Bed already destroyed logically, block might linger if not cleared perfectly
					event.setCancelled(true);
					return;
				}
				game.recordBedDestroyed(team.getName());
				player.getInstance().setBlock(feetPoint, Block.AIR);
				player.getInstance().setBlock(headPoint, Block.AIR);

				String breakerTeamColor = player.getTag(Tag.String("teamColor"));
				if (breakerTeamColor == null) breakerTeamColor = "gray";
				for (BedWarsPlayer p : game.getPlayers()) {
					p.sendMini(String.format("<b><red>BED DESTRUCTION ></b> Team %s's bed was destroyed by <%s>%s</%s>!",
							team.getName(), breakerTeamColor, player.getUsername(), breakerTeamColor));
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
				player.sendMini("<red>You can only break blocks placed by players!</red>");
				event.setCancelled(true);
			}
		}
	}

}
