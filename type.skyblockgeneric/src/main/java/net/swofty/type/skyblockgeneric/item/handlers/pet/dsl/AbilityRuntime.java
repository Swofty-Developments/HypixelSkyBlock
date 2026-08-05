package net.swofty.type.skyblockgeneric.item.handlers.pet.dsl;

import lombok.Getter;
import lombok.Setter;

/**
 * Per-player runtime state for a single pet ability, keyed by the ability instance
 * in {@link net.swofty.type.skyblockgeneric.data.datapoints.DatapointPetData}.
 *
 * <p>The DSL passes the runtime to the {@code BiConsumer<AbilityRuntime, E>} overloads
 * of {@code on(...)} and {@code statistics(...)}. State lives per player, so the shared
 * ability instance never leaks state across players.
 */
@Getter
@Setter
public final class AbilityRuntime {
    private long buffUntil;   // millis when a timed buff expires; active while now < buffUntil
    private long lastProc;    // millis of last trigger; cooldown = now - lastProc
    private int stacks;       // stacking count (Web Battlefield, kill combo)
    private int hits;         // counter (Clubbed Tail every 5th hit)
    private boolean pending;  // one-shot flag (Rolling Miner next gemstone 2x)
}
