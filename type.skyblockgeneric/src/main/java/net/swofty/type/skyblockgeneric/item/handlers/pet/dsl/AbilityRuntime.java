package net.swofty.type.skyblockgeneric.item.handlers.pet.dsl;

import lombok.Getter;
import lombok.Setter;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;

/**
 * Per-player runtime state for a single pet ability, keyed by the ability instance
 * in {@link net.swofty.type.skyblockgeneric.data.datapoints.DatapointPetData}.
 *
 * <p>Only handwritten {@link PetAbility} implementations can use this — they hold
 * {@code this} and call {@code player.getPetData().getAbilityRuntime(this)}.
 * DSL-built abilities ({@link PetDsl.Builder}) CANNOT: their handlers are static method
 * references with no reference to the owning ability instance, so there is no key to
 * look the runtime up by.
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
