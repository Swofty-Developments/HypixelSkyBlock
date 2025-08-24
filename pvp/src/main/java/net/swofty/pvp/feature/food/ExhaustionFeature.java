package net.swofty.pvp.feature.food;

import net.swofty.pvp.feature.CombatFeature;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;

/**
 * Combat feature which manages player exhaustion and its influence on their food and saturation values.
 */
public interface ExhaustionFeature extends CombatFeature {
	ExhaustionFeature NO_OP = new ExhaustionFeature() {
		@Override
		public void addExhaustion(Player player, float exhaustion) {}
		
		@Override
		public void addAttackExhaustion(Player player) {}
		
		@Override
		public void addDamageExhaustion(Player player, DamageType type) {}
		
		@Override
		public void applyHungerEffect(Player player, int amplifier) {}
	};
	
	void addExhaustion(Player player, float exhaustion);
	
	/**
	 * Applies the exhaustion from an attack to a player.
	 *
	 * @param player the player to apply the attack exhaustion to
	 */
	void addAttackExhaustion(Player player);
	
	/**
	 * Applies the exhaustion from taking damage to a player.
	 *
	 * @param player the player to apply the damage exhaustion to
	 * @param type the damage type
	 */
	void addDamageExhaustion(Player player, DamageType type);
	
	/**
	 * Applies effect of the hunger potion effect to a player.
	 * If a player has the effect, this will be called on a regular basis.
	 *
	 * @param player the player to apply the effect to
	 * @param amplifier the amplifier of the effect
	 */
	void applyHungerEffect(Player player, int amplifier);
}
