package net.swofty.pvp.feature.projectile;

import net.swofty.pvp.feature.CombatFeature;

/**
 * Combat feature which handles crossbow shooting.
 */
public interface CrossbowFeature extends CombatFeature {
	CrossbowFeature NO_OP = new CrossbowFeature() {};
}
