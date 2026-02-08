package net.swofty.type.ravengaardgeneric.user;

import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.ravengaardgeneric.data.RavengaardDataHandler;
import org.jetbrains.annotations.NotNull;

public class RavengaardPlayer extends HypixelPlayer {
    public RavengaardPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }

    public RavengaardDataHandler getRavengaardDataHandler() {
        return RavengaardDataHandler.getUser(this.getUuid());
    }
}
