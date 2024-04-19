package net.swofty.types.generic.item.items.foraging.vanilla;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkillableMine;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class Birch implements CustomSkyBlockItem, SkillableMine, Sellable {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
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
