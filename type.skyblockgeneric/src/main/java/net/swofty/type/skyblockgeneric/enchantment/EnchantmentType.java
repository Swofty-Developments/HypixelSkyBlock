package net.swofty.type.skyblockgeneric.enchantment;

import lombok.Getter;
import lombok.SneakyThrows;
import net.swofty.commons.StringUtility;
import net.swofty.type.skyblockgeneric.enchantment.abstr.Ench;
import net.swofty.type.skyblockgeneric.enchantment.abstr.EnchFromTable;
import net.swofty.type.skyblockgeneric.enchantment.impl.*;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
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
    LOOTING(EnchantmentLooting.class),
    ENDER_SLAYER(EnchantmentEnderSlayer.class),
    SMITE(EnchantmentSmite.class),
    SILK_TOUCH(EnchantmentSilkTouch.class),
    SMELTING_TOUCH(EnchantmentSmeltingTouch.class),
    GIANT_KILLER(EnchantmentGiantKiller.class),
    EXECUTE(EnchantmentExecute.class),
    IMPALING(EnchantmentImpaling.class),
    BANE_OF_ARTHROPODS(EnchantmentBaneOfArthropods.class),
    CUBISM(EnchantmentCubism.class),
    FORTUNE(EnchantmentFortune.class),
    ;

    private final Class<? extends Ench> clazz;
    private final Ench ench;

    @SneakyThrows
    EnchantmentType(Class<? extends Ench> ench) {
        this.clazz = ench;

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
