package net.swofty.type.bedwarsgame.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.scoreboard.BelowNameTag;
import net.minestom.server.tag.Tag;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.MapTeam;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import org.tinylog.Logger;

import java.util.*;
import java.util.stream.Collectors;

public final class TeamManager {
	private final Game game;
	private final Map<TeamKey, Boolean> teamBedStatus = new EnumMap<>(TeamKey.class);
	private final Map<TeamKey, Map<String, Integer>> teamUpgrades = new EnumMap<>(TeamKey.class);
	private final Map<TeamKey, List<String>> teamTraps = new EnumMap<>(TeamKey.class);

	public TeamManager(Game game) {
		this.game = game;
	}

	public Map<TeamKey, MapTeam> assignPlayersToTeams() {
		Map<TeamKey, MapTeam> configuredTeams = game.getMapEntry().getConfiguration().getTeams();
		Map<TeamKey, MapTeam> activeGameTeams = new EnumMap<>(TeamKey.class);

		teamBedStatus.clear();
		configuredTeams.keySet().forEach(teamKey -> teamBedStatus.put(teamKey, false));

		game.getPlayers().forEach(player -> player.removeTag(Game.ELIMINATED_TAG));

		List<Player> playersToAssign = new ArrayList<>(game.getPlayers());
		Collections.shuffle(playersToAssign);

		List<Map.Entry<TeamKey, MapTeam>> availableTeams = new ArrayList<>(configuredTeams.entrySet());
		Collections.shuffle(availableTeams);

		int teamSize = Math.max(1, game.getBedwarsGameType().getTeamSize());
		int playerIndex = 0;
		BedWarsMapsConfig.Position spectatorPos = game.getMapEntry().getConfiguration().getLocations().getSpectator();

		for (Map.Entry<TeamKey, MapTeam> entry : availableTeams) {
			if (playerIndex >= playersToAssign.size()) break;

			TeamKey teamKey = entry.getKey();
			MapTeam team = entry.getValue();
			boolean teamHasPlayers = false;

			for (int memberCount = 0; memberCount < teamSize && playerIndex < playersToAssign.size(); memberCount++) {
				Player player = playersToAssign.get(playerIndex);
				assignPlayerToTeam(player, teamKey, team, spectatorPos);
				playerIndex++;
				teamHasPlayers = true;
			}

			if (teamHasPlayers) {
				activeGameTeams.put(teamKey, team);
			}
		}

		handleUnassignedPlayers(playersToAssign, playerIndex, spectatorPos);
		return activeGameTeams;
	}

	private void assignPlayerToTeam(Player player, TeamKey teamKey, MapTeam team,
	                                BedWarsMapsConfig.Position spectatorPos) {
		player.setTag(Tag.String("team"), teamKey.name());

		BedWarsMapsConfig.PitchYawPosition spawnPos = team.getSpawn();
		player.teleport(new Pos(spawnPos.x(), spawnPos.y(), spawnPos.z(), spawnPos.yaw(), spawnPos.pitch()));
		player.setRespawnPoint(new Pos(spectatorPos.x(), spectatorPos.y(), spectatorPos.z()));

		player.setGameMode(GameMode.SURVIVAL);
		player.getInventory().clear();
		player.getInventory().addItemStack(ItemStack.of(Material.WOODEN_SWORD));
		player.setEnableRespawnScreen(false);

		player.setDisplayName(Component.text(teamKey.chatColor() + "§l" + teamKey.getName() + " §r" + teamKey.chatColor() + player.getUsername()));
		player.setBelowNameTag(new BelowNameTag("health", Component.text("20❤", NamedTextColor.RED)));

		equipTeamArmor(player, teamKey);
		Logger.info("Assigned player {} to team {}", player.getUsername(), teamKey.getName());
	}

