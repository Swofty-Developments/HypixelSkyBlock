package net.swofty.type.bedwarsgame.util;

import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.Arrays;

public class InventoryManipulation {

	public static boolean consumeItemIfNotCreative(Player player, PlayerHand hand, int amount) {
		if (player.getGameMode() == GameMode.CREATIVE) {
			return true;
		}

		ItemStack item = player.getItemInHand(hand);
		item = item.withAmount(amt -> amt - amount);
		if (item.amount() == 0) item = ItemStack.AIR;
		if (item.amount() < 0) return false;
		player.setItemInHand(hand, item);
		return true;
	}

	public static void removeItems(Player player, Material material, int amount) {
		PlayerInventory inventory = player.getInventory();
		ItemStack[] slots = inventory.getItemStacks();
		int remaining = amount;

		for (int i = 0; i < slots.length && remaining > 0; i++) {
			var stack = slots[i];
			if (stack.material() == material) {
				int remove = Math.min(stack.amount(), remaining);
				int updated = stack.amount() - remove;
				inventory.setItemStack(i,
						updated > 0 ? stack.withAmount(updated) : ItemStack.AIR);
				remaining -= remove;
			}
		}
	}

	public static boolean hasEnoughMaterial(Player player, Material material, int amount) {
		return Arrays.stream(player.getInventory().getItemStacks())
				.filter(stack -> stack.material() == material)
				.mapToInt(ItemStack::amount)
				.sum() >= amount;
	}

	public static ItemStack hideTooltip(ItemStack item) {
		return item.with(
				ItemComponent.HIDE_ADDITIONAL_TOOLTIP
		);
	}

	public static boolean canBeChested(Material m) {
		if (m.name().endsWith("_axe") ||
				m.name().endsWith("_sword") ||
				m.name().endsWith("_pickaxe") ||
				m.name().endsWith("_shovel") ||
				m.name().endsWith("_hoe")) {
			return false;
		}

		return !m.isArmor();
	}

}
