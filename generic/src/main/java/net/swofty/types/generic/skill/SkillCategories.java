package net.swofty.types.generic.skill;

import lombok.Getter;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.skill.skills.*;
import net.swofty.types.generic.utility.StringUtility;

public enum SkillCategories {
    COMBAT(CombatSkill.class),
    FARMING(FarmingSkill.class),
    FISHING(FishingSkill.class),
    MINING(MiningSkill.class),
    FORAGING(ForagingSkill.class),
    ENCHANTING(EnchantingSkill.class),
    ALCHEMY(AlchemySkill.class),
    CARPENTRY(),
    RUNECRAFTING(),
    SOCIAL(),
    TAMING(),
    DUNGEONERRING(),
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

        return null;
    }
}
