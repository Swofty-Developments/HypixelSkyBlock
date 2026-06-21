package net.swofty.type.skyblockgeneric.fishing.tag;

import java.util.function.BooleanSupplier;
import net.swofty.type.skyblockgeneric.fishing.FishingContext;

/**
 * Gates loot behind a server-wide event being active (Spooky Festival,
 * Fishing Festival, etc.). The actual liveness check is supplied at
 * construction so the tag layer doesn't depend on event-specific code.
 */
public record EventTag(String id, BooleanSupplier activeCheck) implements FishingTag {

    public static EventTag of(String id, BooleanSupplier activeCheck) {
        return new EventTag(id, activeCheck);
    }

    /** Convenience: a never-active event tag for entries pending implementation. */
    public static EventTag dormant(String id) {
        return new EventTag(id, () -> false);
    }

    @Override
    public boolean isAvailable(FishingContext context) {
        return activeCheck.getAsBoolean();
    }
}
