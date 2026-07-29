package net.swofty.type.bedwarsgame.item.impl.lucky;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

public class EffectLuckyReward extends LuckyReward {
    private final PotionEffect effect;
    private final int amplifier;
    private final int duration;

    public EffectLuckyReward(String name, PotionEffect effect, int amplifier, int duration) {
        super(name);
        this.effect = effect;
        this.amplifier = amplifier;
        this.duration = duration;
    }

    @Override
    public void apply(BedWarsPlayer player, Pos openedAt) {
        player.addEffect(new Potion(effect, (byte) amplifier, duration));
    }
}
