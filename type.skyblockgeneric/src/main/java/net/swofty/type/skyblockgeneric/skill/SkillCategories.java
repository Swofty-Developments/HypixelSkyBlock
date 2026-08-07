package net.swofty.type.skyblockgeneric.skill;

import lombok.Getter;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.statistics.ItemStatistic;

public enum SkillCategories {
    COMBAT("combat", ItemStatistic.COMBAT_WISDOM),
    FARMING("farming", ItemStatistic.FARMING_WISDOM),
    FISHING("fishing", ItemStatistic.FISHING_WISDOM),
    MINING("mining", ItemStatistic.MINING_WISDOM),
    FORAGING("foraging", ItemStatistic.FORAGING_WISDOM),
    ENCHANTING("enchanting", ItemStatistic.ENCHANTING_WISDOM),
    RUNECRAFTING("runecrafting", ItemStatistic.RUNE_CRAFTING_WISDOM),
    CARPENTRY("carpentry", ItemStatistic.CARPENTRY_WISDOM),
    ALCHEMY("alchemy", ItemStatistic.ALCHEMY_WISDOM),
    TAMING("taming", ItemStatistic.TAMING_WISDOM),
    HUNTING("hunting", ItemStatistic.HUNTING_WISDOM),
    ;

    private final String file;
    @Getter
    private final ItemStatistic wisdomStatistic;
    private SkillCategory category;

    SkillCategories(String file, ItemStatistic wisdomStatistic) {
        this.file = file;
        this.wisdomStatistic = wisdomStatistic;
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
