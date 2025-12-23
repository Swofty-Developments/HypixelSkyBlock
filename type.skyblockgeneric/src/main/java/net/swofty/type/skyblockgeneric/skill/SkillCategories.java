package net.swofty.type.skyblockgeneric.skill;

import net.swofty.commons.StringUtility;

public enum SkillCategories {
    COMBAT("combat"),
    FARMING("farming"),
    FISHING("fishing"),
    MINING("mining"),
    FORAGING("foraging"),
    ENCHANTING("enchanting"),
    RUNECRAFTING("runecrafting"),
    CARPENTRY("carpentry"),
    ALCHEMY("alchemy"),
    TAMING("taming"),
    ;

    private final String file;
    private SkillCategory category;

    SkillCategories(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return StringUtility.toNormalCase(name());
    }

    public SkillCategory asCategory() {
        if (category == null) {
            category = SkillLoader.loadFromFile(file);
        }
        return category;
    }
}
