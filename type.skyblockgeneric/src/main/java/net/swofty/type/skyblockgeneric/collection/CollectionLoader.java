package net.swofty.type.skyblockgeneric.collection;

import lombok.Data;
import org.tinylog.Logger;
import net.minestom.server.item.Material;
import org.tinylog.Logger;
import net.swofty.commons.YamlFileUtils;
import org.tinylog.Logger;
import net.swofty.commons.item.ItemType;
import org.tinylog.Logger;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import org.tinylog.Logger;
import net.swofty.type.skyblockgeneric.item.components.CraftableComponent;
import org.tinylog.Logger;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import org.tinylog.Logger;
import net.swofty.type.skyblockgeneric.utility.RecipeParser;
import org.tinylog.Logger;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;
import org.yaml.snakeyaml.Yaml;
import org.tinylog.Logger;

import java.io.File;
import org.tinylog.Logger;
import java.io.FileReader;
import org.tinylog.Logger;
import java.io.IOException;
import org.tinylog.Logger;
import java.util.ArrayList;
import org.tinylog.Logger;
import java.util.HashMap;
import org.tinylog.Logger;
import java.util.List;
import org.tinylog.Logger;
import java.util.Map;
import org.tinylog.Logger;

@Data
public class CollectionLoader {
    private static final File COLLECTIONS_DIR = new File("./configuration/skyblock/collections");

    @Data
    public static class CollectionConfig {
        public String name;
        public String displayIcon;
        public @Nullable String displayColor;
        public List<CollectionEntry> collections;
    }

    @Data
    public static class CollectionEntry {
        public String itemType;
        public List<CollectionReward> rewards;
    }

    @Data
    public static class CollectionReward {
        public int amount;
        public List<Reward> rewards;
    }

    @Data
    public static class Reward {
        public String type;
        public RewardData data;
    }

    @Data
    public static class RewardData {
        // Recipe data
        public String recipeType;
        public String craftingMaterial;
        public String resultType;
        public int resultAmount;
        public List<String> pattern;
        public Map<String, RecipeIngredient> ingredients;

        // Special unlock data
        public String unlockedItemType;
        public boolean isEnchantedRecipe;
        public boolean isMinionRecipes;

        // XP data
        public Integer xp;

        // Custom award data
        public String customAward;
    }

    @Data
    public static class RecipeIngredient {
        public String type;
        public int amount;
    }

    public static CollectionCategory loadFromFile(String name) {
        try {
            // Ensure directory exists
            if (!YamlFileUtils.ensureDirectoryExists(COLLECTIONS_DIR)) {
                throw new IOException("Failed to create collections directory");
            }

            File collectionFile = new File(COLLECTIONS_DIR, name.toLowerCase() + ".yml");

            // Load the YAML file
            Yaml yaml = new Yaml();
            CollectionConfig config = yaml.loadAs(new FileReader(collectionFile), CollectionConfig.class);

            return new CollectionCategory() {
                @Override
                public Material getDisplayIcon() {
                    return Material.values().stream()
                            .filter(material -> material.key().value().equalsIgnoreCase(config.displayIcon))
                            .findFirst()
                            .orElse(Material.AIR);
                }

                @Override
                public String getName() {
                    return (config.displayColor == null ? "" : "§" + config.displayColor) +
                            config.name;
                }

                @Override
                public ItemCollection[] getCollections() {
                    List<ItemCollection> collections = new ArrayList<>();
                    for (CollectionEntry entry : config.collections) {
                        ItemCollection collection = parseCollection(entry);
                        collections.add(collection);
                    }
                    return collections.toArray(new ItemCollection[0]);
                }
            };
        } catch (Exception e) {
            Logger.error(e, "Failed to load collection from file");
            return null;
        }
    }

    private static CollectionCategory.ItemCollection parseCollection(CollectionEntry entry) {
        ItemType type = ItemType.valueOf(entry.itemType);
        List<CollectionCategory.ItemCollectionReward> rewards = new ArrayList<>();

        for (CollectionReward reward : entry.rewards) {
            rewards.add(parseReward(reward));
        }

        return new CollectionCategory.ItemCollection(type, rewards.toArray(new CollectionCategory.ItemCollectionReward[0]));
    }

