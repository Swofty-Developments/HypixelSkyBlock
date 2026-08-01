package net.swofty.type.ravengarddungeon.game;

import net.minestom.server.instance.InstanceContainer;
import net.swofty.type.game.game.AbstractGame;
import net.swofty.type.game.game.Game;
import net.swofty.type.ravengarddungeon.config.RavengardDungeonConfig;
import net.swofty.type.ravengarddungeon.user.RavengardDungeonPlayer;

public class RavengardDungeonGame extends AbstractGame<RavengardDungeonPlayer> {
    private final RavengardDungeonConfig config;

    public RavengardDungeonGame(InstanceContainer instance, RavengardDungeonConfig config) {
        super(instance, _ -> {});
        this.config = config;
    }

    @Override
    public Game.JoinResult join(RavengardDungeonPlayer player) {
        Game.JoinResult result = super.join(player);
        if (result instanceof Game.JoinResult.Success) {
            player.setInstance(instance, config.spawnPosition());
        }
        return result;
    }

    @Override
    public int getMaxPlayers() {
        return 4;
    }

    @Override
    public int getMinPlayers() {
        return 1;
    }
}
