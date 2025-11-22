package net.swofty.pvp.feature.projectile;

import net.swofty.pvp.feature.CombatFeature;

/**
 * Combat feature which handles throwing snowballs, eggs and ender pearls.
 */
public interface MiscProjectileFeature extends CombatFeature {
	MiscProjectileFeature NO_OP = new MiscProjectileFeature() {};
}
