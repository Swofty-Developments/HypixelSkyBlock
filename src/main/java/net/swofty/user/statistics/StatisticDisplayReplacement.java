package net.swofty.user.statistics;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatisticDisplayReplacement {
    private String display;
    private int ticksToLast;

    public enum DisplayType {
        MANA,
        DEFENSE
    }
}
