package net.swofty.enchantment;

import lombok.Getter;
import lombok.SneakyThrows;
import net.swofty.enchantment.impl.EnchSharpness;

import java.util.List;

/**
 * Note: Maybe make a new system to replace the enumeration and put every fields needed in the {@link Ench} class, or minimize its use
 */
@Getter
public enum EnchantmentType {
	SHARPNESS(EnchSharpness.class),
	/*EFFICIENCY("§7Increases how quickly your tool breaks blocks.",
		0, 0,
		List.of(),
		List.of(new EnchantmentSource(SourceType.ENCHANTMENT_TABLE, 1, 5),
			new EnchantmentSource("Redstone Collection", 4, 5)),
		new Integer[] {9, 14, 18, 23, 27},
		ItemGroups.TOOLS
	),*/
	;
	
	private final Ench ench;
	private final List<EnchantmentType> conflicts;
	
	@SneakyThrows
	EnchantmentType(Class<? extends Ench> ench, EnchantmentType... conflicts) {
		this.ench = (Ench) ench.getConstructor().newInstance();
		this.conflicts = List.of(conflicts);
	}
	
	public int getApplyCost(int level) {
		if (level < 1 || level > ench.getLevelsToApply().length)
			throw new IllegalArgumentException("level cannot be less than 1 and more than "+ ench.getLevelsToApply().length +" for "+ name());
		return ench.getLevelsToApply()[level-1];
	}
	
	/**
	 * To use if you know it is applicable from the enchanting table
	 */
	public int getApplyCostFromTable(int level) {
		EnchFromTable e = getAsFromTable();
		if (level < 1 || level > e.getLevelsFromTableToApply().length)
			throw new IllegalArgumentException("level cannot be less than 1 and more than "+ e.getLevelsFromTableToApply().length +" for "+ name());
		return e.getLevelsFromTableToApply()[level-1];
	}
	
	public String getDescription(int level) {
		// here so it's not needed to include it every time in the statements
		if(level < 1 || level > ench.getLevelsToApply().length)
			throw new IllegalArgumentException("level cannot be less than 1 and more than "+ ench.getLevelsToApply().length +" for "+ name());
		return ench.getDescription(level);
	}
	
	@SneakyThrows
	public EnchFromTable getAsFromTable() {
		return (EnchFromTable) ench;
	}
	
}
