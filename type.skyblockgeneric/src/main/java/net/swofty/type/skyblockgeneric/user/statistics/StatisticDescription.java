package net.swofty.type.skyblockgeneric.user.statistics;

import lombok.Getter;
import net.swofty.commons.skyblock.statistics.ItemStatistic;

import javax.annotation.Nullable;

@Getter
public enum StatisticDescription {
    FORAGING_FORTUNE(ItemStatistic.FORAGING_FORTUNE,
            ItemStatistic.FORAGING_FORTUNE.getFullDisplayName() + " §7increases how many drops you get from breaking wood. For every §61" + ItemStatistic.FORAGING_FORTUNE.getFullDisplayName() + "§7, you gain a §a1% §7chance for an additional drop."),
    FARMING_FORTUNE(ItemStatistic.FARMING_FORTUNE,
            ItemStatistic.FARMING_FORTUNE.getFullDisplayName() + " §7increases how many drops you get from breaking crops. For every §61" + ItemStatistic.FARMING_FORTUNE.getFullDisplayName() + "§7, you gain a §a1% §7chance for an additional drop."),
    ;

    private final ItemStatistic attachedStatistic;
    private final String description;

    StatisticDescription(ItemStatistic attachedStatistic, String description) {
        this.attachedStatistic = attachedStatistic;
        this.description = description;
    }

    public String getWikiName() {
        // Return name() but capitalise it properly, keeping underscores, e.g. FORAGING_FORTUNE -> Foraging_Fortune
        String[] parts = this.name().split("_");
        StringBuilder wikiName = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            wikiName.append(part.substring(0, 1).toUpperCase()).append(part.substring(1).toLowerCase());
            if (i < parts.length - 1) {
                wikiName.append("_");
            }
        }
        return wikiName.toString();
    }

    public static @Nullable StatisticDescription fromStatistic(ItemStatistic statistic) {
        for (StatisticDescription description : values()) {
            if (description.getAttachedStatistic() == statistic) {
                return description;
            }
        }
        return null;
    }
}
