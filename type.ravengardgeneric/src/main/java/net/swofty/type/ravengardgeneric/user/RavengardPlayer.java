package net.swofty.type.ravengardgeneric.user;

import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.ravengardgeneric.data.RavengardDataHandler;
import org.jetbrains.annotations.NotNull;

public class RavengardPlayer extends HypixelPlayer {
    public RavengardPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }

    public RavengardDataHandler getRavengardDataHandler() {
        return RavengardDataHandler.getUser(this.getUuid());
    }
}
