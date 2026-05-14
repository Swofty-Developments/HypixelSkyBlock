package net.swofty.type.skyblockgeneric.fishing.tag;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

/**
 * Central lookup mapping YAML tag ids to the typed FishingTag instances
 * the resolver uses. Built-in tags are pre-registered; custom tags (events,
 * project-specific regions) can be added via {@link #register}.
 */
public final class FishingTagRegistry {

    private static final Map<String, FishingTag> TAGS = new LinkedHashMap<>();

    static {
        registerAll(
                MediumTag.WATER,
                MediumTag.LAVA,
                TimeOfDayTag.NIGHT,
                TimeOfDayTag.DAY,
                TimeOfDayTag.DAWN,
                TimeOfDayTag.DUSK,
                WeatherTag.RAINING,
                WeatherTag.CLEAR,
                WeatherTag.THUNDERSTORM,
                RegionTag.CRIMSON_ISLE,
                RegionTag.BACKWATER_BAYOU,
                RegionTag.GALATEA,
                RegionTag.PRIVATE_ISLAND,
                RegionTag.HUB,
                RegionTag.THE_END,
                RarityTag.COMMON,
                RarityTag.UNCOMMON,
                RarityTag.RARE,
                RarityTag.EPIC,
                RarityTag.LEGENDARY,
                RarityTag.MYTHIC
        );
    }

    private FishingTagRegistry() {
    }

    public static void register(FishingTag tag) {
        TAGS.put(tag.id(), tag);
    }

    public static void registerAll(FishingTag... tags) {
        for (FishingTag tag : tags) {
            register(tag);
        }
    }

    public static @Nullable FishingTag get(String id) {
        return TAGS.get(id);
    }

    public static List<FishingTag> all() {
        return List.copyOf(TAGS.values());
    }

    public static Set<String> ids() {
        return Set.copyOf(TAGS.keySet());
    }

    /**
     * Resolves a list of YAML tag identifiers into typed tags. Unknown
     * identifiers are surfaced as informational {@link RegionTag} entries
     * (rather than dropped) so config typos remain visible in catalogs and
     * tag-bonus maps still match by id; the resolver simply treats them as
     * always-available.
     */
    public static List<FishingTag> resolve(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return ids.stream()
                .map(FishingTagRegistry::resolveOrSynth)
                .toList();
    }

    private static FishingTag resolveOrSynth(String id) {
        FishingTag known = TAGS.get(id);
        if (known != null) {
            return known;
        }
        Logger.debug("Unknown fishing tag id '{}', synthesising info-only tag", id);
        RegionTag synthetic = RegionTag.info(id);
        TAGS.put(id, synthetic);
        return synthetic;
    }

    public static Stream<FishingTag> stream() {
        return TAGS.values().stream();
    }
}
