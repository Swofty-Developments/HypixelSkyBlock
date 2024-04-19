package net.swofty.types.generic.skill;

import lombok.Getter;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.skill.skills.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.utility.StringUtility;

public enum SkillCategories {
    COMBAT(CombatSkill.class, ItemStatistic.COMBAT_WISDOM),
    FARMING(FarmingSkill.class, ItemStatistic.FARMING_WISDOM),
    FISHING(FishingSkill.class, ItemStatistic.FISHING_WISDOM),
    MINING(MiningSkill.class, ItemStatistic.MINING_WISDOM),
    FORAGING(ForagingSkill.class, ItemStatistic.FORAGING_WISDOM),
    ENCHANTING(EnchantingSkill.class, ItemStatistic.ENCHANTING_WISDOM),
    ;

    private final Class<? extends SkillCategory> clazz;
    @Getter
    private final ItemStatistic wisdom;

    SkillCategories(Class<? extends SkillCategory> clazz, ItemStatistic wisdom) {
        this.clazz = clazz;
        this.wisdom = wisdom;
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
