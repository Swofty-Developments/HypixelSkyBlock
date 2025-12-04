package net.swofty.type.bedwarsgame.game;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServerType;
import net.swofty.type.bedwarsgame.BedWarsGameScoreboard;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.user.ExperienceCause;
import net.swofty.commons.BedwarsGameType;
import net.swofty.type.bedwarsgeneric.game.MapsConfig;
import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public final class Game {

	public static final Tag<Boolean> ELIMINATED_TAG = Tag.Boolean("eliminated");

	private final InstanceContainer instanceContainer;
	private final BedwarsGameType bedwarsGameType = BedwarsGameType.SOLO;
	private final List<BedWarsPlayer> players = new ArrayList<>();
	private final String gameId;
	private final MapsConfig.MapEntry mapEntry;

	private final TeamManager teamManager;
	private final GeneratorManager generatorManager;
	private final GameWorldManager worldManager;
	private final GameCountdown countdown;
	private final GameEventManager eventManager;

	private final Map<String, Map<Integer, ItemStack>> chests = new HashMap<>();
	private final Map<Player, Map<Integer, ItemStack>> enderchests = new HashMap<>();

	private Task timePlayedTask;

	@Setter
	private GameStatus gameStatus;

	public Game(MapsConfig.MapEntry mapEntry, InstanceContainer instanceContainer) {
		this.mapEntry = mapEntry;
		this.gameId = createGameId();
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
		Logger.info("Player {} is attempting to join game {} with map {}", player.getUsername(), gameId, mapEntry.getName());

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
		updatePlayerCount();

		// Check if we can start the countdown
		if (hasMinimumPlayersForStart() && !countdown.isActive()) {
			countdown.startCountdown();
		}

		/*BedWarsDataHandler data = BedWarsDataHandler.getUser(player);
		if (data != null) {
			var counts = data.get(BedWarsDataHandler.Data.MAP_JOIN_COUNTS, DatapointMapStringLong.class).getValue();
			counts.put(mapEntry.getId(), counts.getOrDefault(mapEntry.getId(), 0L) + 1);
			data.get(BedWarsDataHandler.Data.MAP_JOIN_COUNTS, DatapointMapStringLong.class).setValue(counts);
		}*/
	}

	/**
	 * Handles a player leaving at any point in the game.
	 *
	 * @param player the player leaving
	 */
	public void leave(HypixelPlayer player) {
		String teamName = player.getTag(Tag.String("team"));
		players.remove(player);

		cleanupPlayerTags(player);
		player.sendTo(ServerType.BEDWARS_LOBBY);
		updatePlayerCount();

		countdown.checkCountdownConditions();

		if (gameStatus == GameStatus.IN_PROGRESS && teamName != null) {
			checkForWinCondition();
		}
	}

	public void startGame() {
		Logger.info("Starting game {}", gameId);
		gameStatus = GameStatus.IN_PROGRESS;
		worldManager.clearExistingBeds();
		List<MapsConfig.MapEntry.MapConfiguration.MapTeam> activeTeams = teamManager.assignPlayersToTeams();
		worldManager.placeBeds(activeTeams);
		worldManager.spawnShopNPCs(activeTeams);
		generatorManager.startTeamGenerators(activeTeams);
		generatorManager.startGlobalGenerators();
		eventManager.start();
		timePlayedTask = MinecraftServer.getSchedulerManager().buildTask(
				() -> {
					if (gameStatus != GameStatus.IN_PROGRESS) {
						timePlayedTask.cancel();
						return;
					}
					for (BedWarsPlayer player : players) {
						player.xp(ExperienceCause.TIME_PLAYED);
					}
				}
		).delay(TaskSchedule.minutes(1)).repeat(TaskSchedule.minutes(1)).schedule();
		Logger.info("Game {} started with {} active teams", gameId, activeTeams.size());
	}

	public void recordBedDestroyed(String teamName) {
		teamManager.recordBedDestroyed(teamName);

		// Notify all players
		Component message = MiniMessage.miniMessage()
				.deserialize("<red>Team " + teamName + "'s bed has been destroyed!</red>");

		players.forEach(player -> {
			player.sendMessage(message);
			player.playSound(Sound.sound(Key.key("minecraft:entity.wither.death"),
					Sound.Source.MASTER, 1f, 1f), Sound.Emitter.self());
		});

		checkForWinCondition();
	}

	public void playerEliminated(Player player) {
		player.setTag(ELIMINATED_TAG, true);
		String teamName = player.getTag(Tag.String("team"));

		Logger.info("Player {} from team {} has been eliminated",
				player.getUsername(), teamName != null ? teamName : "N/A");

		checkForWinCondition();
	}

	public void checkForWinCondition() {
		if (gameStatus != GameStatus.IN_PROGRESS) return;

		List<MapsConfig.MapEntry.MapConfiguration.MapTeam> activeGameTeams = getActiveGameTeams();
		List<MapsConfig.MapEntry.MapConfiguration.MapTeam> viableTeams = activeGameTeams.stream()
				.filter(this::isTeamViable)
				.toList();

		if (viableTeams.size() <= 1) {
			MapsConfig.MapEntry.MapConfiguration.MapTeam winningTeam =
					viableTeams.isEmpty() ? null : viableTeams.getFirst();
			endGame(winningTeam);
		}
	}

	private String createGameId() {
		return UUID.randomUUID().toString();
	}

	private boolean hasCapacityForPlayer() {
		int teamSize = bedwarsGameType.getTeamSize();
		if (teamSize <= 0) teamSize = 1;
		int maxPlayers = mapEntry.getConfiguration().getTeams().size() * teamSize;
		return players.size() < maxPlayers;
	}

	private boolean hasMinimumPlayersForStart() {
		int teamSize = bedwarsGameType.getTeamSize();
		if (teamSize <= 0) teamSize = 1;

		int minPlayersRequired = teamSize * Math.min(2, mapEntry.getConfiguration().getTeams().size());
		return players.size() >= minPlayersRequired &&
				mapEntry.getConfiguration().getTeams().size() >= 2;
	}

	private void setupPlayerForWaiting(BedWarsPlayer player) {
		MapsConfig.Position waiting = mapEntry.getConfiguration().getLocations().getWaiting();

		if (player.getInstance() == null || player.getInstance().getUuid() != instanceContainer.getUuid()) {
			player.setInstance(instanceContainer, new Pos(waiting.x(), waiting.y(), waiting.z()));
		}

		player.setFlying(false);
		player.setGameMode(GameMode.ADVENTURE);
		player.getInventory().setItemStack(8,
				TypeBedWarsGameLoader.getItemHandler().getItem("leave_game").getItemStack());

		player.setTag(Tag.String("gameId"), gameId);
		player.sendMessage("You have joined the game on map: " + mapEntry.getId());
	}

	private void cleanupPlayerTags(Player player) {
		player.removeTag(Tag.String("gameId"));
		player.removeTag(Tag.String("team"));
		player.removeTag(Tag.String("teamColor"));
		player.removeTag(Tag.String("javaColor"));
	}

	private void updatePlayerCount() {
		int teamSize = bedwarsGameType.getTeamSize();
		if (teamSize <= 0) teamSize = 1;
		int maxPlayers = mapEntry.getConfiguration().getTeams().size() * teamSize;
		Logger.debug("Game {} player count: {}/{}", gameId, players.size(), maxPlayers);
	}

	private List<MapsConfig.MapEntry.MapConfiguration.MapTeam> getActiveGameTeams() {
		return mapEntry.getConfiguration().getTeams().stream()
				.filter(team -> players.stream()
						.anyMatch(p -> team.getName().equals(p.getTag(Tag.String("team")))))
				.collect(Collectors.toList());
	}

	private boolean isTeamViable(MapsConfig.MapEntry.MapConfiguration.MapTeam team) {
		boolean bedAlive = teamManager.isBedAlive(team.getName());
		int aliveMembers = teamManager.countActivePlayersOnTeam(team);
		return bedAlive || aliveMembers > 0;
	}

	private void endGame(MapsConfig.MapEntry.MapConfiguration.MapTeam winningTeam) {
		if (gameStatus == GameStatus.ENDING) return;

		gameStatus = GameStatus.ENDING;
		Logger.info("Game {} has ended. Winner: {}", gameId,
				winningTeam != null ? winningTeam.getName() : "None (Draw)");
		generatorManager.stopAllGenerators();
		eventManager.stop();

		Component titleMessage;
		Component subtitleMessage;

		if (winningTeam != null) {
			titleMessage = MiniMessage.miniMessage().deserialize(String.format(
					"<%s>Team %s has won!</%s>",
					winningTeam.getColor().toLowerCase(), winningTeam.getName(), winningTeam.getColor().toLowerCase()));
			subtitleMessage = Component.text("Congratulations!");
		} else {
			titleMessage = Component.text("Game Over!", NamedTextColor.RED);
			subtitleMessage = Component.text("It's a draw!");
		}

		// Show the end game message to all players
		players.forEach(player -> {
			player.sendTitlePart(TitlePart.TITLE, titleMessage);
			player.sendTitlePart(TitlePart.SUBTITLE, subtitleMessage);
			player.playSound(Sound.sound(Key.key("minecraft:ui.toast.challenge_complete"),
					Sound.Source.MASTER, 1f, 1f), Sound.Emitter.self());

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
