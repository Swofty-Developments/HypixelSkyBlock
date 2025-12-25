package net.swofty.pvp.feature.tracking;

import net.swofty.pvp.feature.CombatFeature;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import org.jetbrains.annotations.Nullable;

/**
 * Combat feature which is used for tracking all the damage to a player and their death message.
 */
public interface TrackingFeature extends CombatFeature {
	TrackingFeature NO_OP = new TrackingFeature() {
		@Override
		public void recordDamage(Player player, @Nullable Entity attacker, Damage damage) {}
		
		@Override
		public @Nullable Component getDeathMessage(Player player) {
			return null;
		}
	};
	
	void recordDamage(Player player, @Nullable Entity attacker, Damage damage);
	
	@Nullable
	Component getDeathMessage(Player player);
}
