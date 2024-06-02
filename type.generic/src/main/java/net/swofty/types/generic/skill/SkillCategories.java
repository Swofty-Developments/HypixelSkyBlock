package net.swofty.types.generic.skill;

import net.swofty.types.generic.skill.skills.*;
import net.swofty.types.generic.utility.StringUtility;

public enum SkillCategories {
    COMBAT(CombatSkill.class),
    FARMING(FarmingSkill.class),
    FISHING(FishingSkill.class),
    MINING(MiningSkill.class),
    FORAGING(ForagingSkill.class),
    ENCHANTING(EnchantingSkill.class),
    RUNECRAFTING(RunecraftingSkill.class),
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
