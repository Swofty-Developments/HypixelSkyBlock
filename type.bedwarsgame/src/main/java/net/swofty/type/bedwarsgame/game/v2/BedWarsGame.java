package net.swofty.type.bedwarsgame.game.v2;

import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.Event;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ChatUtility;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.MapTeam;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.bedwarsgame.BedWarsGameScoreboard;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.events.custom.BedDestroyedEvent;
import net.swofty.type.bedwarsgame.replay.BedWarsReplayManager;
import net.swofty.type.bedwarsgame.shop.impl.AxeShopItem;
import net.swofty.type.bedwarsgame.shop.impl.PickaxeShopItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.user.ExperienceCause;
import net.swofty.type.bedwarsgame.user.TokenCause;
import net.swofty.type.game.game.AbstractTeamGame;
import net.swofty.type.game.game.CountdownConfig;
import net.swofty.type.game.game.GameState;
import net.swofty.type.generic.event.HypixelEventHandler;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
public class BedWarsGame extends AbstractTeamGame<BedWarsPlayer, BedWarsTeam> {
    public static final Tag<Boolean> ELIMINATED_TAG = Tag.Boolean("eliminated");

    private final BedWarsMapsConfig.MapEntry mapEntry;
    private final BedwarsGameType gameType;

    private final BedWarsGeneratorManager generatorManager;
    private final BedWarsWorldManager worldManager;
    private final BedWarsRespawnHandler respawnHandler;
    private final BedWarsGameEventManager gameEventManager;
    private final BedWarsReplayManager replayManager;

    private final Map<TeamKey, Map<Integer, ItemStack>> teamChests = new EnumMap<>(TeamKey.class);
    private final Map<UUID, Map<Integer, ItemStack>> enderChests = new HashMap<>();

    public BedWarsGame(
        BedWarsMapsConfig.MapEntry mapEntry,
        InstanceContainer instance,
        BedwarsGameType gameType
    ) {
        // these need to happen before super() is called
        this.mapEntry = mapEntry;
        this.gameType = gameType;

        super(instance, event -> HypixelEventHandler.callCustomEvent((Event) event));


        // Initialize teams from map config
        mapEntry.getConfiguration().getTeams().keySet()
            .forEach(teamKey -> registerTeam(new BedWarsTeam(teamKey)));

        // Initialize managers
        this.generatorManager = new BedWarsGeneratorManager(this);
        this.worldManager = new BedWarsWorldManager(this);
        this.respawnHandler = new BedWarsRespawnHandler(this);
        this.gameEventManager = new BedWarsGameEventManager(this);

        // Initialize replay with service connection
        ProxyService replayService = new ProxyService(ServiceType.REPLAY);
        this.replayManager = new BedWarsReplayManager(this, replayService);

        // Start scoreboard
        BedWarsGameScoreboard.start(this);
    }

    @Override
    protected CountdownConfig getCountdownConfig() {
        return CountdownConfig.withAcceleration(30, 10, getMaxPlayers());
    }

    @Override
    public int getMaxPlayers() {
        return mapEntry.getConfiguration().getTeams().size() * getTeamSize();
    }

    @Override
    public int getMinPlayers() {
        return getTeamSize() * Math.min(2, mapEntry.getConfiguration().getTeams().size());
    }

    @Override
    protected int getTeamSize() {
        return Math.max(1, gameType.getTeamSize());
    }

    public boolean isBedAlive(TeamKey teamKey) {
        return getTeam(teamKey.name())
            .map(BedWarsTeam::isBedAlive)
            .orElse(false);
    }

    public boolean isPlayerCurrentlyPlaying(UUID playerUuid) {
        BedWarsPlayer player = getPlayer(playerUuid).get();
        return getPlayerTeam(playerUuid).isPresent()
            && !Boolean.TRUE.equals(player.getTag(ELIMINATED_TAG))
            && !(player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SPECTATOR)
            && !player.isInvisible();
    }

