package net.swofty.pvp.feature.knockback;

import net.swofty.pvp.feature.CombatFeature;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.Damage;

/**
 * Combat feature which handles different types of knockback.
 */
public interface KnockbackFeature extends CombatFeature {
	KnockbackFeature NO_OP = new KnockbackFeature() {
		@Override
		public boolean applyDamageKnockback(Damage damage, LivingEntity target) {
			return false;
		}
		
		@Override
		public boolean applyAttackKnockback(LivingEntity attacker, LivingEntity target, int knockback) {
			return false;
		}
		
		@Override
		public boolean applySweepingKnockback(LivingEntity attacker, LivingEntity target) {
			return false;
		}
	};
	
	/**
	 * Applies base knockback to the target entity.
	 *
	 * @param damage the damage that caused the knockback
	 * @param target the entity that is receiving the knockback
	 * @return true if the target entity was knocked back, false otherwise
	 */
	boolean applyDamageKnockback(Damage damage, LivingEntity target);
	
	/**
	 * Applies an extra attack knockback to the target entity.
	 *
	 * @param attacker the attacker that caused the knockback
	 * @param target the entity that is receiving the knockback
	 * @return true if the target entity was knocked back, false otherwise
	 */
	boolean applyAttackKnockback(LivingEntity attacker, LivingEntity target, int knockback);
	
	/**
	 * Applies sweeping knockback to the target entity.
	 *
	 * @param attacker the attacker that caused the knockback
	 * @param target the entity that is receiving the knockback
	 * @return true if the target entity was knocked back, false otherwise
	 */
	boolean applySweepingKnockback(LivingEntity attacker, LivingEntity target);
}
