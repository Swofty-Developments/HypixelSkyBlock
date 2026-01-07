package net.swofty.type.skywarslobby.perk;

import org.tinylog.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Registry for all SkyWars perks.
 * Loads perks from YAML configuration.
 */
public class SkywarsPerkRegistry {
    private static final Map<String, SkywarsPerk> PERKS = new HashMap<>();
    private static boolean initialized = false;

    /**
     * Initialize the perk registry by loading from YAML file.
     */
    public static void initialize() {
        if (initialized) return;

        PERKS.clear();

        List<SkywarsPerk> loadedPerks = SkywarsPerkLoader.loadFromFile();
        for (SkywarsPerk perk : loadedPerks) {
            PERKS.put(perk.getId(), perk);
        }

        Logger.info("Registered " + PERKS.size() + " SkyWars perks");
        initialized = true;
    }

    /**
     * Reinitialize the registry (useful for config reloading)
     */
    public static void reload() {
        initialized = false;
        initialize();
    }

    /**
     * Get a perk by its ID.
     */
    public static SkywarsPerk getPerk(String id) {
        ensureInitialized();
        return PERKS.get(id);
    }

    /**
     * Get all registered perks.
     */
    public static List<SkywarsPerk> getAllPerks() {
        ensureInitialized();
        return new ArrayList<>(PERKS.values());
    }

    /**
     * Get all perks available for a specific mode.
     * @param mode The mode (e.g., "NORMAL", "INSANE")
     */
    public static List<SkywarsPerk> getPerksForMode(String mode) {
        ensureInitialized();
        return PERKS.values().stream()
                .filter(perk -> perk.isAvailableFor(mode))
                .collect(Collectors.toList());
    }

    /**
     * Get all perks sorted by rarity (lowest first)
     */
    public static List<SkywarsPerk> getPerksSortedByRarity(String mode, boolean lowestFirst) {
        List<SkywarsPerk> perks = getPerksForMode(mode);
        perks.sort((a, b) -> {
            int comparison = Integer.compare(a.getRarity().getSortOrder(), b.getRarity().getSortOrder());
            return lowestFirst ? comparison : -comparison;
        });
        return perks;
    }

    /**
     * Get all perks that can drop from Soul Well
     */
    public static List<SkywarsPerk> getSoulWellPerks() {
        ensureInitialized();
        return PERKS.values().stream()
                .filter(SkywarsPerk::isSoulWellDrop)
                .collect(Collectors.toList());
    }

    /**
     * Get all perks that can drop from Soul Well and are not owned by the player
     * @param ownedPerkIds Set of perk IDs already owned by the player
     */
    public static List<SkywarsPerk> getUnownedSoulWellPerks(Set<String> ownedPerkIds) {
        ensureInitialized();
        return PERKS.values().stream()
                .filter(SkywarsPerk::isSoulWellDrop)
                .filter(perk -> !ownedPerkIds.contains(perk.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Get a random perk from Soul Well drops
     * @param ownedPerkIds Set of perk IDs already owned by the player
     * @return Random perk, or null if all perks are owned
     */
    public static SkywarsPerk getRandomSoulWellPerk(Set<String> ownedPerkIds) {
        List<SkywarsPerk> unownedPerks = getUnownedSoulWellPerks(ownedPerkIds);
        if (unownedPerks.isEmpty()) {
            return null;
        }
        return unownedPerks.get(new Random().nextInt(unownedPerks.size()));
    }

    /**
     * Get perks by rarity
     */
    public static List<SkywarsPerk> getPerksByRarity(SkywarsPerkRarity rarity) {
        ensureInitialized();
        return PERKS.values().stream()
                .filter(perk -> perk.getRarity() == rarity)
                .collect(Collectors.toList());
    }

    /**
     * Check if a perk exists
     */
    public static boolean exists(String id) {
        ensureInitialized();
        return PERKS.containsKey(id);
    }

    /**
     * Get all global perks (always active for everyone)
     */
    public static List<SkywarsPerk> getGlobalPerks() {
        ensureInitialized();
        return PERKS.values().stream()
                .filter(SkywarsPerk::isGlobal)
                .collect(Collectors.toList());
    }

    /**
     * Get all selectable perks for a mode (not global, not tournament exclusive)
     */
    public static List<SkywarsPerk> getSelectablePerksForMode(String mode) {
        ensureInitialized();
        return PERKS.values().stream()
                .filter(perk -> perk.isAvailableFor(mode))
                .filter(SkywarsPerk::isSelectable)
                .collect(Collectors.toList());
    }

    /**
     * Get selectable perks sorted by rarity
     */
    public static List<SkywarsPerk> getSelectablePerksSortedByRarity(String mode, boolean lowestFirst) {
        List<SkywarsPerk> perks = getSelectablePerksForMode(mode);
        perks.sort((a, b) -> {
            int comparison = Integer.compare(a.getRarity().getSortOrder(), b.getRarity().getSortOrder());
            return lowestFirst ? comparison : -comparison;
        });
        return perks;
    }

    private static void ensureInitialized() {
        if (!initialized) {
            initialize();
        }
    }
}
