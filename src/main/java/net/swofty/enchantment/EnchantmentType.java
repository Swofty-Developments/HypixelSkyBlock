package net.swofty.enchantment;

import lombok.Getter;
import lombok.SneakyThrows;
import net.swofty.enchantment.abstr.Ench;
import net.swofty.enchantment.abstr.EnchFromTable;
import net.swofty.enchantment.impl.EnchantmentEfficiency;
import net.swofty.enchantment.impl.EnchantmentSharpness;
import net.swofty.utility.StringUtility;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Note: Maybe make a new system to replace the enumeration and put every fields needed in the {@link Ench} class, or minimize its use
 */
@Getter
public enum EnchantmentType {
	SHARPNESS(EnchantmentSharpness.class),
	EFFICIENCY(EnchantmentEfficiency.class),
	;
	
	private final Class<? extends Ench> clazz;
	private final List<EnchantmentType> conflicts;

	private final Ench ench;
	
	@SneakyThrows
	EnchantmentType(Class<? extends Ench> ench, EnchantmentType... conflicts) {
		this.clazz = ench;
		this.conflicts = List.of(conflicts);

		this.ench = ench.getConstructor().newInstance();
	}
	
	public int getApplyCost(int level) {
		if (level < 1 || level > ench.getLevelsToApply().maximumLevel())
			throw new IllegalArgumentException("level cannot be less than 1 and more than " +
					ench.getLevelsToApply().maximumLevel() + " for "+ name());
		return ench.getLevelsToApply().get(level);
	}
	
	public String getDescription(int level) {
		if (level < 1 || level > ench.getLevelsToApply().maximumLevel())
			throw new IllegalArgumentException("level cannot be less than 1 and more than " +
					ench.getLevelsToApply().maximumLevel() + " for "+ name());
		return ench.getDescription(level);
	}

	public @Nullable EnchFromTable getEnchFromTable() {
		if (ench instanceof EnchFromTable)
			return (EnchFromTable) ench;
		return null;
	}

	public String getName() {
		return StringUtility.toNormalCase(this.name());
	}
}
