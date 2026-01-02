package net.swofty.type.murdermysterygame.game;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServerType;
import net.swofty.commons.murdermystery.MurderMysteryGameType;
import net.swofty.commons.murdermystery.MurderMysteryLeaderboardMode;
import net.swofty.commons.murdermystery.map.MurderMysteryMapsConfig;
import net.swofty.type.generic.data.datapoints.DatapointMurderMysteryModeStats;
import net.swofty.type.generic.data.handlers.MurderMysteryDataHandler;
import net.swofty.type.generic.experience.PlayerExperienceHandler;
import net.kyori.adventure.text.format.TextDecoration;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.murdermysterygame.gold.GoldManager;
import net.swofty.type.murdermysterygame.role.GameRole;
import net.swofty.type.murdermysterygame.role.RoleManager;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import net.swofty.type.murdermysterygame.weapon.WeaponManager;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.item.ItemEntityMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.achievement.PlayerAchievementHandler;

@Getter
public class Game {
    public static final Tag<Boolean> ELIMINATED_TAG = Tag.Boolean("eliminated");

    private final InstanceContainer instanceContainer;
    private final MurderMysteryGameType gameType;
    private final String gameId = UUID.randomUUID().toString();
    private final MurderMysteryMapsConfig.MapEntry mapEntry;

    private final List<MurderMysteryPlayer> players = new ArrayList<>();
    private final List<UUID> disconnectedPlayerUuids = new ArrayList<>();
    private final RoleManager roleManager;
    private final GoldManager goldManager;
    private final WeaponManager weaponManager;
    private final GameCountdown countdown;

    @Setter
    private GameStatus gameStatus;

    private boolean murdererReceivedSword = false;
    private long gameStartTime = 0;
    private long murdererSwordTime = 0;
    private Entity droppedDetectiveBow = null;
    private boolean detectiveBowPickedUp = false;
    private MurderMysteryPlayer murdererKiller = null;
    private final Map<UUID, Integer> murdererKillsThisGame = new HashMap<>();
    private int deathCount = 0;
    private long lastMurdererKillTime = 0;
    private final Map<UUID, Game.KillType> murdererKillMethods = new HashMap<>();

    public Game(MurderMysteryMapsConfig.MapEntry mapEntry,
                InstanceContainer instanceContainer,
                MurderMysteryGameType gameType) {
        this.mapEntry = mapEntry;
        this.instanceContainer = instanceContainer;
        this.gameType = gameType;

        this.roleManager = new RoleManager(this);
        this.goldManager = new GoldManager(this);
        this.weaponManager = new WeaponManager(this);
        this.countdown = new GameCountdown(this);

        this.gameStatus = GameStatus.WAITING;
    }

    public void join(MurderMysteryPlayer player) {
        if (gameStatus != GameStatus.WAITING) {
            player.sendMessage(Component.text("Game already in progress!", NamedTextColor.RED));
            player.sendTo(ServerType.MURDER_MYSTERY_LOBBY);
            return;
        }

        if (players.size() >= gameType.getMaxPlayers()) {
            player.sendMessage(Component.text("Game is full!", NamedTextColor.RED));
            player.sendTo(ServerType.MURDER_MYSTERY_LOBBY);
            return;
        }

        setupPlayerForWaiting(player);
        players.add(player);
        player.setTag(Tag.String("gameId"), gameId);

        // Format: [name] has joined (count/max)!
        broadcastMessage(Component.empty()
                .append(Component.text(player.getFullDisplayName()))
                .append(Component.text(" has joined ", NamedTextColor.YELLOW))
                .append(Component.text("(", NamedTextColor.YELLOW))
                .append(Component.text(players.size(), NamedTextColor.AQUA))
                .append(Component.text("/", NamedTextColor.YELLOW))
                .append(Component.text(gameType.getMaxPlayers(), NamedTextColor.AQUA))
                .append(Component.text(")!", NamedTextColor.YELLOW)));

        if (hasMinimumPlayers() && !countdown.isActive()) {
            countdown.startCountdown();
        }
    }

    public void leave(MurderMysteryPlayer player) {
        players.remove(player);
        player.removeTag(Tag.String("gameId"));
        player.sendTo(ServerType.MURDER_MYSTERY_LOBBY);

        countdown.checkCountdownConditions();

        if (gameStatus == GameStatus.IN_PROGRESS) {
            checkWinConditions();
        }
    }

    public void disconnect(MurderMysteryPlayer player) {
        disconnectedPlayerUuids.add(player.getUuid());
        players.remove(player);

        if (gameStatus == GameStatus.IN_PROGRESS) {
            checkWinConditions();
        }
    }

    public boolean hasDisconnectedPlayer(UUID uuid) {
        return disconnectedPlayerUuids.contains(uuid);
    }

