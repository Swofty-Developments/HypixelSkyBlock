package net.swofty.types.generic.enchantment;

import lombok.Getter;
import lombok.SneakyThrows;
import net.swofty.types.generic.enchantment.abstr.Ench;
import net.swofty.types.generic.enchantment.abstr.EnchFromTable;
import net.swofty.types.generic.enchantment.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public enum EnchantmentType {
    SHARPNESS(EnchantmentSharpness.class),
    EFFICIENCY(EnchantmentEfficiency.class),
    SCAVENGER(EnchantmentScavenger.class),
    PROTECTION(EnchantmentProtection.class),
    CRITICAL(EnchantmentCritical.class),
    FIRST_STRIKE(EnchantmentFirstStrike.class),
    GROWTH(EnchantmentGrowth.class),
    LUCK(EnchantmentLuck.class),
    ENDER_SLAYER(EnchantmentEnderSlayer.class),
    SMITE(EnchantmentSmite.class),
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

    public int getApplyCost(int level, SkyBlockPlayer player) {
        if (level < 1 || level > ench.getLevelsToApply(player).maximumLevel())
            throw new IllegalArgumentException("level cannot be less than 1 and more than " +
                    ench.getLevelsToApply(player).maximumLevel() + " for " + name());
        return ench.getLevelsToApply(player).get(level);
    }

    public String getDescription(int level, SkyBlockPlayer player) {
        if (level < 1 || level > ench.getLevelsToApply(player).maximumLevel())
            return ("level cannot be less than 1 and more than " +
                    ench.getLevelsToApply(player).maximumLevel() + " for " + name());
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
