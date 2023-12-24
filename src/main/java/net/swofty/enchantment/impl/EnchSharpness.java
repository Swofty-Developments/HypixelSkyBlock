package net.swofty.enchantment.impl;

import lombok.Getter;
import net.swofty.enchantment.EnchFromTable;
import net.swofty.enchantment.EnchantmentSource;
import net.swofty.utility.ItemGroups;

import java.util.List;

@Getter
public class EnchSharpness implements EnchFromTable {
	
	public static final int[] increases = new int[] {5, 10, 15, 20, 30, 45, 65};
	
	public final int[] levelsToApply = new int[] {9, 14, 18, 23, 27, 91, 179};
	private final int[] levelsFromTableToApply = new int[] {10, 15, 20, 25, 30};
	public final List<EnchantmentSource> sources = List.of(
		new EnchantmentSource(EnchantmentSource.SourceType.ENCHANTMENT_TABLE, 1, 5),
		new EnchantmentSource("Gravel Collection", 4, 5),
		new EnchantmentSource(EnchantmentSource.SourceType.DARK_AUCTION, 6, 6),
		new EnchantmentSource(EnchantmentSource.SourceType.EXPERIMENTS, 6, 7),
		new EnchantmentSource(EnchantmentSource.SourceType.BAZAAR, 6, 7),
		new EnchantmentSource(EnchantmentSource.SourceType.SCORPIUS, 7, 7)
	);
	public final List<ItemGroups> groups = List.of(ItemGroups.SWORD, ItemGroups.FISHING_WEAPON, ItemGroups.LONG_SWORD, ItemGroups.GAUNTLET);
	
	@Override
	public String getDescription(int level) {
		return "Increases melee damage dealt by §a"+ increases[level-1] +"%§7.";
	}
	
	@Override
	public int getRequiredEnchantingLevel() {
		return 0;
	}
	
	@Override
	public int getRequiredBookshelfPower() {
		return 0;
	}
	
}
