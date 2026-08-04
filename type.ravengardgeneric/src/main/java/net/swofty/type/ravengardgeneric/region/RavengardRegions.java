package net.swofty.type.ravengardgeneric.region;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ServerType;
import org.tinylog.Logger;

/**
 * Regions that ship with the server. Anything defined here is cached but never written to the
 * database, so a hand-drawn region of the same name from /ravengardregion always wins.
 */
public final class RavengardRegions {

    private RavengardRegions() {
    }

    public static void registerDefaults() {
        // The Nevermore is the ship the tutorial starts on; it spans the whole starting_world capture
        register("nevermore", RavengardRegionType.NEVERMORE,
                new Pos(-144, -64, 336), new Pos(191, 319, 671));
    }

    private static void register(String name, RavengardRegionType type, Pos first, Pos second) {
        if (RavengardRegion.getFromID(name) != null) {
            return;
        }
        new RavengardRegion(name, first, second, type, ServerType.RAVENGARD_LOBBY).cache();
        Logger.info("Registered built-in Ravengard region '{}' ({})", name, type.getDisplayName());
    }
}
