package net.swofty.pvp.feature.projectile;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import net.swofty.pvp.entity.projectile.Arrow;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.inventory.PlayerInventoryUtils;

/**
 * Vanilla implementation of {@link ProjectileItemFeature}
 */
public class VanillaProjectileItemFeature implements ProjectileItemFeature {
	public static final DefinedFeature<VanillaProjectileItemFeature> DEFINED = new DefinedFeature<>(
			FeatureType.PROJECTILE_ITEM, configuration -> new VanillaProjectileItemFeature()
	);
	
	public static final Predicate<ItemStack> ARROW_PREDICATE = stack ->
			stack.material() == Material.ARROW
					|| stack.material() == Material.SPECTRAL_ARROW
					|| stack.material() == Material.TIPPED_ARROW;
	
	public static final Predicate<ItemStack> ARROW_OR_FIREWORK_PREDICATE = ARROW_PREDICATE.or(stack ->
			stack.material() == Material.FIREWORK_ROCKET);
	
	@Override
	public @Nullable ProjectileItem getBowProjectile(Player player) {
		return getProjectile(player, ARROW_PREDICATE, ARROW_PREDICATE);
	}
	
	@Override
	public @Nullable ProjectileItem getCrossbowProjectile(Player player) {
		return getProjectile(player, ARROW_OR_FIREWORK_PREDICATE, ARROW_PREDICATE);
	}
	
	public static @Nullable ProjectileItem getProjectile(Player player, Predicate<ItemStack> heldSupportedPredicate,
	                                                     Predicate<ItemStack> allSupportedPredicate) {
		ProjectileItem held = getHeldItem(player, heldSupportedPredicate);
		if (held != null) return held;
		
		ItemStack[] itemStacks = player.getInventory().getItemStacks();
		for (int i = 0; i < itemStacks.length; i++) {
			ItemStack stack = itemStacks[i];
			if (stack == null || stack.isAir()) continue;
			if (allSupportedPredicate.test(stack)) return new ProjectileItem(i, stack);
		}
		
		if (player.getGameMode() == GameMode.CREATIVE) {
			return new ProjectileItem(-1, Arrow.DEFAULT_ARROW);
		} else {
			return null;
		}
	}
	
	private static @Nullable ProjectileItem getHeldItem(Player player, Predicate<ItemStack> predicate) {
		ItemStack stack = player.getItemInHand(PlayerHand.OFF);
		if (predicate.test(stack)) return new ProjectileItem(PlayerInventoryUtils.OFFHAND_SLOT, stack);
		
		stack = player.getItemInHand(PlayerHand.MAIN);
		if (predicate.test(stack)) return new ProjectileItem(player.getHeldSlot(), stack);
		
		return null;
	}
}
