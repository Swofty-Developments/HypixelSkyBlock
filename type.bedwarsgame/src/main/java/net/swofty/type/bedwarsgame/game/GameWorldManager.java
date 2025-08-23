package net.swofty.type.bedwarsgame.game;

import net.kyori.adventure.key.Key;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.entity.BedWarsShopNPC;
import net.swofty.type.bedwarsgame.map.MapsConfig;
import org.intellij.lang.annotations.Subst;
import org.tinylog.Logger;

import java.util.List;

public final class GameWorldManager {
	private final Game game;
	private final InstanceContainer instanceContainer;

	public GameWorldManager(Game game) {
		this.game = game;
		this.instanceContainer = game.getInstanceContainer();
	}

	public void clearExistingBeds() {
		List<MapsConfig.MapEntry.MapConfiguration.MapTeam> teams =
				game.getMapEntry().getConfiguration().getTeams();

		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : teams) {
			MapsConfig.TwoBlockPosition bedPositions = team.getBed();
			if (bedPositions != null) {
				clearBedBlocks(bedPositions);
			}
		}
	}

	private void clearBedBlocks(MapsConfig.TwoBlockPosition bedPositions) {
		if (bedPositions.feet() != null) {
			instanceContainer.setBlock(
					(int) bedPositions.feet().x(),
					(int) bedPositions.feet().y(),
					(int) bedPositions.feet().z(),
					Block.AIR
			);
		}

		if (bedPositions.head() != null) {
			instanceContainer.setBlock(
					(int) bedPositions.head().x(),
					(int) bedPositions.head().y(),
					(int) bedPositions.head().z(),
					Block.AIR
			);
		}
	}

	public void placeBeds(List<MapsConfig.MapEntry.MapConfiguration.MapTeam> activeTeams) {
		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : activeTeams) {
			placeBedForTeam(team);
		}
	}

	private void placeBedForTeam(MapsConfig.MapEntry.MapConfiguration.MapTeam team) {
		MapsConfig.TwoBlockPosition bedPositions = team.getBed();
		if (bedPositions == null || bedPositions.feet() == null || bedPositions.head() == null) {
			Logger.warn("Bed position not fully defined for team: {}. Skipping bed placement.", team.getName());
			return;
		}

		MapsConfig.Position feetPos = bedPositions.feet();
		MapsConfig.Position headPos = bedPositions.head();

		try {
			Material bedMaterial = getBedMaterialForTeam(team);
			String facing = calculateBedFacing(feetPos, headPos);

			Block footBlock = bedMaterial.block()
					.withProperty("part", "foot")
					.withProperty("facing", facing);
			Block headBlock = bedMaterial.block()
					.withProperty("part", "head")
					.withProperty("facing", facing);

			instanceContainer.setBlock((int) feetPos.x(), (int) feetPos.y(), (int) feetPos.z(), footBlock);
			instanceContainer.setBlock((int) headPos.x(), (int) headPos.y(), (int) headPos.z(), headBlock);

			game.getTeamManager().setBedStatus(team.getName(), true);

			Logger.debug("Placed {} bed for team {} (foot: {}, head: {}, facing: {})",
					team.getColor(), team.getName(), feetPos, headPos, facing);

		} catch (IllegalArgumentException e) {
			Logger.error("Error placing bed for team {}: {}", team.getName(), e.getMessage());
			// Place stone blocks as fallback
			instanceContainer.setBlock((int) feetPos.x(), (int) feetPos.y(), (int) feetPos.z(), Block.STONE);
			instanceContainer.setBlock((int) headPos.x(), (int) headPos.y(), (int) headPos.z(), Block.STONE);
		}
	}

	private Material getBedMaterialForTeam(MapsConfig.MapEntry.MapConfiguration.MapTeam team) {
		@Subst("red_bed") String bedBlockName = team.getColor().toLowerCase() + "_bed";
		Material bedMaterial = Material.fromKey(Key.key(Key.MINECRAFT_NAMESPACE, bedBlockName));

		if (bedMaterial == null) {
			Logger.warn("Could not find bed material for color: {}. Defaulting to RED_BED.", team.getColor());
			return Material.RED_BED;
		}

		return bedMaterial;
	}

	private String calculateBedFacing(MapsConfig.Position feetPos, MapsConfig.Position headPos) {
		if (headPos.x() > feetPos.x()) return "east";
		if (headPos.x() < feetPos.x()) return "west";
		if (headPos.z() > feetPos.z()) return "south";
		return "north";
	}

	public void spawnShopNPCs(List<MapsConfig.MapEntry.MapConfiguration.MapTeam> activeTeams) {
		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : activeTeams) {
			spawnShopNPCsForTeam(team);
		}
	}

	private void spawnShopNPCsForTeam(MapsConfig.MapEntry.MapConfiguration.MapTeam team) {
		if (team.getShop() == null) {
			return;
		}

		MapsConfig.PitchYawPosition itemShopPos = team.getShop().item();
		MapsConfig.PitchYawPosition teamShopPos = team.getShop().team();

		if (itemShopPos != null) {
			BedWarsShopNPC itemShopNpc = new BedWarsShopNPC("Item Shop", BedWarsShopNPC.NPCType.SHOP);
			itemShopNpc.setInstance(instanceContainer,
					new Pos(itemShopPos.x(), itemShopPos.y(), itemShopPos.z(),
							itemShopPos.yaw(), itemShopPos.pitch())).join();
		}

		if (teamShopPos != null) {
			BedWarsShopNPC teamShopNpc = new BedWarsShopNPC("Team Upgrades", BedWarsShopNPC.NPCType.TEAM);
			teamShopNpc.setInstance(instanceContainer,
					new Pos(teamShopPos.x(), teamShopPos.y(), teamShopPos.z(),
							teamShopPos.yaw(), teamShopPos.pitch())).join();
		}
	}

	public void respawnAllBeds() {
		List<MapsConfig.MapEntry.MapConfiguration.MapTeam> teams =
				game.getMapEntry().getConfiguration().getTeams();

		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : teams) {
			if (game.getTeamManager().isBedAlive(team.getName())) {
				placeBedForTeam(team);
			}
		}
	}
}