    /**
     * Gets players on a specific team.
     */
    public List<BedWarsPlayer> getPlayersOnTeam(TeamKey teamKey) {
        return getPlayers().stream()
            .filter(p -> teamKey.equals(p.getTeamKey()))
            .toList();
    }

    /**
     * Gets team traps for a team.
     */
    public List<String> getTeamTraps(TeamKey teamKey) {
        return getTeam(teamKey.name())
            .map(BedWarsTeam::getTraps)
            .orElse(List.of());
    }

    public void equipTeamArmor(BedWarsPlayer player, TeamKey teamKey) {
        respawnHandler.equipTeamArmor(player, teamKey);
    }

    public void addTeamTrap(TeamKey teamKey, String trapKey) {
        getTeam(teamKey.name()).ifPresent(team -> team.addTrap(trapKey));
    }

    public int getTeamUpgradeLevel(TeamKey teamKey, String upgradeName) {
        return getTeam(teamKey.name())
            .map(team -> team.getUpgradeLevel(upgradeName))
            .orElse(0);
    }

    @Nullable
    public String canAcceptPartyWarp() {
        if (state != GameState.WAITING) {
            return "Cannot warp as the game has already started";
        }
        return null;
    }

    public void onPlayerRejoin(BedWarsPlayer player, DisconnectedPlayerData data) {
        restorePlayerData(player, data.savedData());
        player.setInstance(instance);

        Optional<BedWarsTeam> teamOpt = getPlayerTeam(player.getUuid());
        if (teamOpt.isEmpty()) {
            player.sendTo(ServerType.BEDWARS_LOBBY);
            return;
        }

        BedWarsTeam team = teamOpt.get();
        String teamColor = team.getColorCode();
        player.setTeamName(team.getTeamKey());

        broadcastMessage(Component.text(teamColor + player.getUsername() + " §7reconnected."));

        if (!team.isBedAlive()) {
            setupAsSpectator(player);
        } else {
            respawnHandler.startRespawn(player);
        }
    }

    // find a better place for this method
    public static void literalSetupSpectator(BedWarsPlayer player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvisible(true);
        player.setFlying(true);
        player.getInventory().clear();
    }

    public void setupPlayer(BedWarsPlayer player) {
        TeamKey teamKey = player.getTeamKey();
        MapTeam playerTeam = getMapEntry().getConfiguration().getTeams().get(teamKey);
        BedWarsMapsConfig.PitchYawPosition spawnPos = playerTeam.getSpawn();

        player.getInventory().clear();
        player.clearTitle();
        player.teleport(new Pos(spawnPos.x(), spawnPos.y(), spawnPos.z(), spawnPos.pitch(), spawnPos.yaw()));
        player.setGameMode(GameMode.SURVIVAL);
        player.setInvisible(false);
        player.setFlying(false);
        player.getInventory().addItemStack(ItemStack.of(Material.WOODEN_SWORD));
        player.setHealth(20f);
        player.setFood(20);
        equipTeamArmor(player, teamKey);

        // Give back the downgraded tools
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

        // equip the player with team armor
        equipTeamArmor(player, teamKey);

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
    }

    @Override
    protected boolean canPlayerRejoin(BedWarsPlayer player) {
        return getPlayerTeam(player.getUuid())
            .map(BedWarsTeam::isBedAlive)
            .orElse(false);
    }

    @Override
    protected Map<String, Object> savePlayerData(BedWarsPlayer player) {
        Map<String, Object> data = new HashMap<>();
        data.put("armorLevel", player.getTag(TypeBedWarsGameLoader.ARMOR_LEVEL_TAG));
        return data;
    }

    protected void restorePlayerData(BedWarsPlayer player, Map<String, Object> savedData) {
        Integer armorLevel = (Integer) savedData.get("armorLevel");
        if (armorLevel != null) {
            player.setTag(TypeBedWarsGameLoader.ARMOR_LEVEL_TAG, armorLevel);
        }
    }
    @Override
    protected void onTeamEliminated(BedWarsTeam team) {
        super.onTeamEliminated(team);

        // Record to replay
        if (replayManager.isRecording()) {
            replayManager.recordTeamElimination(team.getTeamKey());
        }
    }

