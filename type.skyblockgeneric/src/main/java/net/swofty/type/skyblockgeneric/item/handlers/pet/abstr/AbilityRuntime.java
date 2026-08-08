package net.swofty.type.skyblockgeneric.item.handlers.pet.abstr;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class AbilityRuntime {
    private long buffUntil;   // millis when a timed buff expires; active while now < buffUntil
    private long lastProc;    // millis of last trigger; cooldown = now - lastProc
    private int stacks;       // stacking count (Web Battlefield, kill combo)
    private int hits;         // counter (Clubbed Tail every 5th hit)
    private boolean pending;  // one-shot flag (Rolling Miner next gemstone 2x)
}
