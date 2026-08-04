package net.swofty.type.ravengarddungeon.user;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.tag.Tag;
import net.swofty.type.game.game.GameParticipant;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Setter
@Getter
public class RavengardDungeonPlayer extends RavengardPlayer implements GameParticipant {
    private static final Tag<String> GAME_ID = Tag.String("ravengard_game_id");
    private static final Tag<Integer> TEAM = Tag.Integer("ravengard_dungeon_team");
    private static final Tag<Boolean> DEAD = Tag.Boolean("ravengard_dungeon_dead");
    private UUID spectatingTeammate;

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

    public int getDungeonTeam() {
        Integer team = getTag(TEAM);
        return team == null ? -1 : team;
    }

    public void setDungeonTeam(int team) {
        setTag(TEAM, team);
    }

    public boolean isDungeonDead() {
        return Boolean.TRUE.equals(getTag(DEAD));
    }

    public void setDungeonDead(boolean dead) {
        setTag(DEAD, dead);
    }

}
