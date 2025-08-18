package net.swofty.type.bedwarsgame.shop.traps;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.Trap;

public class CounterOffensiveTrap extends Trap {

    public CounterOffensiveTrap() {
        super(
                "counter_offensive_trap",
                "Counter-Offensive Trap",
                ItemStack.of(Material.FEATHER),
                "Grants Speed II to your team for 10s when triggered.",
                Currency.DIAMOND
        );
    }

    @Override
    public void triggered(Game game, String teamName, Player triggerer) {
        game.getPlayers().stream()
            .filter(p -> teamName.equals(p.getTag(Tag.String("team"))))
            .forEach(p -> p.addEffect(new Potion(PotionEffect.SPEED, (byte)1, 200)));
    }
}

