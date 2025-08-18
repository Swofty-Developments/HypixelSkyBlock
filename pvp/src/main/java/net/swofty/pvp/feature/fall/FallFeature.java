package net.swofty.pvp.feature.fall;

import net.swofty.pvp.feature.CombatFeature;
import net.minestom.server.entity.LivingEntity;

/**
 * Combat feature which manages the fall distance and fall damage of entities.
 * It may also apply this damage when needed.
 */
public interface FallFeature extends CombatFeature {
	FallFeature NO_OP = new FallFeature() {
		@Override
		public int getFallDamage(LivingEntity entity, double fallDistance) {
			return 0;
		}
		
		@Override
		public double getFallDistance(LivingEntity entity) {
			return 0;
		}
		
		@Override
		public void resetFallDistance(LivingEntity entity) {}
		
		@Override
		public void setExtraFallParticles(LivingEntity entity, boolean extraFallParticles) {}
	};
	
	int getFallDamage(LivingEntity entity, double fallDistance);
	
	double getFallDistance(LivingEntity entity);
	
	void resetFallDistance(LivingEntity entity);
	
	void setExtraFallParticles(LivingEntity entity, boolean extraFallParticles);
}
