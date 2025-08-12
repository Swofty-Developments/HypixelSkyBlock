package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;

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