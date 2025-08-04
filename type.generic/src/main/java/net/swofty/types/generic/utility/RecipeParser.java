package net.swofty.types.generic.utility;

import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.item.ItemQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.crafting.ShapedRecipe;
import net.swofty.types.generic.item.crafting.ShapelessRecipe;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;
import net.swofty.types.generic.string.PlayerTemplateProcessor;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.tinylog.Logger;

import java.util.*;

public class RecipeParser {
    public static SkyBlockRecipe<?> parseRecipe(Map<String, Object> config) {
        String type = (String) config.get("type");
        String recipeType = (String) config.get("recipe-type");
        SkyBlockRecipe.RecipeType craftingType;

        try {
            craftingType = SkyBlockRecipe.RecipeType.valueOf(recipeType.toUpperCase());
        } catch (Exception e) {
            Logger.warn("Unknown recipe type enum '{}', defaulting to NONE", recipeType);
            craftingType = SkyBlockRecipe.RecipeType.NONE;
        }

        Map<String, Object> resultConfig = (Map<String, Object>) config.get("result");
        SkyBlockItem result = parseResult(resultConfig);
        List<RequirementCheck> requirements = parseRequirements((Map<String, Object>) config.get("requirements"));

        if (type.equalsIgnoreCase("shapeless")) {
            return parseShapelessRecipe(config, craftingType, result, requirements);
        } else if (type.equalsIgnoreCase("shaped")) {
            return parseShapedRecipe(config, craftingType, result, requirements);
        }

        throw new IllegalArgumentException("Invalid recipe type: " + type);
    }

    private record RequirementCheck(String leftVariable, String operation, String rightVariable, String failMessage) {
        public boolean check(SkyBlockPlayer player) {
            PlayerTemplateProcessor processor = new PlayerTemplateProcessor(player);
            String leftValue = processor.parseMessage(leftVariable).trim();
            String rightValue = processor.parseMessage(rightVariable).trim();

            try {
                double leftNumber = Double.parseDouble(leftValue);
                double rightNumber = Double.parseDouble(rightValue);

                return switch (operation) {
                    case ">=" -> leftNumber >= rightNumber;
                    case ">" -> leftNumber > rightNumber;
                    case "=" -> leftNumber == rightNumber;
                    case "<" -> leftNumber < rightNumber;
                    case "<=" -> leftNumber <= rightNumber;
                    case "!=" -> leftNumber != rightNumber;
                    default -> throw new IllegalArgumentException("Invalid operation: " + operation);
                };
            } catch (NumberFormatException e) {
                return switch (operation) {
                    case "=" -> leftValue.equals(rightValue);
                    case "!=" -> !leftValue.equals(rightValue);
                    case "contains" -> leftValue.contains(rightValue);
                    case "startswith" -> leftValue.startsWith(rightValue);
                    case "endswith" -> leftValue.endsWith(rightValue);
                    default -> throw new IllegalArgumentException("Invalid string operation: " + operation);
                };
            }
        }
    }

    private static List<RequirementCheck> parseRequirements(Map<String, Object> requirementsConfig) {
        if (requirementsConfig == null) return new ArrayList<>();

        List<RequirementCheck> requirements = new ArrayList<>();

        for (Map.Entry<String, Object> entry : requirementsConfig.entrySet()) {
            Map<String, Object> requirement = (Map<String, Object>) entry.getValue();

            String leftVar = (String) requirement.get("left");
            String operation = (String) requirement.get("operation");
            String rightVar = (String) requirement.get("right");
            String failMessage = (String) requirement.getOrDefault("fail-message",
                    "§cRequirement not met: " + entry.getKey());

            requirements.add(new RequirementCheck(leftVar, operation, rightVar, failMessage));
        }

        return requirements;
    }

    private static ShapelessRecipe parseShapelessRecipe(Map<String, Object> config,
                                                        SkyBlockRecipe.RecipeType craftingType,
                                                        SkyBlockItem result,
                                                        List<RequirementCheck> requirements) {
        List<Map<String, Object>> ingredients = (List<Map<String, Object>>) config.get("ingredients");
        int amount = (int) ((Map<String, Object>) config.get("result")).get("amount");
        result.setAmount(amount);

        ShapelessRecipe recipe = new ShapelessRecipe(craftingType, result, amount,
                (player) -> checkRequirements(player, requirements));

        recipe.init();

        for (Map<String, Object> ingredient : ingredients) {
            String materialType = (String) ingredient.get("type");
            int count = (int) ingredient.getOrDefault("amount", 1);

            if (materialType.startsWith("ITEM_TYPE_")) {
                recipe.add(ItemType.valueOf(materialType.substring(10)), count);
            } else {
                recipe.add(ItemType.valueOf(materialType), count);
            }
        }

        return recipe;
    }

    private static ShapedRecipe parseShapedRecipe(Map<String, Object> config,
                                                  SkyBlockRecipe.RecipeType craftingType,
                                                  SkyBlockItem result,
                                                  List<RequirementCheck> requirements) {
        Map<String, Map<String, Object>> ingredients = (Map<String, Map<String, Object>>) config.get("ingredients");
        List<String> pattern = (List<String>) config.get("pattern");

        Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>();

        for (Map.Entry<String, Map<String, Object>> entry : ingredients.entrySet()) {
            char key = entry.getKey().charAt(0);
            Map<String, Object> ingredient = entry.getValue();

            String materialType = (String) ingredient.get("type");
            int amount = (int) ingredient.getOrDefault("amount", 1);

            if (materialType.startsWith("ITEM_TYPE_")) {
                ingredientMap.put(key, new ItemQuantifiable(ItemType.valueOf(materialType.substring(10)), amount));
            } else {
                ingredientMap.put(key, new ItemQuantifiable(ItemType.valueOf(materialType), amount));
            }
        }

        result.setAmount((Integer) ((Map<String, Object>) config.get("result")).get("amount"));

        ShapedRecipe recipe = new ShapedRecipe(craftingType, result, ingredientMap, pattern,
                (player) -> checkRequirements(player, requirements));

        recipe.init();
        return recipe;
    }

    private static SkyBlockRecipe.CraftingResult checkRequirements(SkyBlockPlayer player, List<RequirementCheck> requirements) {
        if (requirements.isEmpty()) return new SkyBlockRecipe.CraftingResult(true, null);

        List<String> failureMessages = new ArrayList<>();
        boolean canCraft = true;

        for (RequirementCheck req : requirements) {
            if (!req.check(player)) {
                canCraft = false;
                failureMessages.add(req.failMessage());
            }
        }

        return new SkyBlockRecipe.CraftingResult(canCraft,
                canCraft ? null : failureMessages.toArray(new String[0]));
    }

    private static SkyBlockItem parseResult(Map<String, Object> resultConfig) {
        String type = (String) resultConfig.get("type");
        int amount = (int) resultConfig.getOrDefault("amount", 1);

        ItemType itemType = type.startsWith("ITEM_TYPE_")
                ? ItemType.valueOf(type.substring(10))
                : ItemType.valueOf(type);

        return new SkyBlockItem(itemType, amount);
    }
}
