package net.swofty.pvp.feature.projectile;

import net.swofty.pvp.feature.CombatFeature;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Combat feature which determines which projectile to use for bow and crossbow shooting.
 */
public interface ProjectileItemFeature extends CombatFeature {
	ProjectileItemFeature NO_OP = new ProjectileItemFeature() {
		@Override
		public @Nullable ProjectileItem getBowProjectile(Player player) {
			return null;
		}
		
		@Override
		public @Nullable ProjectileItem getCrossbowProjectile(Player player) {
			return null;
		}
	};
	
	@Nullable ProjectileItem getBowProjectile(Player player);
	
	@Nullable ProjectileItem getCrossbowProjectile(Player player);
	
	record ProjectileItem(int slot, ItemStack stack) {}
}
