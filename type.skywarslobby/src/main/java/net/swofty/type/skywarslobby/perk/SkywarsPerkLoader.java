package net.swofty.type.skywarslobby.perk;

import lombok.Data;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.YamlFileUtils;
import org.tinylog.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Loader for SkyWars perk configuration from YAML files.
 */
public class SkywarsPerkLoader {
    private static final File PERKS_DIR = new File("./configuration/skywars");

    @Data
    public static class PerksConfig {
        public Map<String, PerkEntry> perks;
    }

    @Data
    public static class PerkEntry {
        public String name;
        public String material;
        public String rarity = "COMMON";
        public int cost = 0;
        public int opalCost = 0;
        public boolean soulWellDrop = true;
        public boolean global = false;
        public boolean angelsDescentOnly = false;
        public boolean tournamentExclusive = false;
        public String description;
        public String effectDescription;
        public List<String> modes;
        public List<ItemEntry> startingItems;
        // Optional effect fields - parsed but handled by game logic
        public List<EffectEntry> effects;
        public Integer recoveryChance;
        public Integer igniteChance;
        public Integer saviorChance;
        public Integer witherChance;
        public KillRewardEntry killReward;
    }

    @Data
    public static class ItemEntry {
        public String material;
        public int amount = 1;
    }

    @Data
    public static class EffectEntry {
        public String type;
        public int duration = 0;
        public int amplifier = 0;
    }

    @Data
    public static class KillRewardEntry {
        public String effect;
        public int duration = 0;
        public int amplifier = 0;
    }

    /**
     * Load SkyWars perks from the YAML file
     */
    public static List<SkywarsPerk> loadFromFile() {
        List<SkywarsPerk> perks = new ArrayList<>();

        try {
            if (!YamlFileUtils.ensureDirectoryExists(PERKS_DIR)) {
                throw new IOException("Failed to create perks directory");
            }

            File perksFile = new File(PERKS_DIR, "perks.yml");

            if (!perksFile.exists()) {
                Logger.warn("Perks file not found at: " + perksFile.getAbsolutePath());
                return getDefaultPerks();
            }

            Yaml yaml = new Yaml();
            PerksConfig config = yaml.loadAs(new FileReader(perksFile), PerksConfig.class);

            if (config == null || config.perks == null) {
                Logger.warn("Invalid perks.yml structure");
                return getDefaultPerks();
            }

            for (Map.Entry<String, PerkEntry> entry : config.perks.entrySet()) {
                String id = entry.getKey();
                PerkEntry perkEntry = entry.getValue();

                SkywarsPerk perk = parsePerk(id, perkEntry);
                if (perk != null) {
                    perks.add(perk);
                }
            }

            Logger.info("Loaded " + perks.size() + " SkyWars perks from configuration");

        } catch (Exception e) {
            Logger.error(e, "Failed to load SkyWars perks from file");
            return getDefaultPerks();
        }

        return perks;
    }

    /**
     * Parse a single perk entry
     */
    private static SkywarsPerk parsePerk(String id, PerkEntry entry) {
        if (entry == null || entry.name == null) {
            return null;
        }

        Material iconMaterial = parseMaterial(entry.material);
        SkywarsPerkRarity rarity = parseRarity(entry.rarity);
        Set<String> modes = entry.modes != null ? new HashSet<>(entry.modes) : Set.of("NORMAL", "INSANE");
        List<ItemStack> startingItems = parseItems(entry.startingItems);

        return new SkywarsPerk(
                id,
                entry.name,
                iconMaterial,
                rarity,
                entry.cost,
                entry.opalCost,
                entry.soulWellDrop,
                entry.global,
                entry.angelsDescentOnly,
                entry.tournamentExclusive,
                entry.description != null ? entry.description : "",
                entry.effectDescription != null ? entry.effectDescription : "",
                modes,
                startingItems
        );
    }

    /**
     * Parse items list from YAML config
     */
    private static List<ItemStack> parseItems(List<ItemEntry> itemEntries) {
        List<ItemStack> items = new ArrayList<>();

        if (itemEntries == null) {
            return items;
        }

        for (ItemEntry entry : itemEntries) {
            if (entry.material != null) {
                Material material = parseMaterial(entry.material);
                items.add(ItemStack.of(material, entry.amount));
            }
        }

        return items;
    }

    /**
     * Parse material string to Material enum
     */
    private static Material parseMaterial(String materialStr) {
        if (materialStr == null) {
            return Material.STONE;
        }

        // Minestom uses key().value() which returns lowercase like "oak_planks"
        String normalizedStr = materialStr.toLowerCase().replace("-", "_");
        return Material.values().stream()
                .filter(m -> m.key().value().equalsIgnoreCase(normalizedStr))
                .findFirst()
                .orElse(Material.STONE);
    }

    /**
     * Parse rarity string to enum
     */
    private static SkywarsPerkRarity parseRarity(String rarityStr) {
        if (rarityStr == null) {
            return SkywarsPerkRarity.COMMON;
        }

        try {
            return SkywarsPerkRarity.valueOf(rarityStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SkywarsPerkRarity.COMMON;
        }
    }

    /**
     * Get default perks if file loading fails
     */
    private static List<SkywarsPerk> getDefaultPerks() {
        List<SkywarsPerk> defaults = new ArrayList<>();

        defaults.add(new SkywarsPerk(
                "bridger",
                "Bridger",
                Material.OAK_PLANKS,
                SkywarsPerkRarity.COMMON,
                0,
                0,
                true,
                false,
                false,
                false,
                "Block conservation",
                "50% chance to not consume blocks when placing them",
                Set.of("NORMAL", "INSANE"),
                new ArrayList<>()
        ));

        defaults.add(new SkywarsPerk(
                "lucky_charm",
                "Lucky Charm",
                Material.RABBIT_FOOT,
                SkywarsPerkRarity.COMMON,
                0,
                0,
                true,
                false,
                false,
                false,
                "Golden apple chance on kill",
                "30% chance to get a Golden Apple on kill",
                Set.of("NORMAL", "INSANE"),
                new ArrayList<>()
        ));

        return defaults;
    }
}
