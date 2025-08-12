package net.swofty.type.skyblockgeneric.skill;

import net.swofty.commons.StringUtility;
import net.swofty.type.generic.skill.skills.*;

public enum SkillCategories {
    COMBAT(CombatSkill.class),
    FARMING(FarmingSkill.class),
    FISHING(FishingSkill.class),
    MINING(MiningSkill.class),
    FORAGING(ForagingSkill.class),
    ENCHANTING(EnchantingSkill.class),
    RUNECRAFTING(RunecraftingSkill.class),
    CARPENTRY(CarpentrySkill.class),
    ;

    private final Class<? extends SkillCategory> clazz;

    SkillCategories(Class<? extends SkillCategory> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return StringUtility.toNormalCase(name());
    }

    public SkillCategory asCategory() {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Failed to instantiate SkillCategory");
    }
}
