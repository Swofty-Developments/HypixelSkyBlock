package net.swofty.anticheat.loader;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SwoftyValues {
    private boolean shouldPrint = true;
    private int ticksAllowedToMissPing = 40;
    private int tickLength = 50;
}