    private static CollectionCategory.ItemCollectionReward parseReward(CollectionReward reward) {
        List<CollectionCategory.Unlock> rewardTypes = new ArrayList<>();

        for (Reward r : reward.rewards) {
            switch (r.type.toUpperCase()) {
                case "RECIPE" -> rewardTypes.add(parseRecipe(r.data));
                case "SPECIAL_UNLOCK" -> rewardTypes.add(parseSpecialUnlock(r.data));
                case "XP" -> rewardTypes.add(new CollectionCategory.UnlockXP() {
                    @Override
                    public int xp() {
                        return r.data.xp;
                    }
                });
                case "CUSTOM_AWARD" -> rewardTypes.add(new CollectionCategory.UnlockCustomAward() {
                    @Override
                    public CustomCollectionAward getAward() {
                        return CustomCollectionAward.valueOf(r.data.customAward);
                    }
                });
            }
        }

        return new CollectionCategory.ItemCollectionReward(reward.amount, rewardTypes.toArray(new CollectionCategory.Unlock[0]));
    }

    private static CollectionCategory.UnlockRecipe parseRecipe(RewardData data) {
        return new CollectionCategory.UnlockRecipe() {
            @Override
            public SkyBlockRecipe<?> getRecipe() {
                // Convert RewardData to the Map format expected by RecipeParser
                Map<String, Object> config = convertToRecipeConfig(data);
                return RecipeParser.parseRecipe(config);
            }
        };
    }

    private static Map<String, Object> convertToRecipeConfig(RewardData data) {
        Map<String, Object> config = new HashMap<>();
        config.put("type", data.pattern != null ? "shaped" : "shapeless");
        config.put("recipe-type", data.recipeType);

        // Convert result
        Map<String, Object> result = new HashMap<>();
        if (data.craftingMaterial == null) {
            throw new RuntimeException("Crafting material is null for " + data.unlockedItemType);
        }
        result.put("type", data.craftingMaterial);
        result.put("amount", data.resultAmount);
        config.put("result", result);

        // Convert ingredients based on recipe type
        if (data.pattern != null) {
            config.put("pattern", data.pattern);
            config.put("ingredients", convertShapedIngredients(data.ingredients));
        } else {
            config.put("ingredients", convertShapelessIngredients(data.ingredients));
        }

        return config;
    }

    private static Map<String, Map<String, Object>> convertShapedIngredients(Map<String, RecipeIngredient> ingredients) {
        Map<String, Map<String, Object>> converted = new HashMap<>();

        for (Map.Entry<String, RecipeIngredient> entry : ingredients.entrySet()) {
            Map<String, Object> ingredient = new HashMap<>();
            ingredient.put("type", entry.getValue().type);
            ingredient.put("amount", entry.getValue().amount);
            converted.put(entry.getKey(), ingredient);
        }

        return converted;
    }

    private static List<Map<String, Object>> convertShapelessIngredients(Map<String, RecipeIngredient> ingredients) {
        List<Map<String, Object>> converted = new ArrayList<>();

        for (RecipeIngredient ingredient : ingredients.values()) {
            Map<String, Object> ingredientMap = new HashMap<>();
            ingredientMap.put("type", ingredient.type);
            ingredientMap.put("amount", ingredient.amount);
            converted.add(ingredientMap);
        }

        return converted;
    }

    private static CollectionCategory.UnlockRecipe parseSpecialUnlock(RewardData data) {
        return new CollectionCategory.UnlockRecipe() {
            @Override
            public SkyBlockRecipe<?> getRecipe() {
                return null;
            }

            @Override
            public List<SkyBlockRecipe<?>> getRecipes() {
                if (data.isMinionRecipes) {
                    SkyBlockItem item = new SkyBlockItem(data.unlockedItemType);
                    if (!item.hasComponent(CraftableComponent.class)) {
                        throw new RuntimeException("Item " + data.unlockedItemType + " does not have a craftable component");
                    }
                    CraftableComponent component = item.getComponent(CraftableComponent.class);
                    return component.getRecipes();
                } else if (data.isEnchantedRecipe) {
                    if (data.craftingMaterial == null) {
                        System.out.println("Crafting material is null for " + data.unlockedItemType);
                    }
                    return List.of(SkyBlockRecipe.getStandardEnchantedRecipe(
                            SkyBlockRecipe.RecipeType.valueOf(data.recipeType),
                            ItemType.valueOf(data.craftingMaterial),
                            ItemType.valueOf(data.unlockedItemType)
                    ));
                }
                return null;
            }
        };
    }
}