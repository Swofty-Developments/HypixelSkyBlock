package net.swofty.type.bedwarsgame.game;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import net.swofty.commons.ChatUtility;
import net.swofty.commons.ServerType;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.MapTeam;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.BedWarsGameScoreboard;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.stats.BedWarsStatsRecorder;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.user.ExperienceCause;
import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

import java.util.*;

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

	private final Map<String, Map<Integer, ItemStack>> chests = new HashMap<>();
	private final Map<Player, Map<Integer, ItemStack>> enderchests = new HashMap<>();

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
			p.sendMessage("§k" + randomLetters + " §ehas joined (§b" + players.size() + "§e/§b" + maxPlayers + "§e)");
		}

		if (hasMinimumPlayersForStart() && !countdown.isActive()) {
			countdown.startCountdown();
		}
	}

	public void leave(HypixelPlayer player) {
		String teamName = player.getTag(Tag.String("team"));
		players.remove(player);

		player.removeTag(Tag.String("gameId"));
		player.removeTag(Tag.String("team"));
		player.sendTo(ServerType.BEDWARS_LOBBY);

		countdown.checkCountdownConditions();

		if (gameStatus == GameStatus.IN_PROGRESS && teamName != null) {
			checkForWinCondition();
		}
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

	public void playerEliminated(Player player) {
		player.setTag(ELIMINATED_TAG, true);
		String teamName = player.getTag(Tag.String("team"));
		Logger.info("Player {} from team {} eliminated", player.getUsername(), teamName != null ? teamName : "N/A");
		checkForWinCondition();
	}

	public void checkForWinCondition() {
		if (gameStatus != GameStatus.IN_PROGRESS) return;

		List<TeamKey> viableTeams = mapEntry.getConfiguration().getTeams().keySet().stream()
				.filter(teamKey -> hasPlayersOnTeam(teamKey) && isTeamViable(teamKey))
				.toList();

		if (viableTeams.size() <= 1) {
			TeamKey winningTeam = viableTeams.isEmpty() ? null : viableTeams.getFirst();
			endGame(winningTeam);
		}
	}

	private boolean hasPlayersOnTeam(TeamKey teamKey) {
		return players.stream().anyMatch(p -> teamKey.getName().equals(p.getTag(Tag.String("team"))));
	}

	private boolean isTeamViable(TeamKey teamKey) {
		return teamManager.isBedAlive(teamKey) || teamManager.countActivePlayersOnTeam(teamKey) > 0;
	}

	private boolean hasCapacityForPlayer() {
		return players.size() < getMaxPlayers();
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
			titleMessage = winningTeam.chatColor() + "Team " + winningTeam.getName() + "has won!";
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
