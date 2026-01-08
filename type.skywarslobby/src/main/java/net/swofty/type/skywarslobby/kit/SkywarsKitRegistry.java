package net.swofty.type.skywarslobby.kit;

import org.tinylog.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Registry for all SkyWars kits.
 * Loads kits from YAML configuration.
 */
public class SkywarsKitRegistry {
    private static final Map<String, SkywarsKit> KITS = new HashMap<>();
    private static boolean initialized = false;

    /**
     * Initialize the kit registry by loading from YAML file.
     */
    public static void initialize() {
        if (initialized) return;

        KITS.clear();

        List<SkywarsKit> loadedKits = SkywarsKitLoader.loadFromFile();
        for (SkywarsKit kit : loadedKits) {
            KITS.put(kit.getId(), kit);
        }

        Logger.info("Registered " + KITS.size() + " SkyWars kits");
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
     * Get a kit by its ID.
     */
    public static SkywarsKit getKit(String id) {
        ensureInitialized();
        return KITS.get(id);
    }

    /**
     * Get all registered kits.
     */
    public static List<SkywarsKit> getAllKits() {
        ensureInitialized();
        return new ArrayList<>(KITS.values());
    }

    /**
     * Get all kits available for a specific mode.
     * @param mode The mode (e.g., "NORMAL", "INSANE")
     */
    public static List<SkywarsKit> getKitsForMode(String mode) {
        ensureInitialized();
        return KITS.values().stream()
                .filter(kit -> kit.isAvailableFor(mode))
                .collect(Collectors.toList());
    }

    /**
     * Get all kits sorted by rarity (lowest first)
     */
    public static List<SkywarsKit> getKitsSortedByRarity(String mode, boolean lowestFirst) {
        List<SkywarsKit> kits = getKitsForMode(mode);
        kits.sort((a, b) -> {
            int comparison = Integer.compare(a.getRarity().getSortOrder(), b.getRarity().getSortOrder());
            return lowestFirst ? comparison : -comparison;
        });
        return kits;
    }

    /**
     * Get all default (free) kits.
     */
    public static List<SkywarsKit> getDefaultKits() {
        ensureInitialized();
        return KITS.values().stream()
                .filter(SkywarsKit::isDefault)
                .collect(Collectors.toList());
    }

    /**
     * Get all kits that can drop from Soul Well
     */
    public static List<SkywarsKit> getSoulWellKits() {
        ensureInitialized();
        return KITS.values().stream()
                .filter(SkywarsKit::isSoulWellDrop)
                .collect(Collectors.toList());
    }

    /**
     * Get all kits that can drop from Soul Well and are not owned by the player
     * @param ownedKitIds Set of kit IDs already owned by the player
     */
    public static List<SkywarsKit> getUnownedSoulWellKits(Set<String> ownedKitIds) {
        ensureInitialized();
        return KITS.values().stream()
                .filter(SkywarsKit::isSoulWellDrop)
                .filter(kit -> !ownedKitIds.contains(kit.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Get a random kit from Soul Well drops
     * @param ownedKitIds Set of kit IDs already owned by the player
     * @return Random kit, or null if all kits are owned
     */
    public static SkywarsKit getRandomSoulWellKit(Set<String> ownedKitIds) {
        List<SkywarsKit> unownedKits = getUnownedSoulWellKits(ownedKitIds);
        if (unownedKits.isEmpty()) {
            return null;
        }
        return unownedKits.get(new Random().nextInt(unownedKits.size()));
    }

    /**
     * Get all kits sorted by rarity with owned first option
     * @param mode The mode
     * @param lowestFirst Sort by lowest rarity first
     * @param ownedFirst Put owned kits at the top
     * @param ownedKitIds Set of kit IDs owned by the player
     */
    public static List<SkywarsKit> getKitsSortedByRarity(String mode, boolean lowestFirst,
                                                          boolean ownedFirst, Set<String> ownedKitIds) {
        List<SkywarsKit> kits = getKitsForMode(mode);
        kits.sort((a, b) -> {
            if (ownedFirst) {
                boolean aOwned = ownedKitIds.contains(a.getId());
                boolean bOwned = ownedKitIds.contains(b.getId());
                if (aOwned != bOwned) {
                    return aOwned ? -1 : 1;
                }
            }
            int comparison = Integer.compare(a.getRarity().getSortOrder(), b.getRarity().getSortOrder());
            return lowestFirst ? comparison : -comparison;
        });
        return kits;
    }

    /**
     * Get kits by rarity
     */
    public static List<SkywarsKit> getKitsByRarity(SkywarsKitRarity rarity) {
        ensureInitialized();
        return KITS.values().stream()
                .filter(kit -> kit.getRarity() == rarity)
                .collect(Collectors.toList());
    }

    /**
     * Check if a kit exists
     */
    public static boolean exists(String id) {
        ensureInitialized();
        return KITS.containsKey(id);
    }

    private static void ensureInitialized() {
        if (!initialized) {
            initialize();
        }
    }
}
