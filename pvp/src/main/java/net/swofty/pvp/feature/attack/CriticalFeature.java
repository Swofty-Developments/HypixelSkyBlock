package net.swofty.pvp.feature.attack;

import net.swofty.pvp.feature.CombatFeature;
import net.minestom.server.entity.LivingEntity;

/**
 * Combat feature used to determine when an attack was critical and what this means for the resulting damage.
 */
public interface CriticalFeature extends CombatFeature {
	CriticalFeature NO_OP = new CriticalFeature() {
		@Override
		public boolean shouldCrit(LivingEntity attacker, AttackValues.PreCritical values) {
			return false;
		}
		
		@Override
		public float applyToDamage(float damage) {
			return damage;
		}
	};
	
	boolean shouldCrit(LivingEntity attacker, AttackValues.PreCritical values);
	
	/**
	 * Determines the new damage amount when the attack was critical.
	 *
	 * @param damage the old damage amount
	 * @return the new damage amount
	 */
	float applyToDamage(float damage);
}