    public void rejoin(MurderMysteryPlayer player) {
        disconnectedPlayerUuids.remove(player.getUuid());
        players.add(player);
        player.setTag(Tag.String("gameId"), gameId);

        // Restore player state based on game status
        if (gameStatus == GameStatus.IN_PROGRESS) {
            GameRole role = roleManager.getRole(player.getUuid());
            if (role != null) {
                setupPlayerForGame(player, role);
                addPlayerToHiddenNametagsTeam(player);
                player.setInstance(instanceContainer, getWaitingPosition());
                player.sendMessage(Component.text("You have rejoined the game!", NamedTextColor.GREEN));
            } else {
                // Player was eliminated before disconnect - set up as invisible spectator
                setupPlayerForSpectator(player);
                player.setInstance(instanceContainer, getWaitingPosition());
                player.sendMessage(Component.text("You have rejoined as a spectator.", NamedTextColor.GRAY));
            }
        } else if (gameStatus == GameStatus.WAITING) {
            setupPlayerForWaiting(player);
            // Format: [name] has rejoined (count/max)!
            broadcastMessage(Component.empty()
                    .append(Component.text(player.getFullDisplayName()))
                    .append(Component.text(" has rejoined ", NamedTextColor.YELLOW))
                    .append(Component.text("(", NamedTextColor.YELLOW))
                    .append(Component.text(players.size(), NamedTextColor.AQUA))
                    .append(Component.text("/", NamedTextColor.YELLOW))
                    .append(Component.text(gameType.getMaxPlayers(), NamedTextColor.AQUA))
                    .append(Component.text(")!", NamedTextColor.YELLOW)));
        } else {
            // Game is ending, just set up as invisible spectator
            setupPlayerForSpectator(player);
            player.setInstance(instanceContainer, getWaitingPosition());
        }
    }

