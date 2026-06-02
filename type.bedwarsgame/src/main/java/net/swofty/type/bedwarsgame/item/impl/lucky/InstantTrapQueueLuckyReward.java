package net.swofty.type.bedwarsgame.item.impl.lucky;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

import java.util.List;

public class InstantTrapQueueLuckyReward extends LuckyReward {
    public InstantTrapQueueLuckyReward() {
        super("Instant Trap Queue");
    }

    @Override
    public void apply(BedWarsPlayer player, Pos openedAt) {
        List<String> traps = List.of("blindness", "counter_offensive", "reveal");
        traps.forEach(trap -> player.getGame().addTeamTrap(player.getTeamKey(), trap));
        player.sendMessage("§aFilled your trap queue.");
    }
}
