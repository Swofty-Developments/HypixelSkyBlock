package net.swofty.pvp.feature.block;

import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.item.ItemStack;

/**
 * Combat feature which manages legacy blocking for a player.
 */
public interface LegacyBlockFeature extends BlockFeature {
	LegacyBlockFeature NO_OP = new LegacyBlockFeature() {
		@Override public boolean isBlocking(Player player) { return false; }
		@Override public void block(Player player) {}
		@Override public void unblock(Player player) {}
		@Override public boolean canBlockWith(Player player, ItemStack stack) { return false; }
		@Override public boolean isDamageBlocked(LivingEntity entity, Damage damage) { return false; }
		@Override public boolean applyBlock(LivingEntity entity, Damage damage) { return false; }
	};
	
	boolean isBlocking(Player player);
	
	void block(Player player);
	
	void unblock(Player player);
	
	boolean canBlockWith(Player player, ItemStack stack);
}
