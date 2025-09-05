package net.swofty.pvp.feature.block;

import net.swofty.pvp.feature.CombatFeature;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.Damage;

/**
 * Combat feature used to determine whether an entity is blocking damage and how the block should be applied.
 */
public interface BlockFeature extends CombatFeature {
	BlockFeature NO_OP = new BlockFeature() {
		@Override
		public boolean isDamageBlocked(LivingEntity entity, Damage damage) {
			return false;
		}
		
		@Override
		public boolean applyBlock(LivingEntity entity, Damage damage) {
			return false;
		}
	};
	
	boolean isDamageBlocked(LivingEntity entity, Damage damage);
	
	/**
	 * Applies the block to the {@link Damage} object.
	 *
	 * @param entity the entity blocking the damage
	 * @param damage the damage object
	 * @return whether the damage was FULLY blocked
	 */
	boolean applyBlock(LivingEntity entity, Damage damage);
}
