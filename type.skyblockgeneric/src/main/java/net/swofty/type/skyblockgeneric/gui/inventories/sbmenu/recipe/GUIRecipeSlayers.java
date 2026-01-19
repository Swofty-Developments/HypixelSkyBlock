package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.item.crafting.ShapedRecipe;
import net.swofty.type.skyblockgeneric.item.crafting.ShapelessRecipe;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIRecipeSlayers extends StatelessView {

    private static final int[] CATEGORY_SLOTS = {20, 21, 22, 23, 24};

    private static final List<SkyBlockRecipe.RecipeType> SLAYER_TYPES = List.of(
            SkyBlockRecipe.RecipeType.REVENANT_HORROR,
            SkyBlockRecipe.RecipeType.TARANTULA_BROODFATHER,
            SkyBlockRecipe.RecipeType.SVEN_PACKMASTER,
            SkyBlockRecipe.RecipeType.VOIDGLOOM_SERAPH,
            SkyBlockRecipe.RecipeType.INFERNO_DEMONLORD
    );

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Slayer Recipes", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        ArrayList<SkyBlockRecipe> allRecipes = new ArrayList<>();
        allRecipes.addAll(ShapedRecipe.CACHED_RECIPES);
        allRecipes.addAll(ShapelessRecipe.CACHED_RECIPES);

        // Title item
        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();

            SkyBlockRecipe.RecipeType type = SkyBlockRecipe.RecipeType.SLAYER;

            ArrayList<SkyBlockRecipe> typeRecipes = new ArrayList<>();
            ArrayList<SkyBlockRecipe> allowedRecipes = new ArrayList<>();
            allRecipes.forEach(recipe -> {
                if (SLAYER_TYPES.contains(recipe.getRecipeType())) {
                    typeRecipes.add(recipe);
                }
            });

            ArrayList<String> lore = new ArrayList<>(Arrays.asList(
                    "§7View all of the " + StringUtility.toNormalCase(type.name()) + " Recipes",
                    "§7that you have unlocked!", " "));

            typeRecipes.forEach(recipe -> {
                SkyBlockRecipe.CraftingResult result =
                        (SkyBlockRecipe.CraftingResult) recipe.getCanCraft().apply(player);

                if (result.allowed()) {
                    allowedRecipes.add(recipe);
                }
            });

            String unlockedPercentage = String.format("%.2f", (allowedRecipes.size() / (double) typeRecipes.size()) * 100);
            lore.add("§7Slayer Recipes Unlocked: §e" + unlockedPercentage + "§6%");

            String baseLoadingBar = "─────────────────";
            int maxBarLength = baseLoadingBar.length();
            int completedLength = (int) ((allowedRecipes.size() / (double) typeRecipes.size()) * maxBarLength);

            String completedLoadingBar = "§2§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
            int formattingCodeLength = 4;
            String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                    completedLoadingBar.length() - formattingCodeLength,
                    maxBarLength
            ));

            lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §e" + allowedRecipes.size() + "§6/§e" + typeRecipes.size());

            return ItemStackCreator.getStack("§a" + StringUtility.toNormalCase(type.name()) + " Recipes",
                    type.getMaterial(), 1, lore);
        });

        // Category items
        for (int i = 0; i < CATEGORY_SLOTS.length && i < SLAYER_TYPES.size(); i++) {
            SkyBlockRecipe.RecipeType type = SLAYER_TYPES.get(i);
            int slot = CATEGORY_SLOTS[i];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();

                ArrayList<SkyBlockRecipe> typeRecipes = new ArrayList<>();
                ArrayList<SkyBlockRecipe> allowedRecipes = new ArrayList<>();
                allRecipes.forEach(recipe -> {
                    if (recipe.getRecipeType() == type) {
                        typeRecipes.add(recipe);
                    }
                });

                ArrayList<String> lore = new ArrayList<>(Arrays.asList(
                        "§7View all of the " + StringUtility.toNormalCase(type.name()) + " Recipes",
                        "§7that you have unlocked!", " "));

                typeRecipes.forEach(recipe -> {
                    SkyBlockRecipe.CraftingResult result =
                            (SkyBlockRecipe.CraftingResult) recipe.getCanCraft().apply(player);

                    if (result.allowed()) {
                        allowedRecipes.add(recipe);
                    }
                });

                String unlockedPercentage = String.format("%.2f", (allowedRecipes.size() / (double) typeRecipes.size()) * 100);
                lore.add("§7Recipes Unlocked: §e" + unlockedPercentage + "§6%");

                String baseLoadingBar = "─────────────────";
                int maxBarLength = baseLoadingBar.length();
                int completedLength = (int) ((allowedRecipes.size() / (double) typeRecipes.size()) * maxBarLength);

                String completedLoadingBar = "§2§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
                int formattingCodeLength = 4;
                String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                        completedLoadingBar.length() - formattingCodeLength,
                        maxBarLength
                ));

                lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §e" + allowedRecipes.size() + "§6/§e" + typeRecipes.size());
                lore.add(" ");
                lore.add("§eClick to view!");

                return ItemStackCreator.getStack("§a" + StringUtility.toNormalCase(type.name()) + " Recipes",
                        type.getMaterial(), 1, lore);
            }, (_, c) -> c.push(new GUIRecipeCategory(type), GUIRecipeCategory.createInitialState((SkyBlockPlayer) c.player(), type)));
        }
    }
}
