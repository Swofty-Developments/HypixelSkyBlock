package net.swofty.user.statistics;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ManaDisplayReplacement {
    private String display;
    private int ticksToLast;
}
