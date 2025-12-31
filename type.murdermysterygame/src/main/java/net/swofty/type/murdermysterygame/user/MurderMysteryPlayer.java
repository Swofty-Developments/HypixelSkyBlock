package net.swofty.type.murdermysterygame.user;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class MurderMysteryPlayer extends HypixelPlayer {
    private boolean eliminated = false;
    private int goldCollected = 0;
    private int tokensEarnedThisGame = 0;
    private int killsThisGame = 0;

    public MurderMysteryPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }

    public void addGold(int amount) {
        this.goldCollected += amount;
    }

    public void resetGold() {
        this.goldCollected = 0;
    }

    public void addTokens(int amount) {
        this.tokensEarnedThisGame += amount;
    }

    public void addKill() {
        this.killsThisGame++;
    }

    public void resetGameStats() {
        this.tokensEarnedThisGame = 0;
        this.killsThisGame = 0;
        this.goldCollected = 0;
        this.eliminated = false;
    }
}