	public void equipTeamArmor(Player player, TeamKey teamKey) {
		Integer armorLevel = player.getTag(TypeBedWarsGameLoader.ARMOR_LEVEL_TAG);
		if (armorLevel == null) armorLevel = 0;

		Material boots = null;
		Material leggings = switch (armorLevel) {
			case 1 -> {
				boots = Material.CHAINMAIL_BOOTS;
				yield Material.CHAINMAIL_LEGGINGS;
			}
			case 2 -> {
				boots = Material.IRON_BOOTS;
				yield Material.IRON_LEGGINGS;
			}
			case 3 -> {
				boots = Material.DIAMOND_BOOTS;
				yield Material.DIAMOND_LEGGINGS;
			}
			default -> null;
		};

		net.minestom.server.color.Color minestomColor = new net.minestom.server.color.Color(teamKey.rgb());

		if (boots != null && leggings != null) {
			player.setEquipment(EquipmentSlot.BOOTS, ItemStack.of(boots));
			player.setEquipment(EquipmentSlot.LEGGINGS, ItemStack.of(leggings));
		} else {
			player.setEquipment(EquipmentSlot.BOOTS, ItemStack.of(Material.LEATHER_BOOTS).with(DataComponents.DYED_COLOR, minestomColor));
			player.setEquipment(EquipmentSlot.LEGGINGS, ItemStack.of(Material.LEATHER_LEGGINGS).with(DataComponents.DYED_COLOR, minestomColor));
		}

		player.setEquipment(EquipmentSlot.CHESTPLATE,
				ItemStack.of(Material.LEATHER_CHESTPLATE).with(DataComponents.DYED_COLOR, minestomColor));
		player.setEquipment(EquipmentSlot.HELMET,
				ItemStack.of(Material.LEATHER_HELMET).with(DataComponents.DYED_COLOR, minestomColor));
	}

	private void handleUnassignedPlayers(List<Player> playersToAssign, int startIndex,
	                                     BedWarsMapsConfig.Position spectatorPos) {
		if (startIndex >= playersToAssign.size()) return;

		int unassignedCount = playersToAssign.size() - startIndex;
		Logger.warn("{} players were not assigned to teams as all slots were filled.", unassignedCount);

		for (int i = startIndex; i < playersToAssign.size(); i++) {
			Player unassignedPlayer = playersToAssign.get(i);
			unassignedPlayer.sendMessage(Component.text(
					"All team slots were full. You have been moved to spectator.", NamedTextColor.YELLOW));
			unassignedPlayer.setGameMode(GameMode.SPECTATOR);
			unassignedPlayer.teleport(new Pos(spectatorPos.x(), spectatorPos.y(), spectatorPos.z()));
		}
	}

	public int getTeamUpgradeLevel(TeamKey teamKey, String upgradeKey) {
		return teamUpgrades.computeIfAbsent(teamKey, k -> new HashMap<>()).getOrDefault(upgradeKey, 0);
	}

	public void setTeamUpgradeLevel(TeamKey teamKey, String upgradeKey, int level) {
		teamUpgrades.computeIfAbsent(teamKey, k -> new HashMap<>()).put(upgradeKey, level);
	}

	public List<String> getTeamTraps(TeamKey teamKey) {
		return teamTraps.computeIfAbsent(teamKey, k -> new ArrayList<>());
	}

	public void addTeamTrap(TeamKey teamKey, String trapKey) {
		List<String> traps = teamTraps.computeIfAbsent(teamKey, k -> new ArrayList<>());
		if (!traps.contains(trapKey)) {
			traps.add(trapKey);
		}
	}

	public void removeTeamTrap(TeamKey teamKey, String trapKey) {
		List<String> traps = teamTraps.get(teamKey);
		if (traps != null) {
			traps.remove(trapKey);
			if (traps.isEmpty()) {
				teamTraps.remove(teamKey);
			}
		}
	}

	public List<BedWarsPlayer> getPlayersOnTeam(TeamKey teamKey) {
		return game.getPlayers().stream()
				.filter(player -> teamKey.equals(player.getTeamKey())
						&& player.isOnline()
						&& !Boolean.TRUE.equals(player.getTag(Game.ELIMINATED_TAG)))
				.collect(Collectors.toList());
	}

	public int countActivePlayersOnTeam(TeamKey teamKey) {
		return (int) game.getPlayers().stream()
				.filter(player -> teamKey.equals(player.getTeamKey())
						&& player.isOnline()
						&& !Boolean.TRUE.equals(player.getTag(Game.ELIMINATED_TAG)))
				.count();
	}

	public TeamKey getTeamKeyByName(String teamName) {
		return Arrays.stream(TeamKey.values())
				.filter(k -> k.getName().equalsIgnoreCase(teamName))
				.findFirst()
				.orElse(null);
	}

	public void recordBedDestroyed(TeamKey teamKey) {
		teamBedStatus.put(teamKey, false);
		Logger.info("Bed destroyed for team: {}", teamKey.getName());
	}

	public boolean isBedAlive(TeamKey teamKey) {
		return teamBedStatus.getOrDefault(teamKey, false);
	}

	public void setBedStatus(TeamKey teamKey, boolean alive) {
		teamBedStatus.put(teamKey, alive);
	}

	public Map<TeamKey, Boolean> getTeamBedStatus() {
		return new EnumMap<>(teamBedStatus);
	}
}
