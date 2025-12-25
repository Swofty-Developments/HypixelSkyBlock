package net.swofty.pvp.feature.explosion;

import net.swofty.pvp.feature.CombatFeature;

/**
 * Combat feature which handles placing and igniting explosives.
 * Supports tnt, end crystals and respawn anchors.
 */
public interface ExplosiveFeature extends CombatFeature {
	ExplosiveFeature NO_OP = new ExplosiveFeature() {};
}
