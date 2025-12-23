package net.swofty.type.skyblockgeneric.potion.handler;

import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

/**
 * Handler for base potions that have no effect (Awkward, Thick, Mundane, Water).
 */
public class NoOpHandler implements PotionEffectHandler {

    public static final NoOpHandler INSTANCE = new NoOpHandler();

    @Override
    public void applyToPlayer(SkyBlockPlayer player, int level, long durationMs) {
        // No effect
    }

    @Override
    public boolean affectsOtherPlayers() {
        return false;
    }

    @Override
    public boolean affectsMobs() {
        return false;
    }
}
