package net.swofty.type.bedwarsgame.item.impl.lucky;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.damage.Damage;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

public class RouletteBlitzLuckyReward extends LuckyReward {
    public RouletteBlitzLuckyReward() {
        super("Roulette Blitz");
    }

    @Override
    public void apply(BedWarsPlayer player, Pos openedAt) {
        player.getGame().getPlayers().stream()
            .filter(other -> other != player && !other.getTeamKey().equals(player.getTeamKey()))
            .findAny()
            .ifPresent(other -> other.damage(Damage.fromPlayer(player, 40f)));
    }
}
