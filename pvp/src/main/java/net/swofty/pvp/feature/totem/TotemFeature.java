package net.swofty.pvp.feature.totem;

import net.swofty.pvp.feature.CombatFeature;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.DamageType;

/**
 * Combat feature which determines whether a totem protects a player and what happens afterward.
 */
public interface TotemFeature extends CombatFeature {
	TotemFeature NO_OP = (entity, type) -> false;
	
	/**
	 * Returns whether the entity is protected. May also apply (visual) effects.
	 *
	 * @param entity the entity to check for
	 * @param type the type of damage being done to the entity
	 * @return whether the entity is protected by a totem
	 */
	boolean tryProtect(LivingEntity entity, DamageType type);
}
