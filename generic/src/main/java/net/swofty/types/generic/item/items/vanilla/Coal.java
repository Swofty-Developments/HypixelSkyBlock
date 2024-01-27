package net.swofty.types.generic.item.items.vanilla;

import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.SkillableMine;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class Coal implements SkillableMine, CustomSkyBlockItem {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public SkillCategories getSkillCategory() {
        return SkillCategories.MINING;
    }

    @Override
    public double getMiningValueForSkill() {
        return 5;
    }
}
