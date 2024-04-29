package net.swofty.types.generic.user.statistics;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatisticDisplayReplacement {
    private String display;
    private int ticksToLast;
    private Purpose purpose;

    public enum DisplayType {
        MANA,
        DEFENSE,
        COINS,
    }

    public enum Purpose {
        SKILL,
        COLLECTION,
        MUSIC,
        ENCHANTMENT
    }
}
