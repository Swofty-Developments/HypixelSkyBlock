package net.swofty.pvp.feature.cooldown;

import net.swofty.pvp.feature.CombatFeature;
import net.minestom.server.entity.Player;

/**
 * Combat feature used to manage a players attack cooldown.
 */
public interface AttackCooldownFeature extends CombatFeature {
	AttackCooldownFeature NO_OP = new AttackCooldownFeature() {
		@Override
		public void resetCooldownProgress(Player player) {
		}
		
		@Override
		public double getAttackCooldownProgress(Player player) {
			return 1.0;
		}
	};
	
	/**
	 * Reset the attack cooldown progress of the player, so it can attack again.
	 *
	 * @param player the player to reset the attack cooldown progress from
	 */
	void resetCooldownProgress(Player player);
	
	/**
	 * Get the attack cooldown progress of the player,
	 * a value between 0.0 and 1.0 where higher values mean more attack damage.
	 *
	 * @param player the player to get the attack cooldown progress from
	 * @return the attack cooldown progress of the player
	 */
	double getAttackCooldownProgress(Player player);
}
