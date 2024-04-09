package net.swofty.types.generic.item.items.skill.foraging;

import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkillableMine;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class Acacia implements CustomSkyBlockItem, SkillableMine, Sellable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public double getSellValue() {
        return 2;
    }

    @Override
    public SkillCategories getSkillCategory() {
        return SkillCategories.FORAGING;
    }

    @Override
    public double getMiningValueForSkill() {
        return 6;
    }
}
