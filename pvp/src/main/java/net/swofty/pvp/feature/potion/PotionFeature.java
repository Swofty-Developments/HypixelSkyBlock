package net.swofty.pvp.feature.potion;

import net.swofty.pvp.feature.CombatFeature;

/**
 * Combat feature which handles drinking and throwing potions.
 */
public interface PotionFeature extends CombatFeature {
	PotionFeature NO_OP = new PotionFeature() {};
}
