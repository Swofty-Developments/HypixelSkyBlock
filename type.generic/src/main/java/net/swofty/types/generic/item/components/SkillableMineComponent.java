package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.skill.SkillCategories;

public class SkillableMineComponent extends SkyBlockItemComponent {
    @Getter
    private final SkillCategories category;
    @Getter
    private final double miningValue;

    public SkillableMineComponent(String category, double miningValue) {
        this.category = SkillCategories.valueOf(category);
        this.miningValue = miningValue;
    }
}