    public void startGame() {
        gameStatus = GameStatus.IN_PROGRESS;
        gameStartTime = System.currentTimeMillis();
        murdererReceivedSword = false;

        roleManager.assignRoles();
        setupHiddenNametags();

        for (MurderMysteryPlayer player : players) {
            GameRole role = roleManager.getRole(player.getUuid());
            setupPlayerForGame(player, role);
            announceRole(player, role);
        }

        // Teaming warning
        broadcastMessage(Component.text("Teaming with the Murderer is not allowed!", NamedTextColor.RED, net.kyori.adventure.text.format.TextDecoration.BOLD));

        goldManager.startSpawning();
        startKillZoneCheck();
        startMurdererSwordCountdown();
        startSurvivalRewards();

        // Game timer (5 minutes)
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (gameStatus == GameStatus.IN_PROGRESS) {
                endGame(WinCondition.TIME_EXPIRED);
            }
        }).delay(TaskSchedule.minutes(5)).schedule();
    }

    private void startMurdererSwordCountdown() {
        // Count down from 30 seconds, announce last 5 seconds
        final int[] secondsRemaining = {30};

        var task = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (gameStatus != GameStatus.IN_PROGRESS) return;

            secondsRemaining[0]--;

            if (secondsRemaining[0] <= 5 && secondsRemaining[0] > 0) {
                String word = secondsRemaining[0] == 1 ? "second" : "seconds";
                broadcastMessage(Component.empty()
                        .append(Component.text("The Murderer receives their sword in ", NamedTextColor.YELLOW))
                        .append(Component.text(secondsRemaining[0], NamedTextColor.RED))
                        .append(Component.text(" " + word + "!", NamedTextColor.YELLOW)));
            }
        }).delay(TaskSchedule.seconds(1)).repeat(TaskSchedule.seconds(1)).schedule();

        // Schedule the sword delivery at exactly 30 seconds
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (gameStatus != GameStatus.IN_PROGRESS) return;
            task.cancel();
            murdererReceivedSword = true;
            murdererSwordTime = System.currentTimeMillis();

            // Give the knife to all murderers
            for (MurderMysteryPlayer murderer : roleManager.getPlayersWithRole(GameRole.MURDERER)) {
                if (!murderer.isEliminated()) {
                    weaponManager.giveMurdererKnife(murderer);
                }
            }

            broadcastMessage(Component.text("The Murderer has received their sword!", NamedTextColor.YELLOW));
        }).delay(TaskSchedule.seconds(30)).schedule();
    }

    private void startSurvivalRewards() {
        // Every 30 seconds, give +40 tokens to surviving players
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (gameStatus != GameStatus.IN_PROGRESS) return;

            MurderMysteryLeaderboardMode leaderboardMode = MurderMysteryLeaderboardMode.fromGameType(gameType);

            for (MurderMysteryPlayer player : players) {
                if (!player.isEliminated()) {
                    // Add tokens to session tracking
                    player.addTokens(40);

                    // Add tokens to persistent stats
                    MurderMysteryDataHandler handler = MurderMysteryDataHandler.getUser(player);
                    if (handler != null) {
                        DatapointMurderMysteryModeStats statsDP = handler.get(
                                MurderMysteryDataHandler.Data.MODE_STATS,
                                DatapointMurderMysteryModeStats.class);
                        statsDP.getValue().recordTokens(leaderboardMode, 40);
                    }

                    player.sendMessage(Component.text("+40 Tokens! Survived 30 seconds", NamedTextColor.DARK_GREEN));
                }
            }
        }).delay(TaskSchedule.seconds(30)).repeat(TaskSchedule.seconds(30)).schedule();
    }

    public boolean hasMurdererReceivedSword() {
        return murdererReceivedSword;
    }

    private void startKillZoneCheck() {
        var killRegions = mapEntry.getConfiguration() != null ? mapEntry.getConfiguration().getKillRegions() : null;
        if (killRegions == null || killRegions.isEmpty()) return;

        // Check if this is the Aquarium map for JAWS! achievement
        boolean isAquariumMap = mapEntry.getName() != null &&
                mapEntry.getName().toLowerCase().contains("aquarium");

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (gameStatus != GameStatus.IN_PROGRESS) return;

            List<MurderMysteryPlayer> playersToCheck = new ArrayList<>(players);
            for (MurderMysteryPlayer player : playersToCheck) {
                if (player.isEliminated()) continue;

                double x = player.getPosition().x();
                double y = player.getPosition().y();
                double z = player.getPosition().z();

                for (MurderMysteryMapsConfig.KillRegion region : killRegions) {
                    if (region.contains(x, y, z)) {
                        // JAWS! achievement - get eaten by shark on Aquarium
                        if (isAquariumMap) {
                            PlayerAchievementHandler achHandler = new PlayerAchievementHandler(player);
                            achHandler.addProgress("murdermystery.jaws", 1);
                        }
                        onEnvironmentalDeath(player);
                        break;
                    }
                }
            }
        }).repeat(TaskSchedule.tick(5)).schedule();
    }

    public void onEnvironmentalDeath(MurderMysteryPlayer victim) {
        GameRole victimRole = roleManager.getRole(victim.getUuid());

        victim.setEliminated(true);
        victim.setTag(ELIMINATED_TAG, true);
        setupPlayerForSpectator(victim);

        // Send death message
        sendDeathMessage(victim, "You fell out of the world.");

        // Handle detective death - drop bow
        if (victimRole == GameRole.DETECTIVE) {
            dropDetectiveBow(victim);
        }

        checkWinConditions();
    }

    private void setupPlayerForWaiting(MurderMysteryPlayer player) {
        Pos waitingPos = getWaitingPosition();

        if (player.getInstance() == null || !player.getInstance().getUuid().equals(instanceContainer.getUuid())) {
            player.setInstance(instanceContainer, waitingPos);
        } else {
            player.teleport(waitingPos);
        }

        player.getInventory().clear();
        player.getInventory().setItemStack(8,
                TypeMurderMysteryGameLoader.getItemHandler().getItem("leave_game").getItemStack());
        player.setFlying(false);
        player.setGameMode(GameMode.ADVENTURE);
        player.setEliminated(false);
        player.resetGold();
    }

    private Pos getWaitingPosition() {
        var config = mapEntry.getConfiguration();
        if (config != null && config.getLocations() != null && config.getLocations().getWaiting() != null) {
            var waiting = config.getLocations().getWaiting();
            return new Pos(waiting.x(), waiting.y(), waiting.z(), waiting.yaw(), waiting.pitch());
        }
        // Fallback position
        return new Pos(0, 66, 0);
    }

    private void setupPlayerForGame(MurderMysteryPlayer player, GameRole role) {
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);

        // Teleport to a random spawn point
        Pos spawnPos = getRandomSpawnPosition();
        player.teleport(spawnPos);

        // Murderer gets knife after 30 second countdown (handled in startMurdererSwordCountdown)
        if (role == GameRole.DETECTIVE) {
            weaponManager.giveDetectiveBow(player);
        }
        // Innocents start with no weapons - must collect gold
    }

    private void setupPlayerForSpectator(MurderMysteryPlayer player) {
        player.getInventory().clear();
        player.getInventory().setItemStack(0,
                TypeMurderMysteryGameLoader.getItemHandler().getItem("spectator_compass").getItemStack());
        player.getInventory().setItemStack(7,
                TypeMurderMysteryGameLoader.getItemHandler().getItem("play_again").getItemStack());
        player.getInventory().setItemStack(8,
                TypeMurderMysteryGameLoader.getItemHandler().getItem("leave_game").getItemStack());

        // Make player invisible to all other players instead of spectator mode
        for (MurderMysteryPlayer otherPlayer : players) {
            if (!otherPlayer.equals(player) && !otherPlayer.isEliminated()) {
                player.removeViewer(otherPlayer);
                player.updateOldViewer(otherPlayer);
            }
        }

        // Allow flying
        player.setAllowFlying(true);
        player.setFlying(true);
    }

    private void setupHiddenNametags() {
        // Create a team with hidden nametags for all players
        List<String> playerNames = players.stream()
                .map(MurderMysteryPlayer::getUsername)
                .toList();

        TeamsPacket createTeamPacket = new TeamsPacket(
                "mm_hidden",
                new TeamsPacket.CreateTeamAction(
                        Component.empty(),
                        (byte) 0x00,
                        TeamsPacket.NameTagVisibility.NEVER,
                        TeamsPacket.CollisionRule.ALWAYS,
                        NamedTextColor.WHITE,
                        Component.empty(),
                        Component.empty(),
                        playerNames
                )
        );

        // Send to all players
        for (MurderMysteryPlayer player : players) {
            player.sendPacket(createTeamPacket);
        }
    }

    private void addPlayerToHiddenNametagsTeam(MurderMysteryPlayer newPlayer) {
        // Send existing team info to the new player
        List<String> allPlayerNames = players.stream()
                .map(MurderMysteryPlayer::getUsername)
                .toList();

        TeamsPacket createTeamPacket = new TeamsPacket(
                "mm_hidden",
                new TeamsPacket.CreateTeamAction(
                        Component.empty(),
                        (byte) 0x00,
                        TeamsPacket.NameTagVisibility.NEVER,
                        TeamsPacket.CollisionRule.ALWAYS,
                        NamedTextColor.WHITE,
                        Component.empty(),
                        Component.empty(),
                        allPlayerNames
                )
        );
        newPlayer.sendPacket(createTeamPacket);

        // Tell existing players to add the new player to the team
        TeamsPacket addPlayerPacket = new TeamsPacket(
                "mm_hidden",
                new TeamsPacket.AddEntitiesToTeamAction(List.of(newPlayer.getUsername()))
        );

        for (MurderMysteryPlayer player : players) {
            if (!player.equals(newPlayer)) {
                player.sendPacket(addPlayerPacket);
            }
        }
    }

    private Pos getRandomSpawnPosition() {
        var config = mapEntry.getConfiguration();
        if (config != null && config.getPlayerSpawns() != null && !config.getPlayerSpawns().isEmpty()) {
            var spawns = config.getPlayerSpawns();
            var spawn = spawns.get(ThreadLocalRandom.current().nextInt(spawns.size()));
            return new Pos(spawn.x(), spawn.y(), spawn.z());
        }
        // Fallback to waiting position if no spawns configured
        return getWaitingPosition();
    }

    private void announceRole(MurderMysteryPlayer player, GameRole role) {
        Title title = Title.title(
                Component.text("You are the " + role.getDisplayName(), role.getColor()),
                Component.text(role.getDescription(), NamedTextColor.GRAY),
                Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500))
        );
        player.showTitle(title);
    }

    public void onPlayerKill(MurderMysteryPlayer killer, MurderMysteryPlayer victim) {
        onPlayerKill(killer, victim, KillType.KNIFE);
    }

    public void onPlayerKill(MurderMysteryPlayer killer, MurderMysteryPlayer victim, KillType killType) {
        GameRole killerRole = roleManager.getRole(killer.getUuid());
        GameRole victimRole = roleManager.getRole(victim.getUuid());

        // Track kills for the killer (in-game counter)
        killer.addKill();

        // Record kill to persistent stats
        recordKillStats(killer, killType);

        // Track death count for achievements
        deathCount++;

        // Record kill timestamp for rapid kill achievements
        killer.recordKillTimestamp();

        // === QUEST TRIGGERS ===
        // Weekly: Professional - Kill any player
        killer.getQuestHandler().addProgressByTrigger("murdermystery.kills", 1);

        // Track murderer kills for Murder Spree challenge
        if (killerRole == GameRole.MURDERER) {
            int currentKills = murdererKillsThisGame.getOrDefault(killer.getUuid(), 0) + 1;
            murdererKillsThisGame.put(killer.getUuid(), currentKills);
            lastMurdererKillTime = System.currentTimeMillis();

            // === ACHIEVEMENT TRIGGERS FOR MURDERER ===
            PlayerAchievementHandler killerAchHandler = new PlayerAchievementHandler(killer);

            // Tiered: Stabber - kills as murderer
            killerAchHandler.addProgress("murdermystery.stabber", 1);

            // Per-game: Where's my EMP? - 15 kills as murderer
            killerAchHandler.addProgress("murdermystery.wheres_my_emp", 1);

            // Slice N Dice - 5 kills within 5 seconds as murderer
            if (killer.getKillsInLast5Seconds() >= 5) {
                killerAchHandler.addProgress("murdermystery.slice_n_dice", 1);
            }

            // Not Today - Kill Detective within 30s of receiving sword
            if (victimRole == GameRole.DETECTIVE && murdererSwordTime > 0) {
                if (System.currentTimeMillis() - murdererSwordTime <= 30000) {
                    killerAchHandler.addProgress("murdermystery.not_today", 1);
                }
            }

            // Reset bow kill streak on knife kill
            if (killType == KillType.KNIFE) {
                killer.resetBowKillStreak();
            }
        }

        // Track who killed the murderer (for hero detection)
        if (victimRole == GameRole.MURDERER) {
            murdererKiller = killer;

            // Only in Classic/Double Up mode (not Assassins)
            if (gameType != MurderMysteryGameType.ASSASSINS) {
                // Daily: Power Play - killed the murderer
                killer.getQuestHandler().addProgressByTrigger("murdermystery.power_play", 1);

                // Challenge: Hero - killed murderer as any role
                killer.getQuestHandler().addProgressByTrigger("murdermystery.hero", 1);

                // Challenge: Sherlock - Detective killed Murderer under 2 minutes
                if (killerRole == GameRole.DETECTIVE) {
                    long elapsedMs = System.currentTimeMillis() - gameStartTime;
                    if (elapsedMs < 120_000) { // Under 2 minutes
                        killer.getQuestHandler().addProgressByTrigger("murdermystery.sherlock", 1);
                    }
                }

                // === ACHIEVEMENT TRIGGERS FOR KILLING MURDERER ===
                PlayerAchievementHandler killerAchHandler = new PlayerAchievementHandler(killer);

                // Clean Round - killed murderer before anyone died
                if (deathCount == 1) { // This is the first death (the murderer)
                    killerAchHandler.addProgress("murdermystery.clean_round", 1);
                }

                // Close Enough - killed murderer as last person alive
                int aliveNonMurderers = countAliveNonMurderers();
                if (aliveNonMurderers == 1) {
                    killerAchHandler.addProgress("murdermystery.close_enough", 1);
                }

                // Caught In The Act - killed murderer within 3 seconds of their kill
                if (lastMurdererKillTime > 0 && System.currentTimeMillis() - lastMurdererKillTime <= 3000) {
                    killerAchHandler.addProgress("murdermystery.caught_in_the_act", 1);
                }

                // Track for Double Duty (Double Up mode)
                if (gameType == MurderMysteryGameType.DOUBLE_UP) {
                    murdererKillMethods.put(killer.getUuid(), killType);
                    // Check if player killed both murderers with different methods
                    long killsOfMurderersByThisPlayer = murdererKillMethods.entrySet().stream()
                            .filter(e -> e.getKey().equals(killer.getUuid()))
                            .count();
                    if (killsOfMurderersByThisPlayer >= 2) {
                        // Check if different kill types were used
                        Set<KillType> usedTypes = new HashSet<>();
                        for (var entry : murdererKillMethods.entrySet()) {
                            if (entry.getKey().equals(killer.getUuid())) {
                                usedTypes.add(entry.getValue());
                            }
                        }
                        if (usedTypes.size() >= 2) {
                            killerAchHandler.addProgress("murdermystery.double_duty", 1);
                        }
                    }
                }
            }
        }

        victim.setEliminated(true);
        victim.setTag(ELIMINATED_TAG, true);
        setupPlayerForSpectator(victim);

        if (gameType == MurderMysteryGameType.ASSASSINS) {
            handleAssassinKill(killer, victim);
        } else {
            handleClassicKill(killer, victim, killerRole, victimRole);
        }

        // Handle detective death - drop bow
        if (victimRole == GameRole.DETECTIVE) {
            dropDetectiveBow(victim);
        }

        checkWinConditions();
    }

    private void recordKillStats(MurderMysteryPlayer killer, KillType killType) {
        MurderMysteryDataHandler handler = MurderMysteryDataHandler.getUser(killer);
        if (handler == null) return;

        MurderMysteryLeaderboardMode leaderboardMode = MurderMysteryLeaderboardMode.fromGameType(gameType);
        DatapointMurderMysteryModeStats statsDP = handler.get(
                MurderMysteryDataHandler.Data.MODE_STATS,
                DatapointMurderMysteryModeStats.class);
        var stats = statsDP.getValue();

        // Track kills as murderer
        GameRole killerRole = roleManager.getRole(killer.getUuid());
        if (killerRole == GameRole.MURDERER) {
            stats.recordKillAsMurderer(leaderboardMode);
        }

        switch (killType) {
            case BOW -> stats.recordBowKill(leaderboardMode);
            case KNIFE -> stats.recordKnifeKill(leaderboardMode);
            case THROWN_KNIFE -> stats.recordThrownKnifeKill(leaderboardMode);
            case TRAP -> stats.recordTrapKill(leaderboardMode);
        }
    }

    public enum KillType {
        BOW, KNIFE, THROWN_KNIFE, TRAP
    }

    private void dropDetectiveBow(MurderMysteryPlayer detective) {
        // Drop the bow at the detective's location
        Pos deathPos = detective.getPosition();

        Entity bowEntity = new Entity(EntityType.ITEM);
        ItemEntityMeta meta = (ItemEntityMeta) bowEntity.getEntityMeta();
        meta.setItem(ItemStack.of(Material.BOW));
        bowEntity.setInstance(instanceContainer, deathPos);
        droppedDetectiveBow = bowEntity;
        detectiveBowPickedUp = false;

        // Broadcast bow drop message
        broadcastMessage(Component.empty()
                .append(Component.text("The Bow has been dropped! ", NamedTextColor.GOLD))
                .append(Component.text("Find the Bow for a chance to kill the Murderer.", NamedTextColor.YELLOW)));
    }

    public boolean isDroppedDetectiveBow(Entity entity) {
        return droppedDetectiveBow != null && droppedDetectiveBow.equals(entity);
    }

    public void onDetectiveBowPickedUp(MurderMysteryPlayer player) {
        if (droppedDetectiveBow != null) {
            droppedDetectiveBow.remove();
            droppedDetectiveBow = null;
            detectiveBowPickedUp = true;

            // Give the player a bow and arrow
            weaponManager.giveInnocentBow(player);

            // Broadcast pickup message
            broadcastMessage(Component.text("A player has picked up the Bow!", NamedTextColor.YELLOW));
        }
    }

    public void sendDeathMessage(MurderMysteryPlayer player, String reason) {
        player.sendMessage(Component.empty()
                .append(Component.text("YOU DIED! ", NamedTextColor.RED))
                .append(Component.text(reason, NamedTextColor.YELLOW)));
    }

    private void handleClassicKill(MurderMysteryPlayer killer, MurderMysteryPlayer victim,
                                   GameRole killerRole, GameRole victimRole) {
        if (killerRole == GameRole.MURDERER) {
            // Murderer killed someone - no penalty
            broadcastMessage(Component.text(victim.getUsername() + " was killed!", NamedTextColor.RED));
            sendDeathMessage(victim, "You were killed by the Murderer.");
        } else {
            // Innocent or Detective killed someone
            if (victimRole == GameRole.MURDERER) {
                // Correctly identified murderer!
                broadcastMessage(Component.text(killer.getUsername() + " killed the murderer!", NamedTextColor.GREEN));
                sendDeathMessage(victim, "You were identified and eliminated.");
            } else {
                // Killed an innocent - shooter also dies
                sendDeathMessage(victim, "You were shot by " + killer.getUsername() + ".");

                killer.setEliminated(true);
                killer.setTag(ELIMINATED_TAG, true);
                setupPlayerForSpectator(killer);
                broadcastMessage(Component.text(killer.getUsername() + " killed an innocent and was struck by lightning!", NamedTextColor.RED));
                sendDeathMessage(killer, "You killed an innocent player.");

                // If the killer was the detective, also drop their bow
                GameRole killerRole2 = roleManager.getRole(killer.getUuid());
                if (killerRole2 == GameRole.DETECTIVE) {
                    dropDetectiveBow(killer);
                }
            }
        }
    }

    private void handleAssassinKill(MurderMysteryPlayer killer, MurderMysteryPlayer victim) {
        UUID targetUuid = roleManager.getAssassinTarget(killer.getUuid());
        if (victim.getUuid().equals(targetUuid)) {
            // Correct target
            killer.sendMessage(Component.text("Target eliminated! New target assigned.", NamedTextColor.GREEN));
            // Inherit the victim's target
            UUID newTarget = roleManager.getAssassinTarget(victim.getUuid());
            roleManager.reassignTarget(killer.getUuid(), newTarget);

            // Daily: Hitman - killed assigned target in Assassins
            killer.getQuestHandler().addProgressByTrigger("murdermystery.assassin_target_kills", 1);

            // === ASSASSINS MODE ACHIEVEMENTS ===
            PlayerAchievementHandler achHandler = new PlayerAchievementHandler(killer);

            // Tiered: Hitman - kill players in Assassins
            achHandler.addProgress("murdermystery.hitman", 1);

            // Sixth Sense - kill 2 targets within 5 seconds in Assassins
            if (killer.getKillsInLast5Seconds() >= 2) {
                achHandler.addProgress("murdermystery.sixth_sense", 1);
            }
        } else {
            // Wrong target - killer dies
            killer.setEliminated(true);
            killer.setTag(ELIMINATED_TAG, true);
            setupPlayerForSpectator(killer);
            broadcastMessage(Component.text(killer.getUsername() + " attacked the wrong person!", NamedTextColor.RED));
        }
    }

    public void checkWinConditions() {
        if (gameStatus != GameStatus.IN_PROGRESS) return;

        if (gameType == MurderMysteryGameType.ASSASSINS) {
            int aliveCount = countAlivePlayers();
            if (aliveCount <= 1) {
                endGame(WinCondition.LAST_STANDING);
            }
        } else {
            int aliveMurderers = roleManager.countAliveWithRole(GameRole.MURDERER);
            int aliveInnocents = roleManager.countAliveWithRole(GameRole.INNOCENT)
                    + roleManager.countAliveWithRole(GameRole.DETECTIVE);

            if (aliveMurderers == 0) {
                endGame(WinCondition.INNOCENTS_WIN);
            } else if (aliveInnocents == 0) {
                endGame(WinCondition.MURDERER_WINS);
            }
        }
    }

    private void endGame(WinCondition condition) {
        gameStatus = GameStatus.ENDING;
        goldManager.stopSpawning();

        // Record stats for all players
        recordGameStats(condition);

        // === CHALLENGE COMPLETIONS ===
        // Challenge: Murder Spree - Murderer with 5+ kills
        for (MurderMysteryPlayer player : players) {
            GameRole role = roleManager.getRole(player.getUuid());
            if (role == GameRole.MURDERER) {
                int kills = murdererKillsThisGame.getOrDefault(player.getUuid(), 0);
                if (kills >= 5) {
                    player.getQuestHandler().addProgressByTrigger("murdermystery.murder_spree", 1);
                }
            }
        }

        // Challenge: Serial Killer - Most kills in Assassins (no ties)
        if (gameType == MurderMysteryGameType.ASSASSINS) {
            MurderMysteryPlayer topKiller = null;
            int maxKills = 0;
            boolean tied = false;

            for (MurderMysteryPlayer player : players) {
                int kills = player.getKillsThisGame();
                if (kills > maxKills) {
                    maxKills = kills;
                    topKiller = player;
                    tied = false;
                } else if (kills == maxKills && kills > 0) {
                    tied = true;
                }
            }

            if (topKiller != null && !tied && maxKills > 0) {
                topKiller.getQuestHandler().addProgressByTrigger("murdermystery.serial_killer", 1);
            }
        }

        // === ACHIEVEMENT TRIGGERS FOR GAME END ===
        long elapsedMs = System.currentTimeMillis() - gameStartTime;
        long remainingMs = (5 * 60 * 1000) - elapsedMs; // 5-minute game duration
        boolean innocentsWon = (condition == WinCondition.INNOCENTS_WIN || condition == WinCondition.TIME_EXPIRED);
        boolean murdererWon = (condition == WinCondition.MURDERER_WINS);

        for (MurderMysteryPlayer player : players) {
            GameRole role = roleManager.getRole(player.getUuid());
            if (role == null) continue;

            PlayerAchievementHandler achHandler = new PlayerAchievementHandler(player);

            // === TIMING-BASED ACHIEVEMENTS (Murderer wins) ===
            if (murdererWon && role == GameRole.MURDERER) {
                // Tiered: You're All Mine - win games as murderer
                achHandler.addProgress("murdermystery.youre_all_mine", 1);

                // Calculated - win as murderer with 15 seconds or less remaining
                if (remainingMs <= 15000) {
                    achHandler.addProgress("murdermystery.calculated", 1);
                }

                // Uncalculated - win as murderer with 2+ minutes remaining
                if (remainingMs >= 120000) {
                    achHandler.addProgress("murdermystery.uncalculated", 1);
                }
            }

            // === INNOCENT/DETECTIVE WIN ACHIEVEMENTS ===
            if (innocentsWon && (role == GameRole.INNOCENT || role == GameRole.DETECTIVE)) {
                // Tiered: Peace is Mine - win as innocent/detective without dying
                if (!player.isEliminated()) {
                    achHandler.addProgress("murdermystery.peace_is_mine", 1);
                }

                // TIME_EXPIRED specific achievements
                if (condition == WinCondition.TIME_EXPIRED) {
                    // Catch Me If You Can - survive until time runs out as innocent
                    if (role == GameRole.INNOCENT && !player.isEliminated()) {
                        achHandler.addProgress("murdermystery.catch_me_if_you_can", 1);
                    }

                    // No Money, No Problems - survive timeout without collecting gold
                    if (role == GameRole.INNOCENT && !player.isEliminated() && !player.isHasCollectedGoldThisGame()) {
                        achHandler.addProgress("murdermystery.no_money_no_problems", 1);
                    }
                }
            }

            // === HERO ACHIEVEMENTS ===
            if (murdererKiller != null && murdererKiller.getUuid().equals(player.getUuid())) {
                GameRole heroRole = roleManager.getRole(murdererKiller.getUuid());
                // Saving The Day - be the hero of the game (non-detective kills murderer)
                if (heroRole != GameRole.DETECTIVE) {
                    achHandler.addProgress("murdermystery.saving_the_day", 1);
                }

                // Tiered: Countermeasures - hero wins in Classic/Double Up
                if (gameType != MurderMysteryGameType.ASSASSINS) {
                    achHandler.addProgress("murdermystery.countermeasures", 1);
                }
            }

            // === ROLE STREAK ACHIEVEMENT ===
            // I Am Special - be Murderer/Detective two games in a row
            if (role == GameRole.MURDERER || role == GameRole.DETECTIVE) {
                GameRole lastRole = player.getLastGameRole();
                if (lastRole != null && (lastRole == GameRole.MURDERER || lastRole == GameRole.DETECTIVE)) {
                    achHandler.addProgress("murdermystery.i_am_special", 1);
                }
            }
            // Store current role for next game comparison
            player.setLastGameRole(role);
        }

        // Clear per-game tracking
        murdererKillsThisGame.clear();

        String message = switch (condition) {
            case INNOCENTS_WIN -> "The Innocents have won!";
            case MURDERER_WINS -> "The Murderer has won!";
            case TIME_EXPIRED -> "Time's up! Innocents survived!";
            case LAST_STANDING -> getLastStandingPlayer() != null ?
                    getLastStandingPlayer().getUsername() + " is the last one standing!" :
                    "Game Over!";
        };

        NamedTextColor color = switch (condition) {
            case INNOCENTS_WIN, TIME_EXPIRED -> NamedTextColor.GREEN;
            case MURDERER_WINS -> NamedTextColor.RED;
            case LAST_STANDING -> NamedTextColor.GOLD;
        };

        Title title = Title.title(
                Component.text(message, color),
                Component.empty(),
                Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(5), Duration.ofMillis(500))
        );
        players.forEach(p -> p.showTitle(title));

        // Show game results after 2 seconds (so title is visible first)
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            sendGameResults(condition);
        }).delay(TaskSchedule.seconds(2)).schedule();

        // Cleanup after 10 seconds
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            List<MurderMysteryPlayer> playersToRemove = new ArrayList<>(players);
            for (MurderMysteryPlayer player : playersToRemove) {
                leave(player);
            }
            players.clear();
            roleManager.clear();
            murdererKiller = null;
            gameStatus = GameStatus.WAITING;
        }).delay(TaskSchedule.seconds(10)).schedule();
    }

    private void sendGameResults(WinCondition condition) {
        String thickBar = "§a§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";

        for (MurderMysteryPlayer player : players) {
            // First block - Game Results
            player.sendMessage(Component.text(thickBar));
            player.sendMessage(Component.text("                    ")
                    .append(Component.text("MURDER MYSTERY", NamedTextColor.WHITE, TextDecoration.BOLD)));

            // Winner line
            if (gameType == MurderMysteryGameType.ASSASSINS) {
                // Assassins mode - show last standing
                MurderMysteryPlayer winner = getLastStandingPlayer();
                if (winner != null) {
                    player.sendMessage(Component.text("              ")
                            .append(Component.text("Winner: ", NamedTextColor.WHITE, TextDecoration.BOLD))
                            .append(Component.text(winner.getUsername(), NamedTextColor.GOLD)));

                    player.sendMessage(Component.empty());
                    player.sendMessage(Component.text(" §7Last Standing: ")
                            .append(Component.text(winner.getFullDisplayName()))
                            .append(Component.text(" §7(§6" + winner.getKillsThisGame() + "§7 kills)")));
                } else {
                    player.sendMessage(Component.text("              ")
                            .append(Component.text("Winner: ", NamedTextColor.WHITE, TextDecoration.BOLD))
                            .append(Component.text("None", NamedTextColor.GRAY)));
                }
            } else {
                // Classic mode - show winner and roles
                boolean innocentsWon = (condition == WinCondition.INNOCENTS_WIN || condition == WinCondition.TIME_EXPIRED);
                player.sendMessage(Component.text("         ")
                        .append(Component.text("Winner: ", NamedTextColor.WHITE, TextDecoration.BOLD))
                        .append(innocentsWon ?
                                Component.text("INNOCENTS", NamedTextColor.GREEN) :
                                Component.text("MURDERER", NamedTextColor.RED)));

                player.sendMessage(Component.empty());

                // Detective line
                MurderMysteryPlayer detective = roleManager.getPlayersWithRole(GameRole.DETECTIVE).stream().findFirst().orElse(null);
                if (detective != null) {
                    Component detectiveName = detective.isEliminated() ?
                            Component.text(detective.getFullDisplayName()).decorate(TextDecoration.STRIKETHROUGH) :
                            Component.text(detective.getFullDisplayName());
                    player.sendMessage(Component.text(" §7Detective: ").append(detectiveName));
                }

                // Murderer line
                MurderMysteryPlayer murderer = roleManager.getPlayersWithRole(GameRole.MURDERER).stream().findFirst().orElse(null);
                if (murderer != null) {
                    Component murdererName = murderer.isEliminated() ?
                            Component.text(murderer.getFullDisplayName()).decorate(TextDecoration.STRIKETHROUGH) :
                            Component.text(murderer.getFullDisplayName());
                    player.sendMessage(Component.text(" §7Murderer: ")
                            .append(murdererName)
                            .append(Component.text(" §7(§6" + murderer.getKillsThisGame() + "§7 kills)")));
                }

                // Hero line (if non-detective killed murderer)
                if (murdererKiller != null && roleManager.getRole(murdererKiller.getUuid()) != GameRole.DETECTIVE) {
                    player.sendMessage(Component.text(" §7Hero: ")
                            .append(Component.text(murdererKiller.getFullDisplayName())));
                }
            }

            player.sendMessage(Component.text(thickBar));

            // Second block - Reward Summary
            player.sendMessage(Component.text("                 ")
                    .append(Component.text("Reward Summary", NamedTextColor.WHITE, TextDecoration.BOLD)));
            player.sendMessage(Component.text("   §7You earned:"));
            player.sendMessage(Component.text("   §a+" + player.getTokensEarnedThisGame() + " Murder Mystery Tokens"));
            player.sendMessage(Component.text("   §b+267 Hypixel Experience"));

            player.sendMessage(Component.text(thickBar));

            // Actually give the Hypixel Experience
            PlayerExperienceHandler expHandler = new PlayerExperienceHandler(player);
            expHandler.addExperience(267);
        }
    }

    private void recordGameStats(WinCondition condition) {
        MurderMysteryLeaderboardMode leaderboardMode = MurderMysteryLeaderboardMode.fromGameType(gameType);
        long gameDuration = System.currentTimeMillis() - gameStartTime;

        boolean innocentsWon = (condition == WinCondition.INNOCENTS_WIN || condition == WinCondition.TIME_EXPIRED);
        boolean murdererWon = (condition == WinCondition.MURDERER_WINS);

        for (MurderMysteryPlayer player : players) {
            MurderMysteryDataHandler handler = MurderMysteryDataHandler.getUser(player);
            if (handler == null) continue;

            DatapointMurderMysteryModeStats statsDP = handler.get(
                    MurderMysteryDataHandler.Data.MODE_STATS,
                    DatapointMurderMysteryModeStats.class);
            var stats = statsDP.getValue();

            // Record games played for everyone
            stats.recordGamePlayed(leaderboardMode);

            GameRole role = roleManager.getRole(player.getUuid());
            if (role == null) continue;

            // Record wins based on condition and role
            if (gameType == MurderMysteryGameType.ASSASSINS) {
                // Assassins mode - only winner gets the win
                MurderMysteryPlayer winner = getLastStandingPlayer();
                if (winner != null && winner.getUuid().equals(player.getUuid())) {
                    stats.recordWin(leaderboardMode);

                    // Quest: Winner - won a game
                    player.getQuestHandler().addProgressByTrigger("murdermystery.games_won", 1);
                }
            } else {
                // Classic/Double Up mode
                if (innocentsWon && (role == GameRole.INNOCENT || role == GameRole.DETECTIVE)) {
                    // Innocents/Detective won
                    if (role == GameRole.DETECTIVE) {
                        stats.recordDetectiveWin(leaderboardMode);
                        // Record quickest detective win
                        stats.setQuickestDetectiveWin(leaderboardMode, gameDuration);
                    } else {
                        stats.recordWin(leaderboardMode);
                    }

                    // Quest: Winner - won a game
                    player.getQuestHandler().addProgressByTrigger("murdermystery.games_won", 1);

                    // Check if this player killed the murderer (hero)
                    if (murdererKiller != null && murdererKiller.getUuid().equals(player.getUuid())
                            && role != GameRole.DETECTIVE) {
                        stats.recordKillAsHero(leaderboardMode);
                    }
                } else if (murdererWon && role == GameRole.MURDERER) {
                    stats.recordMurdererWin(leaderboardMode);
                    // Record quickest murderer win
                    stats.setQuickestMurdererWin(leaderboardMode, gameDuration);

                    // Quest: Winner - won a game
                    player.getQuestHandler().addProgressByTrigger("murdermystery.games_won", 1);
                    // Quest: Power Play - won as Murderer
                    player.getQuestHandler().addProgressByTrigger("murdermystery.power_play", 1);
                }
            }
        }
    }

    private boolean hasMinimumPlayers() {
        return players.size() >= gameType.getMinPlayers();
    }

    private int countAlivePlayers() {
        return (int) players.stream()
                .filter(p -> !p.isEliminated())
                .count();
    }

    private int countAliveNonMurderers() {
        return (int) players.stream()
                .filter(p -> !p.isEliminated())
                .filter(p -> roleManager.getRole(p.getUuid()) != GameRole.MURDERER)
                .count();
    }

    private MurderMysteryPlayer getLastStandingPlayer() {
        return players.stream()
                .filter(p -> !p.isEliminated())
                .findFirst()
                .orElse(null);
    }

    public void forceStart() {
        forceStart(5);
    }

    public void forceStart(int seconds) {
        if (gameStatus != GameStatus.WAITING) return;
        countdown.forceStart(seconds);
    }

    public Audience getPlayersAsAudience() {
        return Audience.audience(players);
    }

    private void broadcastMessage(Component message) {
        getPlayersAsAudience().sendMessage(message);
    }

    private enum WinCondition {
        INNOCENTS_WIN, MURDERER_WINS, TIME_EXPIRED, LAST_STANDING
    }
}
