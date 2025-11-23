package net.swofty.pvp.feature.projectile;

import net.swofty.pvp.feature.CombatFeature;

/**
 * Combat feature which handles bow shooting.
 */
public interface BowFeature extends CombatFeature {
	BowFeature NO_OP = new BowFeature() {};
}