    @Override
    protected boolean isTeamViable(BedWarsTeam team) {
        Logger.info("Checking if team exists for team " + team.getTeamKey());
        // Team is viable if bed is alive OR has active players OR has rejoinable disconnected players
        if (team.isBedAlive()) return true;

        boolean hasActivePlayers = getPlayers().stream()
            .anyMatch(p -> team.hasPlayer(p.getUuid()) && !Boolean.TRUE.equals(p.getTag(ELIMINATED_TAG)));

        boolean hasRejoinablePlayers = disconnectedPlayers.values().stream()
            .anyMatch(info -> {
                Optional<BedWarsTeam> t = getPlayerTeam(info.uuid());
                return t.isPresent() && t.get().getId().equals(team.getId()) && team.isBedAlive();
            });

        Logger.info(hasActivePlayers + " active players, " + hasRejoinablePlayers + " rejoinable players for team " + team.getTeamKey());
        return hasActivePlayers || hasRejoinablePlayers;
    }

    public void onBedDestroyed(TeamKey teamKey, BedWarsPlayer destroyer) {
        getTeam(teamKey.name()).ifPresent(team -> {
            team.destroyBed();
            MapTeam mapTeam = mapEntry.getConfiguration().getTeams().get(teamKey);
            if (mapTeam == null) {
                Logger.error("No map team configuration found for team {}", teamKey.name());
                return;
            }
            BedWarsMapsConfig.TwoBlockPosition bedPos = mapTeam.getBed();
            if (bedPos == null || bedPos.feet() == null || bedPos.head() == null) {
                Logger.error("No bed position found for team {}", teamKey.name());
                return;
            }

            Point feetPoint = new Pos(bedPos.feet().x(), bedPos.feet().y(), bedPos.feet().z());
            Point headPoint = new Pos(bedPos.head().x(), bedPos.head().y(), bedPos.head().z());
            destroyer.getInstance().setBlock(feetPoint, Block.AIR);
            destroyer.getInstance().setBlock(headPoint, Block.AIR);

            // Record to replay
            if (replayManager.isRecording()) {
                replayManager.recordBedDestroyed(teamKey, destroyer);
            }

            eventDispatcher.accept(new BedDestroyedEvent(
                gameId,
                teamKey,
                destroyer
            ));

            checkWinConditions();
        });
    }

    /**
     * Respawns a team's bed. Usually an admin action, Lucky Block Bed Wars beds can be placed at any location
     * which isn't supported yet.
     */
    public void respawnBed(TeamKey teamKey) {
        getTeam(teamKey.name()).ifPresent(team -> {
            team.setBedAlive(true);

            // Place the bed blocks back
            worldManager.placeBedForTeam(teamKey, mapEntry.getConfiguration().getTeams().get(teamKey));

            // Record to replay
            if (replayManager.isRecording()) {
                replayManager.recordBedRespawned(teamKey);
            }

            broadcastMessage(Component.text(teamKey.chatColor() + "Team " + teamKey.getName() + "'s §abed has been respawned!"));
        });
    }

    /**
     * Called when a player is eliminated (died without bed).
     */
    public void onPlayerEliminated(BedWarsPlayer player) {
        player.setTag(ELIMINATED_TAG, true);
        setupAsSpectator(player);
        checkWinConditions();
    }

