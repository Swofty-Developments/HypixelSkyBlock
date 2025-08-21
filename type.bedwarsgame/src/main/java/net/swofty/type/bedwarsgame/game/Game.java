package net.swofty.type.bedwarsgame.game;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.scoreboard.BelowNameTag;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServerType;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.entity.BedWarsShopNPC;
import net.swofty.type.bedwarsgame.entity.TextDisplayEntity;
import net.swofty.type.bedwarsgame.map.MapsConfig;
import net.swofty.type.bedwarsgame.util.ColorUtil;
import net.swofty.type.bedwarsgame.util.ComponentManipulator;
import net.swofty.type.generic.user.HypixelPlayer;
import org.intellij.lang.annotations.Subst;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Game {

	public static final Tag<Boolean> ELIMINATED_TAG = Tag.Boolean("eliminated");
	private final InstanceContainer instanceContainer;
	private final GameType gameType = GameType.SOLO;
	private final List<Player> players = new ArrayList<>();
	private final String gameId;
	private final char greenMark = '✔';
	private final char redMark = '✖';
	private final Map<String, Boolean> teamBedStatus = new HashMap<>();
	private final Map<String, Map<String, Integer>> teamUpgrades = new HashMap<>();
	private final Map<String, java.util.List<String>> teamTraps = new java.util.HashMap<>();
	private final Map<String, List<net.minestom.server.timer.Task>> teamGeneratorTasks = new HashMap<>();
	private final Map<String, Map<Integer, ItemStack>> chests = new HashMap<>();
	private final Map<Player, Map<Integer, ItemStack>> enderchests = new HashMap<>();
	private final Map<String, List<GeneratorDisplays>> generatorDisplays = new HashMap<>();
	private final MapsConfig.MapEntry mapEntry;
	@Setter
	private GameStatus gameStatus;
	private boolean countdownStarted = false;

	public Game(MapsConfig.MapEntry mapEntry, InstanceContainer instanceContainer) {
		this.mapEntry = mapEntry;
		this.gameId = createGameId();
		this.instanceContainer = instanceContainer;

		gameStatus = GameStatus.WAITING;
	}

	public int getTeamUpgradeLevel(String teamName, String upgradeKey) {
		return teamUpgrades.computeIfAbsent(teamName, k -> new HashMap<>()).getOrDefault(upgradeKey, 0);
	}

	public void setTeamUpgradeLevel(String teamName, String upgradeKey, int level) {
		teamUpgrades.computeIfAbsent(teamName, k -> new HashMap<>()).put(upgradeKey, level);
	}

	public java.util.List<String> getTeamTraps(String teamName) {
		return teamTraps.computeIfAbsent(teamName, k -> new java.util.ArrayList<>());
	}

	public void removeTeamTrap(String teamName, String trapKey) {
		var traps = teamTraps.get(teamName);
		if (traps != null) {
			traps.remove(trapKey);
			if (traps.isEmpty()) {
				teamTraps.remove(teamName);
			}
		}
	}

	public void addTeamTrap(String teamName, String trapKey) {
		var traps = teamTraps.computeIfAbsent(teamName, k -> new java.util.ArrayList<>());
		if (!traps.contains(trapKey)) {
			traps.add(trapKey);
		}
	}

	public String createGameId() {
		return String.valueOf(System.currentTimeMillis());
	}

	public void join(Player player) {
		Logger.info("Player {} is attempting to join game {}", player.getUsername(), gameId);
		if (gameStatus != GameStatus.WAITING) {
			player.sendMessage(Component.text("The game is already in progress or has ended.", NamedTextColor.RED));
			return;
		}

		int teamSize = gameType.getTeamSize();
		if (teamSize <= 0) teamSize = 1;
		int maxPlayers = mapEntry.getConfiguration().getTeams().size() * teamSize;

		if (players.size() >= maxPlayers) {
			player.sendMessage(Component.text("This game is full. Proxy shouldn't have sent you here. Sending you back to lobby", NamedTextColor.RED));
			// TODO: Handle sending player back to lobby or another instance
			return;
		}

		MapsConfig.Position waiting = mapEntry.getConfiguration().getLocations().getWaiting();
		if (player.getInstance() == null || player.getInstance().getUuid() != instanceContainer.getUuid()) {
			player.setInstance(instanceContainer, new Pos(waiting.x(), waiting.y(), waiting.z()));
		} else {
			Logger.info("Player seems to be already here, interesting.");
		}
		player.setFlying(false);

		player.setGameMode(GameMode.ADVENTURE);
		player.getInventory().setItemStack(
				8,
				ItemStack.of(Material.RED_BED)
						.withCustomName(ComponentManipulator.noItalic(Component.text("Leave").color(NamedTextColor.RED)))
						.withTag(Tag.String("action"), "leave")
		);
		//sidebar.addViewer(player);
		player.setTag(Tag.String("gameId"), gameId);
		player.sendMessage("You have joined the game on map: " + mapEntry.getId());

		players.add(player);
		updatePlayerCount();

		if (players.size() >= (teamSize * Math.min(2, mapEntry.getConfiguration().getTeams().size())) && !countdownStarted) {
			if (mapEntry.getConfiguration().getTeams().size() >= 2) { // Only start if there are at least 2 configured teams
				countdownStarted = true;
				//sidebar.updateLineContent("status_line", Component.text("Starting in 15s").color(NamedTextColor.YELLOW));
				//Server.getInstance().getScheduler().buildTask(this::startGame).delay(TaskSchedule.seconds(15)).schedule();
			}
		}
	}

	private void updatePlayerCount() {
		int teamSize = gameType.getTeamSize();
		if (teamSize <= 0) teamSize = 1;
		int maxPlayers = mapEntry.getConfiguration().getTeams().size() * teamSize;
		String text = "Players: " + players.size() + "/" + maxPlayers;
		//sidebar.updateLineContent("players_count", Component.text(text).color(NamedTextColor.GREEN));
	}

	public void startGame() {
		gameStatus = GameStatus.IN_PROGRESS;
		List<MapsConfig.MapEntry.MapConfiguration.MapTeam> configuredTeams = mapEntry.getConfiguration().getTeams();
		List<MapsConfig.MapEntry.MapConfiguration.MapTeam> activeGameTeams = new ArrayList<>();
		teamBedStatus.clear();

		// Default all teams to no bed initially
		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : configuredTeams) {
			teamBedStatus.put(team.getName(), false);
		}

		for (Player p : players) {
			p.removeTag(ELIMINATED_TAG);
		}

		// Clear any existing beds from all configured team locations
		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : configuredTeams) {
			MapsConfig.TwoBlockPosition bedPositions = team.getBed();
			if (bedPositions != null) {
				if (bedPositions.feet() != null) {
					instanceContainer.setBlock((int) bedPositions.feet().x(), (int) bedPositions.feet().y(), (int) bedPositions.feet().z(), Block.AIR);
				}
				if (bedPositions.head() != null) {
					instanceContainer.setBlock((int) bedPositions.head().x(), (int) bedPositions.head().y(), (int) bedPositions.head().z(), Block.AIR);
				}
			}
		}

		List<Player> playersToAssign = new ArrayList<>(this.players);
		Collections.shuffle(playersToAssign);

		List<MapsConfig.MapEntry.MapConfiguration.MapTeam> availableTeams = new ArrayList<>(configuredTeams);
		Collections.shuffle(availableTeams);

		int configuredTeamSize = gameType.getTeamSize();
		if (configuredTeamSize <= 0) configuredTeamSize = 1;

		int playerAssignIndex = 0;
		MapsConfig.Position specPos = mapEntry.getConfiguration().getLocations().getSpectator();

		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : availableTeams) {
			if (playerAssignIndex >= playersToAssign.size()) break;
			boolean teamReceivedPlayers = false;
			for (int memberCount = 0; memberCount < configuredTeamSize; memberCount++) {
				if (playerAssignIndex >= playersToAssign.size()) break;
				Player p = playersToAssign.get(playerAssignIndex);

				p.setTag(Tag.String("team"), team.getName());
				p.setTag(Tag.String("teamColor"), team.getColor());
				p.setTag(Tag.String("javaColor"), team.getJavacolor());

				MapsConfig.PitchYawPosition spawnPos = team.getSpawn();
				p.teleport(new Pos(spawnPos.x(), spawnPos.y(), spawnPos.z(), spawnPos.yaw(), spawnPos.pitch()));
				p.setRespawnPoint(new Pos(specPos.x(), specPos.y(), specPos.z()));
				/*sidebar.removeViewer(p);
				gameSidebar.addViewer(p);
*/
				p.setGameMode(GameMode.SURVIVAL);
				p.getInventory().clear();
				p.getInventory().addItemStack(ItemStack.of(Material.WOODEN_SWORD));
				p.setEnableRespawnScreen(false);
				p.setDisplayName(
						MiniMessage.miniMessage().deserialize("<" + team.getColor().toLowerCase() + "><b>" + team.getName().toUpperCase().charAt(0) + "</b> " + p.getUsername())
				);
				p.setBelowNameTag(new BelowNameTag("health", MiniMessage.miniMessage().deserialize("<red>20❤</red>")));

				java.awt.Color awtColor = ColorUtil.getColorByName(team.getJavacolor());
				if (awtColor != null) {
					net.minestom.server.color.Color minestomColor = new net.minestom.server.color.Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
					p.setEquipment(EquipmentSlot.BOOTS, ItemStack.of(Material.LEATHER_BOOTS).with(DataComponents.DYED_COLOR, minestomColor));
					p.setEquipment(EquipmentSlot.LEGGINGS, ItemStack.of(Material.LEATHER_LEGGINGS).with(DataComponents.DYED_COLOR, minestomColor));
					p.setEquipment(EquipmentSlot.CHESTPLATE, ItemStack.of(Material.LEATHER_CHESTPLATE).with(DataComponents.DYED_COLOR, minestomColor));
					p.setEquipment(EquipmentSlot.HELMET, ItemStack.of(Material.LEATHER_HELMET).with(DataComponents.DYED_COLOR, minestomColor));
				} else {
					Logger.warn("Invalid color name '{}' for team '{}'. Player '{}' will not have colored armor.", team.getColor(), team.getName(), p.getUsername());
				}
				playerAssignIndex++;
				teamReceivedPlayers = true;
			}
			if (teamReceivedPlayers) {
				activeGameTeams.add(team);
			}
		}

		if (playerAssignIndex < playersToAssign.size()) {
			Logger.warn("{} players were not assigned to a team as all team slots were filled.", playersToAssign.size() - playerAssignIndex);
			for (int i = playerAssignIndex; i < playersToAssign.size(); i++) {
				Player unassignedPlayer = playersToAssign.get(i);
				unassignedPlayer.sendMessage(Component.text("All team slots were full. You have been moved to spectator.", NamedTextColor.YELLOW));
				unassignedPlayer.setGameMode(GameMode.SPECTATOR);
				/*gameSidebar.removeViewer(unassignedPlayer);
				sidebar.removeViewer(unassignedPlayer);*/
				unassignedPlayer.teleport(new Pos(specPos.x(), specPos.y(), specPos.z()));
			}
		}

		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : activeGameTeams) {
			MapsConfig.TwoBlockPosition bedPositions = team.getBed();
			if (bedPositions != null && bedPositions.feet() != null && bedPositions.head() != null) {
				MapsConfig.Position feetPos = bedPositions.feet();
				MapsConfig.Position headPos = bedPositions.head();
				try {
					@Subst("red_bed") String bedBlockName = team.getColor().toLowerCase() + "_bed";
					Material bedMaterial = Material.fromKey(Key.key(Key.MINECRAFT_NAMESPACE, bedBlockName));
					if (bedMaterial == null) {
						//Server.getLogger().warn("Could not find bed material for color: {}. Defaulting to RED_BED.", team.getColor());
						bedMaterial = Material.RED_BED;
					}
					String facing = "north";
					if (headPos.x() > feetPos.x()) facing = "east";
					else if (headPos.x() < feetPos.x()) facing = "west";
					else if (headPos.z() > feetPos.z()) facing = "south";

					Block footBlock = bedMaterial.block().withProperty("part", "foot").withProperty("facing", facing);
					Block headBlock = bedMaterial.block().withProperty("part", "head").withProperty("facing", facing);
					instanceContainer.setBlock((int) feetPos.x(), (int) feetPos.y(), (int) feetPos.z(), footBlock);
					instanceContainer.setBlock((int) headPos.x(), (int) headPos.y(), (int) headPos.z(), headBlock);
					teamBedStatus.put(team.getName(), true); // Bed placed successfully
					//Server.getLogger().info("Placed {} bed for active team {} (foot: {}, head: {}, facing: {}).", team.getColor(), team.getName(), feetPos, headPos, facing);
				} catch (IllegalArgumentException e) {
					//Server.getLogger().error("Error placing bed for team {}: {}", team.getName(), e.getMessage());
					instanceContainer.setBlock((int) feetPos.x(), (int) feetPos.y(), (int) feetPos.z(), Block.STONE);
					instanceContainer.setBlock((int) headPos.x(), (int) headPos.y(), (int) headPos.z(), Block.STONE);
				}
			} else {
				//Server.getLogger().warn("Bed position not fully defined for active team: {}. Skipping bed placement.", team.getName());
			}
		}

		// Update sidebar for all teams based on final bed status and player assignments
		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : configuredTeams) {
			updateTeamSidebar(team);
		}

		//sidebar.updateLineContent("status_line", Component.text("In Progress").color(NamedTextColor.GREEN));

		MapsConfig.MapEntry.MapConfiguration mapConfig = mapEntry.getConfiguration();
		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : activeGameTeams) {
			if (team.getShop() != null) {
				MapsConfig.PitchYawPosition shopPos = team.getShop().item();
				MapsConfig.PitchYawPosition teamPos = team.getShop().team();
				if (shopPos != null) {
					BedWarsShopNPC bedWarsShopNpc = new BedWarsShopNPC("Item Shop", BedWarsShopNPC.NPCType.SHOP);
					bedWarsShopNpc.setInstance(instanceContainer, new Pos(shopPos.x(), shopPos.y(), shopPos.z(), shopPos.yaw(), shopPos.pitch())).join();
				}
				if (teamPos != null) {
					BedWarsShopNPC bedWarsShopNpc = new BedWarsShopNPC("Team Upgrades", BedWarsShopNPC.NPCType.TEAM);
					bedWarsShopNpc.setInstance(instanceContainer, new Pos(teamPos.x(), teamPos.y(), teamPos.z(), teamPos.yaw(), shopPos.yaw())).join();
				}
			}
		}

		if (mapConfig.getGenerator() != null && !mapConfig.getGenerator().isEmpty()) {
			for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : activeGameTeams) {
				MapsConfig.Position genLocation = team.getGenerator();
				if (genLocation == null) continue;
				Pos spawnPosition = new Pos(genLocation.x(), genLocation.y(), genLocation.z());
				for (Map.Entry<String, MapsConfig.MapEntry.MapConfiguration.TeamGeneratorConfig> genEntry : mapConfig.getGenerator().entrySet()) {
					@Subst("iron") String materialType = genEntry.getKey().toLowerCase();
					MapsConfig.MapEntry.MapConfiguration.TeamGeneratorConfig genConfig = genEntry.getValue();
					Material itemMaterial;
					try {
						itemMaterial = switch (materialType) {
							case "iron" -> Material.IRON_INGOT;
							case "gold" -> Material.GOLD_INGOT;
							default -> {
								Material mat = Material.fromKey(Key.key(Key.MINECRAFT_NAMESPACE, materialType));
								if (mat == null)
									throw new IllegalArgumentException("Invalid material: " + materialType);
								yield mat;
							}
						};
					} catch (IllegalArgumentException e) {
						//Server.getLogger().warn("Invalid material for team generator type: {} for team {}. Error: {}", materialType, team.getName(), e.getMessage());
						continue;
					}

					final MapsConfig.MapEntry.MapConfiguration.MapTeam currentTeam = team;
					final Material currentMaterial = itemMaterial;
					final int baseAmount = genConfig.getAmount();
					final int baseDelay = genConfig.getDelay();

					var task = MinecraftServer.getSchedulerManager().buildTask(() -> {
						if (getGameStatus() != GameStatus.IN_PROGRESS) return;

						int forgeLevel = getTeamUpgradeLevel(currentTeam.getName(), "forge");
						double multiplier = 1.0;
						if (currentMaterial == Material.IRON_INGOT || currentMaterial == Material.GOLD_INGOT) {
							if (forgeLevel == 1) multiplier = 1.5;      // +50%
							else if (forgeLevel == 2) multiplier = 2.0; // +100%
							else if (forgeLevel >= 4) multiplier = 3.0; // +200%
						}

						int finalAmount = (int) Math.round(baseAmount * multiplier);
						if (finalAmount == 0 && baseAmount > 0 && multiplier > 1.0) finalAmount = 1;

						if (finalAmount > 0) {
							ItemStack itemToSpawn = ItemStack.of(currentMaterial, finalAmount);
							ItemEntity itemEntity = new ItemEntity(itemToSpawn);
							itemEntity.setPickupDelay(Duration.ofMillis(500));
							itemEntity.setInstance(getInstanceContainer(), spawnPosition);
						}
					}).delay(TaskSchedule.seconds(baseDelay)).repeat(TaskSchedule.seconds(baseDelay)).schedule();
					addTeamGeneratorTask(team.getName(), task);
				}
			}
		}

		if (mapConfig.getGlobal_generator() != null) {
			for (Map.Entry<String, MapsConfig.MapEntry.MapConfiguration.GlobalGenerator> entry : mapConfig.getGlobal_generator().entrySet()) {
				@Subst("diamond") String generatorTypeKey = entry.getKey();
				MapsConfig.MapEntry.MapConfiguration.GlobalGenerator genConfig = entry.getValue();
				Material itemMaterial;
				try {
					itemMaterial = Material.fromKey(Key.key(Key.MINECRAFT_NAMESPACE, generatorTypeKey));
				} catch (IllegalArgumentException e) {
					//Server.getLogger().warn("Invalid key for global generator type: {}", generatorTypeKey);
					continue;
				}
				if (itemMaterial == null) {
					//Server.getLogger().warn("Invalid material for global generator type: {}", generatorTypeKey);
					continue;
				}
				int delaySeconds = genConfig.getDelay();
				int amount = genConfig.getAmount();
				int maxAmount = genConfig.getMax();
				List<MapsConfig.Position> genLocations = genConfig.getLocations();
				if (genLocations == null || genLocations.isEmpty()) continue;

				if (generatorTypeKey.equals("diamond") || generatorTypeKey.equals("emerald")) {
					NamedTextColor generatorColor = generatorTypeKey.equals("diamond") ?
							NamedTextColor.AQUA : NamedTextColor.GREEN;

					for (MapsConfig.Position loc : genLocations) {
						Component generatorTitle = Component.text(generatorTypeKey.substring(0, 1).toUpperCase() + generatorTypeKey.substring(1))
								.color(generatorColor)
								.decorate(TextDecoration.BOLD);

						Component tierText = Component.text("Tier I")
								.color(NamedTextColor.YELLOW);

						Component spawnText = MiniMessage.miniMessage().deserialize("<yellow>Spawns in <red>" + delaySeconds + "</red> seconds!</yellow>");

						TextDisplayEntity tierDisplay = new TextDisplayEntity(tierText);
						tierDisplay.setInstance(instanceContainer, new Pos(loc.x(), loc.y() + 4.0, loc.z()));

						TextDisplayEntity generatorDisplay = new TextDisplayEntity(generatorTitle);
						generatorDisplay.setInstance(instanceContainer, new Pos(loc.x(), loc.y() + 3.7, loc.z()));

						TextDisplayEntity spawnDisplay = new TextDisplayEntity(spawnText);
						spawnDisplay.setInstance(instanceContainer, new Pos(loc.x(), loc.y() + 3.4, loc.z()));

						GeneratorDisplays displays = new GeneratorDisplays(spawnDisplay, delaySeconds);
						generatorDisplays.computeIfAbsent(generatorTypeKey, k -> new ArrayList<>()).add(displays);

						final MapsConfig.Position genPos = loc;
						final Material genMaterial = itemMaterial;
						final int genAmount = amount;
						final int genMaxAmount = maxAmount;
						final int genDelay = delaySeconds;
						final GeneratorDisplays finalDisplays = displays;

						MinecraftServer.getSchedulerManager().buildTask(() -> {
							if (gameStatus != GameStatus.IN_PROGRESS) return;

							finalDisplays.countdown--;
							if (finalDisplays.countdown <= 0) {
								finalDisplays.countdown = genDelay;
								Pos spawnPos = new Pos(genPos.x(), genPos.y(), genPos.z());

								long currentItemCount = instanceContainer.getNearbyEntities(spawnPos, 1.5).stream()
										.filter(ItemEntity.class::isInstance)
										.map(ItemEntity.class::cast)
										.filter(e -> e.getItemStack().material() == genMaterial)
										.mapToLong(e -> e.getItemStack().amount())
										.sum();

								if (currentItemCount < genMaxAmount) {
									ItemStack itemToSpawn = ItemStack.of(genMaterial, genAmount);
									ItemEntity itemEntity = new ItemEntity(itemToSpawn);
									itemEntity.setPickupDelay(Duration.ofSeconds(1));
									itemEntity.setInstance(instanceContainer, spawnPos);
									itemEntity.setVelocity(new Vec(0, 0.1, 0));
								}
							}
							finalDisplays.spawnDisplay.setText(MiniMessage.miniMessage().deserialize(String.format("<yellow>Spawns in <red>%d</red> seconds!</yellow>", finalDisplays.countdown)));
						}).delay(TaskSchedule.seconds(1)).repeat(TaskSchedule.seconds(1)).schedule();
					}
				}
			}
		}
	}

	public void updateTeamSidebar(MapsConfig.MapEntry.MapConfiguration.MapTeam team) {
		if (team == null) return;
		boolean bedExists = teamBedStatus.getOrDefault(team.getName(), false);
		int alivePlayers = 0;
		for (Player p : this.players) {
			String playerTeamName = p.getTag(Tag.String("team"));
			// Player is alive for the team if they are on the team, not spectator, online, and not eliminated
			if (team.getName().equals(playerTeamName) &&
					p.getGameMode() != GameMode.SPECTATOR &&
					p.isOnline() &&
					!Boolean.TRUE.equals(p.getTag(ELIMINATED_TAG))) {
				alivePlayers++;
			}
		}

		String teamName = team.getName();
		String teamColor = team.getColor().toLowerCase();
		String teamInitial = teamName.substring(0, 1).toUpperCase();
		String capitalizedTeamName = teamName.substring(0, 1).toUpperCase() + teamName.substring(1).toLowerCase();
		String lineId = String.format("team_%s", teamName.toLowerCase());

		Component lineText;
		if (bedExists) {
			lineText = MiniMessage.miniMessage().deserialize(String.format("<%s><b>%s</b> <white>%s:</white> <green>%s</%s>", teamColor, teamInitial, capitalizedTeamName, greenMark, teamColor));
		} else {
			if (alivePlayers > 0) {
				lineText = MiniMessage.miniMessage().deserialize(String.format("<%s><b>%s</b> <white>%s:</white> <green>%d</%s>", teamColor, teamInitial, capitalizedTeamName, alivePlayers, teamColor));
			} else {
				lineText = MiniMessage.miniMessage().deserialize(String.format("<%s><b>%s</b> <white>%s:</white> <red>%s</%s>", teamColor, teamInitial, capitalizedTeamName, redMark, teamColor));
			}
		}
		//gameSidebar.updateLineContent(lineId, lineText);
	}

	public void recordBedDestroyed(String teamName) {
		teamBedStatus.put(teamName, false);
		//Server.getLogger().info("Bed destroyed for team: {}", teamName);
		for (Player player : players) {
			player.sendMessage(MiniMessage.miniMessage().deserialize(String.format("<red>Team %s's bed has been destroyed!</red>", teamName)));
			player.playSound(Sound.sound(Key.key("minecraft:entity.wither.death"), Sound.Source.MASTER, 1f, 1f), Sound.Emitter.self());
		}
		notifyPlayerOrBedStateChanged(teamName);
		checkForWinCondition();
	}

	public void notifyPlayerOrBedStateChanged(String teamName) {
		if (teamName == null) return;
		mapEntry.getConfiguration().getTeams().stream()
				.filter(t -> t.getName().equalsIgnoreCase(teamName))
				.findFirst()
				.ifPresent(this::updateTeamSidebar);
	}

	public void leave(Player player) {
		String teamName = player.getTag(Tag.String("team"));
		//sidebar.removeViewer(player);
		//gameSidebar.removeViewer(player);
		players.remove(player);

		player.removeTag(Tag.String("gameId"));
		player.removeTag(Tag.String("team"));
		player.removeTag(Tag.String("teamColor"));
		player.removeTag(Tag.String("javaColor"));

		((HypixelPlayer) player).sendTo(ServerType.BEDWARS_LOBBY);
		updatePlayerCount();

		if (gameStatus == GameStatus.IN_PROGRESS && teamName != null) {
			notifyPlayerOrBedStateChanged(teamName);
		}
		// TODO: Add logic to check if game should end if a team is eliminated or not enough players.
	}

	public void playerEliminated(Player player) {
		player.setTag(ELIMINATED_TAG, true);
		String teamName = player.getTag(Tag.String("team"));
		//Server.getLogger().info("Player {} from team {} has been finally eliminated.", player.getUsername(), teamName != null ? teamName : "N/A");
		if (teamName != null) {
			notifyPlayerOrBedStateChanged(teamName); // Update sidebar for the team
		}
		checkForWinCondition();
	}

	private int countActivePlayersOnTeam(MapsConfig.MapEntry.MapConfiguration.MapTeam team) {
		int count = 0;
		for (Player p : this.players) {
			String playerTeamName = p.getTag(Tag.String("team"));
			if (team.getName().equals(playerTeamName) &&
					p.isOnline() &&
					!Boolean.TRUE.equals(p.getTag(ELIMINATED_TAG))) {
				count++;
			}
		}
		return count;
	}

	public void checkForWinCondition() {
		if (gameStatus != GameStatus.IN_PROGRESS) return;

		List<MapsConfig.MapEntry.MapConfiguration.MapTeam> teamsWithActivePlayers = new ArrayList<>();
		List<MapsConfig.MapEntry.MapConfiguration.MapTeam> activeGameTeams = mapEntry.getConfiguration().getTeams().stream()
				.filter(team -> players.stream().anyMatch(p -> team.getName().equals(p.getTag(Tag.String("team")))))
				.collect(Collectors.toList()); // Consider only teams that had players at game start

		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : activeGameTeams) {
			boolean bedOk = teamBedStatus.getOrDefault(team.getName(), false);
			int aliveTeamMembers = countActivePlayersOnTeam(team);

			if (bedOk || aliveTeamMembers > 0) {
				teamsWithActivePlayers.add(team);
			}
		}

		if (teamsWithActivePlayers.size() <= 1) {
			MapsConfig.MapEntry.MapConfiguration.MapTeam winningTeam = teamsWithActivePlayers.isEmpty() ? null : teamsWithActivePlayers.get(0);
			endGame(winningTeam);
		}
	}

	private void endGame(MapsConfig.MapEntry.MapConfiguration.MapTeam winningTeam) {
		if (gameStatus == GameStatus.ENDING) return;
		gameStatus = GameStatus.ENDING;
		// Server.getLogger().info("Game {} has ended. Winner: {}", gameId, winningTeam != null ? winningTeam.getName() : "None (Draw)");

		teamGeneratorTasks.values().stream().flatMap(List::stream).forEach(net.minestom.server.timer.Task::cancel);
		teamGeneratorTasks.clear();

		Component titleMessage;
		Component subtitleMessage;

		if (winningTeam != null) {
			titleMessage = MiniMessage.miniMessage().deserialize(String.format("<%s>Team %s has won!</%s>", winningTeam.getColor().toLowerCase(), winningTeam.getName(), winningTeam.getColor().toLowerCase()));
			subtitleMessage = Component.text("Congratulations!");
		} else {
			titleMessage = Component.text("Game Over!", NamedTextColor.RED);
			subtitleMessage = Component.text("It's a draw!");
		}

		for (Player player : players) {
			player.sendTitlePart(TitlePart.TITLE, titleMessage);
			player.sendTitlePart(TitlePart.SUBTITLE, subtitleMessage);
			player.playSound(Sound.sound(Key.key("minecraft:ui.toast.challenge_complete"), Sound.Source.MASTER, 1f, 1f), Sound.Emitter.self());
			// Optionally, make everyone spectator if not already
			if (player.getGameMode() != GameMode.SPECTATOR) {
				player.setGameMode(GameMode.SPECTATOR);
			}
		}

		MinecraftServer.getSchedulerManager().buildTask(() -> {
			for (Player player : new ArrayList<>(players)) {
				leave(player);
			}
			players.clear();
			TypeBedWarsGameLoader.getGames().remove(this);
			TypeBedWarsGameLoader.createGame(mapEntry);
		}).delay(TaskSchedule.seconds(10)).schedule();
	}

	public void addTeamGeneratorTask(String teamName, net.minestom.server.timer.Task task) {
		teamGeneratorTasks.computeIfAbsent(teamName, k -> new ArrayList<>()).add(task);
	}

	public void respawnAllBeds() {
		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : mapEntry.getConfiguration().getTeams()) {
			if (teamBedStatus.getOrDefault(team.getName(), true)) {
				MapsConfig.TwoBlockPosition bedPositions = team.getBed();
				if (bedPositions != null && bedPositions.feet() != null && bedPositions.head() != null) {
					MapsConfig.Position feetPos = bedPositions.feet();
					MapsConfig.Position headPos = bedPositions.head();
					try {
						@Subst("red_bed") String bedBlockName = team.getColor().toLowerCase() + "_bed";
						Material bedMaterial = Material.fromKey(Key.key(Key.MINECRAFT_NAMESPACE, bedBlockName));
						if (bedMaterial == null) {
							//Server.getLogger().warn("Could not find bed material for color: {}. Defaulting to RED_BED.", team.getColor());
							bedMaterial = Material.RED_BED;
						}
						String facing = "north";
						if (headPos.x() > feetPos.x()) facing = "east";
						else if (headPos.x() < feetPos.x()) facing = "west";
						else if (headPos.z() > feetPos.z()) facing = "south";

						Block footBlock = bedMaterial.block().withProperty("part", "foot").withProperty("facing", facing);
						Block headBlock = bedMaterial.block().withProperty("part", "head").withProperty("facing", facing);
						instanceContainer.setBlock((int) feetPos.x(), (int) feetPos.y(), (int) feetPos.z(), footBlock);
						instanceContainer.setBlock((int) headPos.x(), (int) headPos.y(), (int) headPos.z(), headBlock);
						//Server.getLogger().info("Respawned {} bed for team {} at positions: foot: {}, head: {}, facing: {}.", team.getColor(), team.getName(), feetPos, headPos, facing);
					} catch (IllegalArgumentException e) {
						//Server.getLogger().error("Error respawning bed for team {}: {}", team.getName(), e.getMessage());
					}
				} else {
					//Server.getLogger().warn("Bed position not fully defined for team: {}. Skipping respawn.", team.getName());
				}
			}
		}
	}

	public List<Player> getPlayersOnTeam(String teamName) {
		return players.stream()
				.filter(p -> teamName.equals(p.getTag(Tag.String("team"))) && p.isOnline() && !Boolean.TRUE.equals(p.getTag(ELIMINATED_TAG)))
				.collect(Collectors.toList());
	}

	private static class GeneratorDisplays {
		private final TextDisplayEntity spawnDisplay;
		private int countdown;

		public GeneratorDisplays(TextDisplayEntity spawnDisplay, int delay) {
			this.spawnDisplay = spawnDisplay;
			this.countdown = delay;
		}
	}

}
