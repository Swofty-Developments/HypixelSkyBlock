package net.swofty.enchantment;

import net.swofty.user.statistics.ItemStatistics;
import net.swofty.utility.ItemGroups;

import java.util.List;

public interface Ench {
	
	String getDescription(int level);
	int getRequiredEnchantingLevel();
	int[] getLevelsToApply();
	List<EnchantmentSource> getSources();
	List<ItemGroups> getGroups();
	
	/**
	 * TODO add parameters + never used
	 */
	default void onAttack() {};
	/**
	 * TODO add parameters + never used
	 */
	default void onDamaged() {};
	/**
	 * TODO never used
	 */
	default ItemStatistics getStatistics() {
		return ItemStatistics.EMPTY;
	}
	
}
