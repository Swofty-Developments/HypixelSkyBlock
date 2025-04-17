package net.swofty.types.generic.collection;

import lombok.Data;
import net.minestom.server.item.Material;
import net.swofty.commons.YamlFileUtils;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.item.ItemQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.CraftableComponent;
import net.swofty.types.generic.item.crafting.ShapedRecipe;
import net.swofty.types.generic.item.crafting.ShapelessRecipe;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CollectionLoader {
    private static final File COLLECTIONS_DIR = new File("./configuration/collections");

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
                            .filter(material -> material.namespace().value().equalsIgnoreCase(config.displayIcon))
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
            e.printStackTrace();
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
                if (data.pattern != null) {
                    // Shaped recipe
                    Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>();
                    for (Map.Entry<String, RecipeIngredient> entry : data.ingredients.entrySet()) {
                        char key = entry.getKey().charAt(0);
                        ItemType type = ItemType.valueOf(entry.getValue().type);
                        ingredientMap.put(key, new ItemQuantifiable(type, entry.getValue().amount));
                    }

                    return new ShapedRecipe(
                            SkyBlockRecipe.RecipeType.valueOf(data.recipeType),
                            new SkyBlockItem(ItemType.valueOf(data.resultType), data.resultAmount),
                            ingredientMap,
                            data.pattern
                    );
                } else {
                    // Shapeless recipe
                    ShapelessRecipe recipe = new ShapelessRecipe(
                            SkyBlockRecipe.RecipeType.valueOf(data.recipeType),
                            new SkyBlockItem(ItemType.valueOf(data.resultType)),
                            data.resultAmount
                    );

                    for (RecipeIngredient ingredient : data.ingredients.values()) {
                        recipe.add(ItemType.valueOf(ingredient.type), ingredient.amount);
                    }

                    return recipe;
                }
            }
        };
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
                    return List.of(SkyBlockRecipe.getStandardEnchantedRecipe(
                            SkyBlockRecipe.RecipeType.valueOf(data.recipeType),
                            ItemType.valueOf(data.resultType)
                    ));
                }
                return null;
            }
        };
    }
}