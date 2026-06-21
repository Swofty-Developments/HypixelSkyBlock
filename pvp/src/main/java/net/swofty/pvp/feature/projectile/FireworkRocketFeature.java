package net.swofty.pvp.feature.projectile;

import net.swofty.pvp.feature.CombatFeature;

/**
 * Combat feature which handles using fireworks.
 */
public interface FireworkRocketFeature extends CombatFeature {
    FireworkRocketFeature NO_OP = new FireworkRocketFeature() {
    };
}