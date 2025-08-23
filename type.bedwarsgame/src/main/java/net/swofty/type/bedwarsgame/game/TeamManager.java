package net.swofty.type.bedwarsgame.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.scoreboard.BelowNameTag;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.map.MapsConfig;
import net.swofty.type.bedwarsgame.util.ColorUtil;
import org.tinylog.Logger;

import java.util.*;
import java.util.stream.Collectors;

public final class TeamManager {
	private final Game game;
	private final Map<String, Boolean> teamBedStatus = new HashMap<>();
	private final Map<String, Map<String, Integer>> teamUpgrades = new HashMap<>();
	private final Map<String, List<String>> teamTraps = new HashMap<>();

	public TeamManager(Game game) {
		this.game = game;
	}

	public List<MapsConfig.MapEntry.MapConfiguration.MapTeam> assignPlayersToTeams() {
		List<MapsConfig.MapEntry.MapConfiguration.MapTeam> configuredTeams =
				game.getMapEntry().getConfiguration().getTeams();
		List<MapsConfig.MapEntry.MapConfiguration.MapTeam> activeGameTeams = new ArrayList<>();

		teamBedStatus.clear();
		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : configuredTeams) {
			teamBedStatus.put(team.getName(), false);
		}

		// Remove elimination tags
		game.getPlayers().forEach(player -> player.removeTag(Game.ELIMINATED_TAG));
		List<Player> playersToAssign = new ArrayList<>(game.getPlayers());
		Collections.shuffle(playersToAssign);

		List<MapsConfig.MapEntry.MapConfiguration.MapTeam> availableTeams = new ArrayList<>(configuredTeams);
		Collections.shuffle(availableTeams);

		int teamSize = game.getGameType().getTeamSize();
		if (teamSize <= 0) teamSize = 1;

		int playerIndex = 0;
		MapsConfig.Position spectatorPos = game.getMapEntry().getConfiguration().getLocations().getSpectator();

