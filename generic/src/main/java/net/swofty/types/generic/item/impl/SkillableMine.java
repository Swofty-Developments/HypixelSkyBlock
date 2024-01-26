package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.skill.SkillCategories;

public interface SkillableMine {
    SkillCategories getSkillCategory();
    double getMiningValueForSkill();
}
