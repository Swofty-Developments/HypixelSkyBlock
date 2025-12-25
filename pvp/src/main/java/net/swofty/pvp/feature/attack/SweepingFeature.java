package net.swofty.pvp.feature.attack;

import net.swofty.pvp.feature.CombatFeature;
import net.minestom.server.entity.LivingEntity;

import java.util.Collection;
import java.util.List;

/**
 * Combat feature used to determine whether an attack is a sweeping attack and also used for applying the sweeping.
 */
public interface SweepingFeature extends CombatFeature {
	SweepingFeature NO_OP = new SweepingFeature() {
		@Override
		public boolean shouldSweep(LivingEntity attacker, AttackValues.PreSweeping values) {
			return false;
		}
		
		@Override
		public float getSweepingDamage(LivingEntity attacker, float damage) {
			return 0;
		}
		
		@Override
		public Collection<LivingEntity> applySweeping(LivingEntity attacker, LivingEntity target, float damage) {
			return List.of();
		}
	};
	
	boolean shouldSweep(LivingEntity attacker, AttackValues.PreSweeping values);
	
	float getSweepingDamage(LivingEntity attacker, float damage);
	
	/**
	 * Should return a collection of the affected entities.
	 */
	Collection<LivingEntity> applySweeping(LivingEntity attacker, LivingEntity target, float damage);
}
