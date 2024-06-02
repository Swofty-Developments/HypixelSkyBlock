package net.swofty.types.generic.item.items.mining.vanilla;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.ExtraUnderNameDisplay;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkillableMine;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class GoldIngot implements CustomSkyBlockItem, SkillableMine, Sellable, ExtraUnderNameDisplay {
    @Override
    public SkillCategories getSkillCategory() {
        return SkillCategories.MINING;
    }

    @Override
    public double getMiningValueForSkill() {
        return 5;
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Brewing Ingredient";
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 4;
    }
}

