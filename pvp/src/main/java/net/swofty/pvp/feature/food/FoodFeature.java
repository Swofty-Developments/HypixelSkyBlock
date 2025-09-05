package net.swofty.pvp.feature.food;

import net.swofty.pvp.feature.CombatFeature;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

/**
 * Combat feature which manages player eating and their food and saturation values.
 */
public interface FoodFeature extends CombatFeature {
	FoodFeature NO_OP = new FoodFeature() {
		@Override
		public void addFood(Player player, int food, float saturation) {}
		
		@Override
		public void eat(Player player, int food, float saturationModifier) {}
		
		@Override
		public void eat(Player player, ItemStack stack) {}
		
		@Override
		public void applySaturationEffect(Player player, int amplifier) {}
	};
	
	/**
	 * Adds food to a player.
	 *
	 * @param player the player to add food to
	 * @param food the food amount
	 * @param saturation the saturation of the food
	 */
	void addFood(Player player, int food, float saturation);
	
	/**
	 * Legacy method for a player to eat. Still used by the saturation effect.
	 *
	 * @param player the player which is eating
	 * @param food the food amount
	 * @param saturationModifier the saturation modifier which along with the food amount is used to calculate the saturation
	 */
	void eat(Player player, int food, float saturationModifier);
	
	/**
	 * Modern method for a player to eat. Uses Minestoms registry values for the food amount and saturation.
	 *
	 * @param player the player which is eating
	 * @param stack the item to eat
	 */
	void eat(Player player, ItemStack stack);
	
	/**
	 * Applies effect of the saturation potion effect to a player.
	 * If a player has the effect, this will be called on a regular basis.
	 *
	 * @param player the player to apply the effect to
	 * @param amplifier the amplifier of the effect
	 */
	void applySaturationEffect(Player player, int amplifier);
}
