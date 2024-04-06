package net.swofty.types.generic.item.items.vanilla;

import net.swofty.types.generic.item.impl.BlockImpl;
import net.swofty.types.generic.item.impl.SkillableMine;
import net.swofty.types.generic.skill.SkillCategories;

public class Cobblestone implements BlockImpl, SkillableMine {

    @Override
    public SkillCategories getSkillCategory() {
        return SkillCategories.MINING;
    }

    @Override
    public double getMiningValueForSkill() {
        return 1;
    }
}
