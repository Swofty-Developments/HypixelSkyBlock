package net.swofty.type.bedwarsgame.item.impl.lucky;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.bedwarsgame.item.impl.LuckyBlockTrap;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

public class TrapLuckyReward extends LuckyReward {
    private final String trap;

    public TrapLuckyReward(String name, String trap) {
        super(name);
        this.trap = trap;
    }

    @Override
    public void apply(BedWarsPlayer player, Pos openedAt) {
        LuckyBlockTrap.place(player, openedAt, trap);
    }
}