    private void setupAsSpectator(BedWarsPlayer player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvisible(true);
        player.setFlying(true);
        player.setAllowFlying(true);
        player.updateBelowTag();

        BedWarsMapsConfig.Position spectatorPos = mapEntry.getConfiguration().getLocations().getSpectator();
        if (spectatorPos != null) {
            player.teleport(new Pos(spectatorPos.x(), spectatorPos.y(), spectatorPos.z()));
        }

        player.sendTitlePart(TitlePart.TITLE, Component.text("SPECTATING", NamedTextColor.GRAY));
        player.sendTitlePart(TitlePart.SUBTITLE, Component.text("Your bed was destroyed.", NamedTextColor.RED));

        player.getInventory().setItemStack(0, TypeBedWarsGameLoader.getItemHandler().getItem("teleporter").getItemStack());
        player.getInventory().setItemStack(4, TypeBedWarsGameLoader.getItemHandler().getItem("spectator_settings").getItemStack());
        player.getInventory().setItemStack(7, TypeBedWarsGameLoader.getItemHandler().getItem("play_again").getItemStack());
        player.getInventory().setItemStack(8, TypeBedWarsGameLoader.getItemHandler().getItem("leave_game").getItemStack());
    }

    public Map<TeamKey, MapTeam> getActiveTeamConfigs() {
        Map<TeamKey, MapTeam> configs = new EnumMap<>(TeamKey.class);
        Map<TeamKey, MapTeam> allTeamConfigs = mapEntry.getConfiguration().getTeams();

        for (BedWarsTeam team : getActiveTeams()) {
            TeamKey key = team.getTeamKey();
            if (allTeamConfigs.containsKey(key)) {
                configs.put(key, allTeamConfigs.get(key));
            }
        }

        return configs;
    }

    public void teleportPlayersToSpawns() {
        Map<TeamKey, MapTeam> teamConfigs = mapEntry.getConfiguration().getTeams();

        for (BedWarsPlayer player : getPlayers()) {
            getPlayerTeam(player.getUuid()).ifPresent(team -> {
                MapTeam config = teamConfigs.get(team.getTeamKey());
                if (config != null) {
                    setupPlayer(player);
                    player.reveal();
                    player.setDisplayName(Component.text(
                        team.getColorCode() + "§l" + team.firstLetter() + " §r" + team.getColorCode() + player.getUsername()
                    ));
                }
            });
        }
    }

    public void startTimePlayedRewards() {
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (state != GameState.IN_PROGRESS) return;
            for (BedWarsPlayer player : getPlayers()) {
                if (Boolean.TRUE.equals(player.getTag(ELIMINATED_TAG))) continue; // Skip eliminated spectators
                player.token(TokenCause.TIME_PLAYED);
                player.xp(ExperienceCause.TIME_PLAYED);
            }
        }).delay(TaskSchedule.minutes(1)).repeat(TaskSchedule.minutes(1)).schedule();
    }

    public void sendGameStartMessage() {
        String line = "■".repeat(50);
        Component[] messages = {
            Component.text(line, NamedTextColor.GREEN),
            Component.text(ChatUtility.FontInfo.center("§lBed Wars"), NamedTextColor.WHITE),
            Component.space(),
            Component.text(ChatUtility.FontInfo.center("Protect your bed and destroy the enemy beds."), NamedTextColor.YELLOW),
            Component.text(ChatUtility.FontInfo.center("Upgrade yourself and your team by collecting"), NamedTextColor.YELLOW),
            Component.text(ChatUtility.FontInfo.center("Iron, Gold, Emerald and Diamond from generators"), NamedTextColor.YELLOW),
            Component.text(ChatUtility.FontInfo.center("to access powerful upgrades."), NamedTextColor.YELLOW),
            Component.space(),
            Component.text(line, NamedTextColor.GREEN)
        };

        Audience audience = Audience.audience(getPlayers());
        for (Component msg : messages) {
            audience.sendMessage(msg);
        }
    }

    public void broadcastMessage(Component message) {
        Audience.audience(getPlayers()).sendMessage(message);
    }

    public List<UUID> getDisconnectedPlayerUuids() {
        return new ArrayList<>(disconnectedPlayers.keySet());
    }

    // widens access modifier
    @Override
    public void checkWinConditions() {
        super.checkWinConditions();
    }
}
