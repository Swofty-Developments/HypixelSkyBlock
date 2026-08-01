package net.swofty.type.ravengarddungeon.user;

import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.tag.Tag;
import net.swofty.type.game.game.GameParticipant;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;
import org.jetbrains.annotations.NotNull;

public class RavengardDungeonPlayer extends RavengardPlayer implements GameParticipant {
    private static final Tag<String> GAME_ID = Tag.String("ravengard_game_id");

    public RavengardDungeonPlayer(@NotNull PlayerConnection connection, @NotNull GameProfile profile) {
        super(connection, profile);
    }

    @Override
    public String getGameId() {
        return getTag(GAME_ID);
    }

    @Override
    public void setGameId(String gameId) {
        if (gameId == null) removeTag(GAME_ID);
        else setTag(GAME_ID, gameId);
    }

    @Override
    public net.minestom.server.entity.Player getServerPlayer() {
        return this;
    }
}