		// Assign players to teams
		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : availableTeams) {
			if (playerIndex >= playersToAssign.size()) break;

			boolean teamHasPlayers = false;
			for (int memberCount = 0; memberCount < teamSize && playerIndex < playersToAssign.size(); memberCount++) {
				Player player = playersToAssign.get(playerIndex);
				assignPlayerToTeam(player, team, spectatorPos);
				playerIndex++;
				teamHasPlayers = true;
			}

			if (teamHasPlayers) {
				activeGameTeams.add(team);
			}
		}

		handleUnassignedPlayers(playersToAssign, playerIndex, spectatorPos);
		return activeGameTeams;
	}

	private void assignPlayerToTeam(Player player, MapsConfig.MapEntry.MapConfiguration.MapTeam team,
									MapsConfig.Position spectatorPos) {
		player.setTag(Tag.String("team"), team.getName());
		player.setTag(Tag.String("teamColor"), team.getColor());
		player.setTag(Tag.String("javaColor"), team.getJavacolor());

		// teleport to team spawn
		MapsConfig.PitchYawPosition spawnPos = team.getSpawn();
		player.teleport(new Pos(spawnPos.x(), spawnPos.y(), spawnPos.z(), spawnPos.yaw(), spawnPos.pitch()));
		player.setRespawnPoint(new Pos(spectatorPos.x(), spectatorPos.y(), spectatorPos.z()));

		player.setGameMode(GameMode.SURVIVAL);
		player.getInventory().clear();
		player.getInventory().addItemStack(ItemStack.of(Material.WOODEN_SWORD));
		player.setEnableRespawnScreen(false);

		// display name and health indicator
		player.setDisplayName(MiniMessage.miniMessage().deserialize(
				"<" + team.getColor().toLowerCase() + "><b>" +
						team.getName().toUpperCase().charAt(0) + "</b> " + player.getUsername()));
		player.setBelowNameTag(new BelowNameTag("health",
				MiniMessage.miniMessage().deserialize("<red>20❤</red>"))); // this should be updated on damage

		// armor
		equipTeamArmor(player, team);
		Logger.debug("Assigned player {} to team {}", player.getUsername(), team.getName());
	}

	private void equipTeamArmor(Player player, MapsConfig.MapEntry.MapConfiguration.MapTeam team) {
		java.awt.Color awtColor = ColorUtil.getColorByName(team.getJavacolor());
		if (awtColor != null) {
			net.minestom.server.color.Color minestomColor =
					new net.minestom.server.color.Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());

			player.setEquipment(EquipmentSlot.BOOTS,
					ItemStack.of(Material.LEATHER_BOOTS).with(DataComponents.DYED_COLOR, minestomColor));
			player.setEquipment(EquipmentSlot.LEGGINGS,
					ItemStack.of(Material.LEATHER_LEGGINGS).with(DataComponents.DYED_COLOR, minestomColor));
			player.setEquipment(EquipmentSlot.CHESTPLATE,
					ItemStack.of(Material.LEATHER_CHESTPLATE).with(DataComponents.DYED_COLOR, minestomColor));
			player.setEquipment(EquipmentSlot.HELMET,
					ItemStack.of(Material.LEATHER_HELMET).with(DataComponents.DYED_COLOR, minestomColor));
		} else {
			Logger.warn("Invalid color '{}' for team '{}'. Player '{}' will not have colored armor.",
					team.getColor(), team.getName(), player.getUsername());
		}
	}

	private void handleUnassignedPlayers(List<Player> playersToAssign, int startIndex,
										 MapsConfig.Position spectatorPos) {
		if (startIndex < playersToAssign.size()) {
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
	}

	public int getTeamUpgradeLevel(String teamName, String upgradeKey) {
		return teamUpgrades.computeIfAbsent(teamName, k -> new HashMap<>()).getOrDefault(upgradeKey, 0);
	}

	public void setTeamUpgradeLevel(String teamName, String upgradeKey, int level) {
		teamUpgrades.computeIfAbsent(teamName, k -> new HashMap<>()).put(upgradeKey, level);
	}

	public List<String> getTeamTraps(String teamName) {
		return teamTraps.computeIfAbsent(teamName, k -> new ArrayList<>());
	}

	public void addTeamTrap(String teamName, String trapKey) {
		List<String> traps = teamTraps.computeIfAbsent(teamName, k -> new ArrayList<>());
		if (!traps.contains(trapKey)) {
			traps.add(trapKey);
		}
	}

	public void removeTeamTrap(String teamName, String trapKey) {
		List<String> traps = teamTraps.get(teamName);
		if (traps != null) {
			traps.remove(trapKey);
			if (traps.isEmpty()) {
				teamTraps.remove(teamName);
			}
		}
	}

	public List<Player> getPlayersOnTeam(String teamName) {
		return game.getPlayers().stream()
				.filter(player -> teamName.equals(player.getTag(Tag.String("team")))
						&& player.isOnline()
						&& !Boolean.TRUE.equals(player.getTag(Game.ELIMINATED_TAG)))
				.collect(Collectors.toList());
	}

	public int countActivePlayersOnTeam(MapsConfig.MapEntry.MapConfiguration.MapTeam team) {
		return (int) game.getPlayers().stream()
				.filter(player -> team.getName().equals(player.getTag(Tag.String("team")))
						&& player.isOnline()
						&& !Boolean.TRUE.equals(player.getTag(Game.ELIMINATED_TAG)))
				.count();
	}

	public void recordBedDestroyed(String teamName) {
		teamBedStatus.put(teamName, false);
		Logger.info("Bed destroyed for team: {}", teamName);
	}

	public boolean isBedAlive(String teamName) {
		return teamBedStatus.getOrDefault(teamName, false);
	}

	public void setBedStatus(String teamName, boolean alive) {
		teamBedStatus.put(teamName, alive);
	}

	public Map<String, Boolean> getTeamBedStatus() {
		return new HashMap<>(teamBedStatus);
	}
}
