package net.swofty.pvp.feature.damage;

import net.swofty.pvp.feature.CombatFeature;

/**
 * Combat feature which handles entities being damaged.
 */
public interface DamageFeature extends CombatFeature {
	DamageFeature NO_OP = new DamageFeature() {};
}
