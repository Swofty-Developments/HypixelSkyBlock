package net.swofty.type.bedwarsgame.game;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ChatUtility;
import net.swofty.commons.ServerType;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.MapTeam;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.BedWarsGameScoreboard;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.shop.impl.AxeShopItem;
import net.swofty.type.bedwarsgame.shop.impl.PickaxeShopItem;
import net.swofty.type.bedwarsgame.stats.BedWarsStatsRecorder;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.user.ExperienceCause;
import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public final class Game {

	public static final Tag<Boolean> ELIMINATED_TAG = Tag.Boolean("eliminated");

	private final InstanceContainer instanceContainer;
	private final BedwarsGameType bedwarsGameType = BedwarsGameType.SOLO;
	private final List<BedWarsPlayer> players = new ArrayList<>();
	private final String gameId = UUID.randomUUID().toString();
	private final BedWarsMapsConfig.MapEntry mapEntry;

	private final TeamManager teamManager;
	private final GeneratorManager generatorManager;
	private final GameWorldManager worldManager;
	private final GameCountdown countdown;
	private final GameEventManager eventManager;

	private final Map<TeamKey, Map<Integer, ItemStack>> chests = new HashMap<>();
	private final Map<Player, Map<Integer, ItemStack>> enderchests = new HashMap<>();

	// Track players who disconnected during an active game for rejoin
	private final Map<UUID, DisconnectedPlayerInfo> disconnectedPlayers = new ConcurrentHashMap<>();

	private Task timePlayedTask;

	@Setter
	private GameStatus gameStatus;

	public Game(BedWarsMapsConfig.MapEntry mapEntry, InstanceContainer instanceContainer) {
		this.mapEntry = mapEntry;
		this.instanceContainer = instanceContainer;

		this.teamManager = new TeamManager(this);
		this.generatorManager = new GeneratorManager(this);
		this.worldManager = new GameWorldManager(this);
		this.countdown = new GameCountdown(this);
		this.eventManager = new GameEventManager(this);

		this.gameStatus = GameStatus.WAITING;
		BedWarsGameScoreboard.start(this);
	}

	public void join(BedWarsPlayer player) {
		Logger.info("Player {} joining game {} on map {}", player.getUsername(), gameId, mapEntry.getName());

		if (gameStatus != GameStatus.WAITING) {
			player.sendMessage(Component.text("The game is already in progress or has ended.", NamedTextColor.RED));
			player.sendTo(ServerType.BEDWARS_LOBBY);
			return;
		}

		if (!hasCapacityForPlayer()) {
			player.sendMessage(Component.text(
					"This game is full. You shouldn't have been sent here. You'll be sent back to a lobby.",
					NamedTextColor.RED));
			player.sendTo(ServerType.BEDWARS_LOBBY);
			return;
		}

		setupPlayerForWaiting(player);
		players.add(player);

		int maxPlayers = getMaxPlayers();
		String randomLetters = UUID.randomUUID().toString().replaceAll("-", "")
				.substring(0, new Random().nextInt(10) + 4);
		for (BedWarsPlayer p : players) {
			String name = p.getUuid().compareTo(player.getUuid()) == 0 ? player.getUsername() : "§k" + randomLetters;
			p.sendMessage(name + " §ehas joined (§b" + players.size() + "§e/§b" + maxPlayers + "§e)");
		}
		player.setDisplayName(Component.text(randomLetters, NamedTextColor.WHITE, TextDecoration.OBFUSCATED));

		if (hasMinimumPlayersForStart() && !countdown.isActive()) {
			countdown.startCountdown();
		}
	}

	public void leave(BedWarsPlayer player) {
		TeamKey teamName = player.getTeamKey();
		players.remove(player);

		player.removeTag(Tag.String("gameId"));
		player.removeTag(Tag.String("team"));
		player.sendTo(ServerType.BEDWARS_LOBBY);

		countdown.checkCountdownConditions();

		if (gameStatus == GameStatus.IN_PROGRESS && teamName != null) {
			checkForWinCondition();
		}
	}

	/**
	 * Handle player disconnect during an active game.
	 * Stores their info for potential rejoin instead of removing them permanently.
	 */
	public void handleDisconnect(BedWarsPlayer player) {
		if (gameStatus != GameStatus.IN_PROGRESS) {
			leave(player);
			return;
		}

		TeamKey teamKey = player.getTeamKey();
		boolean bedAlive = teamKey != null && teamManager.isBedAlive(teamKey);

		// Get current upgrade levels
		Integer armorLevel = player.getTag(TypeBedWarsGameLoader.ARMOR_LEVEL_TAG);
		Integer pickaxeLevel = player.getTag(PickaxeShopItem.PICKAXE_UPGRADE_TAG);
		Integer axeLevel = player.getTag(AxeShopItem.AXE_UPGRADE_TAG);

		// Store disconnect info for rejoin
		DisconnectedPlayerInfo info = new DisconnectedPlayerInfo(
				player.getUuid(),
				player.getUsername(),
				teamKey,
				bedAlive,
				armorLevel != null ? armorLevel : 0,
				pickaxeLevel != null ? pickaxeLevel : 0,
				axeLevel != null ? axeLevel : 0
		);
		disconnectedPlayers.put(player.getUuid(), info);

		// Remove from active players list
		players.remove(player);

		// Send disconnect message to remaining players
		String teamColor = teamKey != null ? teamKey.chatColor() : "§7";
		getPlayersAsAudience().sendMessage(
				Component.text(teamColor + player.getUsername() + " §7disconnected.")
		);

		Logger.info("Player {} disconnected from game {} (team: {}, bed alive: {})",
				player.getUsername(), gameId, teamKey, bedAlive);

		// Clear player tags and send to lobby
		player.removeTag(Tag.String("gameId"));
		player.removeTag(Tag.String("team"));
		player.sendTo(ServerType.BEDWARS_LOBBY);

		// Check win condition
		checkForWinCondition();
	}

	/**
	 * Handle player rejoining an active game.
	 */
	public void rejoin(BedWarsPlayer player) {
		DisconnectedPlayerInfo info = disconnectedPlayers.remove(player.getUuid());

		if (info == null) {
			player.sendMessage(Component.text("You don't have a game to rejoin!", NamedTextColor.RED));
			player.sendTo(ServerType.BEDWARS_LOBBY);
			return;
		}

		if (gameStatus != GameStatus.IN_PROGRESS) {
			player.sendMessage(Component.text("The game has ended.", NamedTextColor.RED));
			player.sendTo(ServerType.BEDWARS_LOBBY);
			return;
		}

		// Re-add to players list
		players.add(player);

		// Restore tags
		player.setTag(Tag.String("gameId"), gameId);
		player.setTag(Tag.String("team"), info.getTeamKey().name());

		// Restore upgrade levels
		if (info.getArmorLevel() > 0) {
			player.setTag(TypeBedWarsGameLoader.ARMOR_LEVEL_TAG, info.getArmorLevel());
		}
		if (info.getPickaxeLevel() > 0) {
			player.setTag(PickaxeShopItem.PICKAXE_UPGRADE_TAG, info.getPickaxeLevel());
		}
		if (info.getAxeLevel() > 0) {
			player.setTag(AxeShopItem.AXE_UPGRADE_TAG, info.getAxeLevel());
		}

		// Set to correct instance
		player.setInstance(instanceContainer);

		// Send reconnect message to all players
		String teamColor = info.getTeamKey().chatColor();
		getPlayersAsAudience().sendMessage(
				Component.text(teamColor + player.getUsername() + " §7reconnected.")
		);
		player.getAchievementHandler().completeAchievement("");

		Logger.info("Player {} rejoined game {} (team: {})", player.getUsername(), gameId, info.getTeamKey().getName());

		// check if bed is alive, if not, set up as spectator
		if (!teamManager.isBedAlive(info.getTeamKey())) {
			setupAsSpectator(player);
		} else {
			// Normal rejoin with respawn timer
			triggerRespawnTimer(player, info.getTeamKey());
		}
	}

	private void setupAsSpectator(BedWarsPlayer player) {
		player.setTag(ELIMINATED_TAG, true);
		player.setGameMode(GameMode.SPECTATOR);
		player.setInvisible(true);
		player.setFlying(true);

		BedWarsMapsConfig.Position spectatorPos = mapEntry.getConfiguration().getLocations().getSpectator();
		if (spectatorPos != null) {
			player.teleport(new Pos(spectatorPos.x(), spectatorPos.y(), spectatorPos.z()));
		}

		player.sendTitlePart(TitlePart.TITLE, Component.text("SPECTATING", NamedTextColor.GRAY));
		player.sendTitlePart(TitlePart.SUBTITLE, Component.text("Your bed was destroyed.", NamedTextColor.RED));
	}

	private void triggerRespawnTimer(BedWarsPlayer player, TeamKey teamKey) {
		// Put player in spectator mode temporarily
		player.setGameMode(GameMode.SPECTATOR);
		player.getInventory().clear();

		BedWarsMapsConfig.Position spectatorPos = mapEntry.getConfiguration().getLocations().getSpectator();
		if (spectatorPos != null) {
			player.teleport(new Pos(spectatorPos.x(), spectatorPos.y(), spectatorPos.z()));
		}

		// Start respawn countdown (same as death handler)
		final Title.Times titleTimes = Title.Times.times(Duration.ofMillis(100), Duration.ofSeconds(1), Duration.ofMillis(100));
		final AtomicInteger countdown = new AtomicInteger(5);
		final AtomicReference<Task> taskRef = new AtomicReference<>();

		final Task task = MinecraftServer.getSchedulerManager().buildTask(() -> {
			if (!player.isOnline()) {
				Task currentTask = taskRef.get();
				if (currentTask != null) currentTask.cancel();
				return;
			}

			int secondsRemaining = countdown.getAndDecrement();

			if (secondsRemaining > 0) {
				Component mainTitleText = Component.text("YOU DIED!", NamedTextColor.RED);
				Component subTitleText = Component.text("You will respawn in " + secondsRemaining + " second" + (secondsRemaining == 1 ? "" : "s") + "!", NamedTextColor.YELLOW);
				Title title = Title.title(mainTitleText, subTitleText, titleTimes);
				player.showTitle(title);
			} else {
				// Time to respawn
				player.clearTitle();
				respawnPlayer(player, teamKey);

				// Cancel repeating task
				Task currentTask = taskRef.get();
				if (currentTask != null) {
					currentTask.cancel();
				}
			}
		}).repeat(TaskSchedule.seconds(1)).schedule();
		taskRef.set(task);
	}

	private void respawnPlayer(BedWarsPlayer player, TeamKey teamKey) {
		MapTeam playerTeam = mapEntry.getConfiguration().getTeams().get(teamKey);

		if (playerTeam != null) {
			BedWarsMapsConfig.PitchYawPosition spawnPos = playerTeam.getSpawn();
			player.teleport(new Pos(spawnPos.x(), spawnPos.y(), spawnPos.z(), spawnPos.pitch(), spawnPos.yaw()));
			player.setGameMode(GameMode.SURVIVAL);
			player.setInvisible(false);
			player.setFlying(false);
			player.getInventory().addItemStack(ItemStack.of(Material.WOODEN_SWORD));

			// Give back tools
			AxeShopItem axeShopItem = new AxeShopItem();
			Integer currentAxeLevel = player.getTag(AxeShopItem.AXE_UPGRADE_TAG);
			if (currentAxeLevel != null && currentAxeLevel > 0) {
				player.getInventory().addItemStack(ItemStack.of(axeShopItem.getTier(currentAxeLevel - 1).material()));
			}

			PickaxeShopItem pickaxeShopItem = new PickaxeShopItem();
			Integer currentPickaxeLevel = player.getTag(PickaxeShopItem.PICKAXE_UPGRADE_TAG);
			if (currentPickaxeLevel != null && currentPickaxeLevel > 0) {
				player.getInventory().addItemStack(ItemStack.of(pickaxeShopItem.getTier(currentPickaxeLevel - 1).material()));
			}

			// Equip team armor
			teamManager.equipTeamArmor(player, teamKey);

			// Apply upgrades
			Integer protectionLevel = player.getTag(Tag.Integer("upgrade_reinforced_armor"));
			if (protectionLevel != null) {
				TypeBedWarsGameLoader.getTeamShopManager().getUpgrade("reinforced_armor").applyEffect(this, teamKey, protectionLevel);
			}

			Integer cushionedBootsLevel = player.getTag(Tag.Integer("upgrade_cushioned_boots"));
			if (cushionedBootsLevel != null) {
				TypeBedWarsGameLoader.getTeamShopManager().getUpgrade("cushioned_boots").applyEffect(this, teamKey, cushionedBootsLevel);
			}

			Integer sharpnessLevel = player.getTag(Tag.Integer("upgrade_sharpness"));
			if (sharpnessLevel != null) {
				TypeBedWarsGameLoader.getTeamShopManager().getUpgrade("sharpness").applyEffect(this, teamKey, sharpnessLevel);
			}
		} else {
			Logger.warn("Player {} had team key '{}' but team was not found. Sending to lobby.", player.getUsername(), teamKey.getName());
			player.sendMessage("§cAn unexpected error occurred while respawning you. Please contact a staff member.");
			player.sendTo(ServerType.BEDWARS_LOBBY);
		}
	}

	/**
	 * Check if a player has a pending rejoin for this game.
	 */
	public boolean hasDisconnectedPlayer(UUID playerUuid) {
		return disconnectedPlayers.containsKey(playerUuid);
	}

	/**
	 * Get list of disconnected player UUIDs for heartbeat.
	 */
	public List<UUID> getDisconnectedPlayerUuids() {
		return new ArrayList<>(disconnectedPlayers.keySet());
	}

	public void startGame() {
		Logger.info("Starting game {}", gameId);
		gameStatus = GameStatus.IN_PROGRESS;

		worldManager.clearExistingBeds();
		Map<TeamKey, MapTeam> activeTeams = teamManager.assignPlayersToTeams();
		worldManager.placeBeds(activeTeams);
		worldManager.spawnShopNPCs(activeTeams);
		generatorManager.startTeamGenerators(activeTeams);
		generatorManager.startGlobalGenerators();
		eventManager.start();

		timePlayedTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
			if (gameStatus != GameStatus.IN_PROGRESS) {
				timePlayedTask.cancel();
				return;
			}
			for (BedWarsPlayer player : players) {
				player.xp(ExperienceCause.TIME_PLAYED);
			}
		}).delay(TaskSchedule.minutes(1)).repeat(TaskSchedule.minutes(1)).schedule();

		Logger.info("Game {} started with {} active teams", gameId, activeTeams.size());

		String line = "■".repeat(50);
		Component[] messages = new Component[] {
				Component.text(line, NamedTextColor.GREEN),
				Component.text(ChatUtility.FontInfo.center("Bed Wars"), NamedTextColor.WHITE, TextDecoration.BOLD),
				Component.space(),
				Component.text(ChatUtility.FontInfo.center("Protect your bed and destroy the enemy beds."), NamedTextColor.YELLOW, TextDecoration.BOLD),
				Component.text(ChatUtility.FontInfo.center("Upgrade yourself and your team by collecting"), NamedTextColor.YELLOW, TextDecoration.BOLD),
				Component.text(ChatUtility.FontInfo.center("Iron, Gold, Emerald and Diamond from generators"), NamedTextColor.YELLOW, TextDecoration.BOLD),
				Component.text(ChatUtility.FontInfo.center("to access powerful upgrades."), NamedTextColor.YELLOW, TextDecoration.BOLD),
				Component.space(),
				Component.text(line, NamedTextColor.GREEN)
		};
		for (Component msg : messages) {
			getPlayersAsAudience().sendMessage(msg);
		}
	}

	public void recordBedDestroyed(TeamKey teamKey) {
		teamManager.recordBedDestroyed(teamKey);
		players.forEach(player -> {
			player.sendMessage(teamKey.chatColor() + "Team " + teamKey.getName() + "'s §cbed has been destroyed!");
			player.playSound(Sound.sound(Key.key("minecraft:entity.wither.death"),
					Sound.Source.MASTER, 1f, 1f), Sound.Emitter.self());
		});
		checkForWinCondition();
	}

	public void playerEliminated(BedWarsPlayer player) {
		player.setTag(ELIMINATED_TAG, true);
		TeamKey teamName = player.getTeamKey();
		Logger.info("Player {} from team {} eliminated", player.getUsername(), teamName != null ? teamName : "N/A");
		checkForWinCondition();
	}

	public void checkForWinCondition() {
		if (gameStatus != GameStatus.IN_PROGRESS) return;

		List<TeamKey> viableTeams = mapEntry.getConfiguration().getTeams().keySet().stream()
				.filter(this::isTeamViable)
				.toList();

		if (viableTeams.size() <= 1) {
			TeamKey winningTeam = viableTeams.isEmpty() ? null : viableTeams.getFirst();
			endGame(winningTeam);
		}
	}

	private boolean isTeamViable(TeamKey teamKey) {
		// A team is viable if:
		// 1. Bed is alive, OR
		// 2. Has active (non-eliminated) players online, OR
		// 3. Has disconnected players who can still rejoin
		// 4. Has not been vibecoded by Swofty
		boolean hasActivePlayers = teamManager.countActivePlayersOnTeam(teamKey) > 0;
		boolean hasRejoinablePlayers = disconnectedPlayers.values().stream()
				.anyMatch(info -> info.getTeamKey() == teamKey && teamManager.isBedAlive(info.getTeamKey()));

		return teamManager.isBedAlive(teamKey) || hasActivePlayers || hasRejoinablePlayers;
	}

	private boolean hasCapacityForPlayer() {
		return players.size() < getMaxPlayers();
	}

	public boolean canAcceptNewPlayers() {
		return gameStatus == GameStatus.WAITING;
	}

	public int getAvailableSlots() {
		return Math.max(0, getMaxPlayers() - players.size());
	}

	public String canAcceptPartyWarp() {
		if (gameStatus == GameStatus.IN_PROGRESS) {
			return "Cannot warp - game has already started";
		}
		if (gameStatus == GameStatus.ENDING) {
			return "Cannot warp - game is ending";
		}
		return null;
	}

	private boolean hasMinimumPlayersForStart() {
		int teamSize = Math.max(1, bedwarsGameType.getTeamSize());
		int teamCount = mapEntry.getConfiguration().getTeams().size();
		return players.size() >= teamSize * Math.min(2, teamCount) && teamCount >= 2;
	}

	private int getMaxPlayers() {
		return mapEntry.getConfiguration().getTeams().size() * Math.max(1, bedwarsGameType.getTeamSize());
	}

	private void setupPlayerForWaiting(BedWarsPlayer player) {
		BedWarsMapsConfig.Position waiting = mapEntry.getConfiguration().getLocations().getWaiting();

		if (player.getInstance() == null || player.getInstance().getUuid() != instanceContainer.getUuid()) {
			player.setInstance(instanceContainer, new Pos(waiting.x(), waiting.y(), waiting.z()));
		}

		player.setFlying(false);
		player.setGameMode(GameMode.ADVENTURE);
		player.getInventory().setItemStack(8,
				TypeBedWarsGameLoader.getItemHandler().getItem("leave_game").getItemStack());
		player.setTag(Tag.String("gameId"), gameId);
		player.sendMessage("§8Joined the game on map " + mapEntry.getId() + ".");
	}

	private void endGame(TeamKey winningTeam) {
		if (gameStatus == GameStatus.ENDING) return;

		gameStatus = GameStatus.ENDING;
		Logger.info("Game {} ended. Winner: {}", gameId, winningTeam != null ? winningTeam.getName() : "None (Draw)");

		generatorManager.stopAllGenerators();
		eventManager.stop();

		String titleMessage;
		String subtitleMessage;

		if (winningTeam != null) {
			titleMessage = winningTeam.chatColor() + "Team " + winningTeam.getName() + " has won!";
			subtitleMessage = "Congratulations!";
		} else {
			titleMessage = "§cGame Over!";
			subtitleMessage = "It's a draw!";
		}

		final TeamKey finalWinningTeam = winningTeam;
		players.forEach(player -> {
			player.sendTitlePart(TitlePart.TITLE, Component.text(titleMessage));
			player.sendTitlePart(TitlePart.SUBTITLE, Component.text(subtitleMessage));
			player.playSound(Sound.sound(Key.key("minecraft:ui.toast.challenge_complete"),
					Sound.Source.MASTER, 1f, 1f), Sound.Emitter.self());

			// Record win for players on the winning team
			if (finalWinningTeam != null && finalWinningTeam.getName().equalsIgnoreCase(player.getTeamName())) {
				BedWarsStatsRecorder.recordWin(player, bedwarsGameType);
				player.getAchievementHandler().addProgressByTrigger("bedwars.wins", 1);
			}

			if (player.getGameMode() != GameMode.SPECTATOR) {
				player.setGameMode(GameMode.SPECTATOR);
			}
		});

		MinecraftServer.getSchedulerManager().buildTask(() -> {
			players.forEach(this::leave);
			players.clear();
			TypeBedWarsGameLoader.getGames().remove(this);
			TypeBedWarsGameLoader.createGame(mapEntry);
		}).delay(TaskSchedule.seconds(10)).schedule();
	}

	public Audience getPlayersAsAudience() {
		return Audience.audience(players);
	}

}
