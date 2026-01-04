package net.swofty.type.skywarsgame.user;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarsgame.TypeSkywarsGameLoader;
import net.swofty.type.skywarsgame.game.SkywarsGame;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class SkywarsPlayer extends HypixelPlayer {
    private boolean eliminated = false;
    private int killsThisGame = 0;
    private int assistsThisGame = 0;
    private int chestsOpenedThisGame = 0;
    private int soulsEarnedThisGame = 0;
    private Pos cagePosition = null;
    private String selectedKit = "default";
    private Set<String> activePerks = new HashSet<>();
    private UUID lastDamager = null;
    private long lastDamageTime = 0;
    private int arrowsShotThisGame = 0;
    private int arrowsHitThisGame = 0;
    private int mobsKilledThisGame = 0;

    public SkywarsPlayer(PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }

    public void addKill() {
        killsThisGame++;
    }

    public void addAssist() {
        assistsThisGame++;
    }

    public void addChestOpened() {
        chestsOpenedThisGame++;
    }

    public void addSouls(int amount) {
        soulsEarnedThisGame += amount;
    }

    public void addArrowShot() {
        arrowsShotThisGame++;
    }

    public void addArrowHit() {
        arrowsHitThisGame++;
    }

    public void addMobKill() {
        mobsKilledThisGame++;
    }

    public void setLastDamager(UUID damager) {
        this.lastDamager = damager;
        this.lastDamageTime = System.currentTimeMillis();
    }

    public UUID getAssistDamager() {
        if (lastDamager != null && System.currentTimeMillis() - lastDamageTime < 10000) {
            return lastDamager;
        }
        return null;
    }

    public void setupForSpectator() {
        getInventory().clear();
        getInventory().setItemStack(0,
                TypeSkywarsGameLoader.getItemHandler().getItem("spectator_compass").getItemStack());
        getInventory().setItemStack(7,
                TypeSkywarsGameLoader.getItemHandler().getItem("play_again").getItemStack());
        getInventory().setItemStack(8,
                TypeSkywarsGameLoader.getItemHandler().getItem("leave_game").getItemStack());

        SkywarsGame game = TypeSkywarsGameLoader.getPlayerGame(this);
        if (game != null) {
            for (SkywarsPlayer otherPlayer : game.getPlayers()) {
                if (!otherPlayer.equals(this) && !otherPlayer.isEliminated()) {
                    this.removeViewer(otherPlayer);
                    this.updateOldViewer(otherPlayer);
                }
            }
        }

        setGameMode(GameMode.ADVENTURE);
        setAllowFlying(true);
        setFlying(true);
    }

    public void resetGameState() {
        eliminated = false;
        killsThisGame = 0;
        assistsThisGame = 0;
        chestsOpenedThisGame = 0;
        soulsEarnedThisGame = 0;
        arrowsShotThisGame = 0;
        arrowsHitThisGame = 0;
        mobsKilledThisGame = 0;
        cagePosition = null;
        activePerks = new HashSet<>();
        lastDamager = null;
        lastDamageTime = 0;
    }
}
