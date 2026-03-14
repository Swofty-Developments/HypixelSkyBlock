package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.HypixelSignGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.item.crafting.ShapedRecipe;
import net.swofty.type.skyblockgeneric.item.crafting.ShapelessRecipe;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GUIRecipeBook extends StatelessView {
    private static final int[] CATEGORY_SLOTS = {
            20, 21, 22, 23, 24,
            29, 30, 31, 33
    };

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(I18n.string("gui_sbmenu.recipe.book.title"), InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        ArrayList<SkyBlockRecipe> allRecipes = new ArrayList<>();
        allRecipes.addAll(ShapedRecipe.CACHED_RECIPES);
        allRecipes.addAll(ShapelessRecipe.CACHED_RECIPES);

        layout.slot(51, (_, _) -> ItemStackCreator.getStack(I18n.string("gui_sbmenu.recipe.book.search"), Material.OAK_SIGN, 1,
                I18n.lore("gui_sbmenu.recipe.book.search.lore")), (_, c) -> {
            new HypixelSignGUI(c.player()).open(new String[]{"Enter query", ""}).thenAccept(line -> {
                if (line == null) {
                    return;
                }

                c.push(new GUISearchRecipe(), GUISearchRecipe.createInitialState(line));
            });
        });

        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            StringBuilder missionDisplay = new StringBuilder();
            List<String> missionLore = new ArrayList<>();
            SkyBlockRecipe.getMissionDisplay(missionLore, player.getUuid());
            for (int j = 0; j < missionLore.size(); j++) {
                missionDisplay.append(missionLore.get(j));
                if (j < missionLore.size() - 1) missionDisplay.append("\n");
            }
            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.recipe.book.info"), Material.BOOK, 1,
                    I18n.lore("gui_sbmenu.recipe.book.info.lore", Map.of("mission_display", missionDisplay.toString())));
        });

        for (int i = 0; i < CATEGORY_SLOTS.length && i < SkyBlockRecipe.RecipeType.values().length; i++) {
            SkyBlockRecipe.RecipeType type = SkyBlockRecipe.RecipeType.values()[i];
            int slot = CATEGORY_SLOTS[i];

            ArrayList<SkyBlockRecipe> typeRecipes = new ArrayList<>();
            allRecipes.forEach(recipe -> {
                if (recipe.getRecipeType() == type) {
                    typeRecipes.add(recipe);
                }
            });

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                ArrayList<SkyBlockRecipe> allowedRecipes = new ArrayList<>();

                typeRecipes.forEach(recipe -> {
                    SkyBlockRecipe.CraftingResult result = (SkyBlockRecipe.CraftingResult) recipe.getCanCraft().apply(player);
                    if (result.allowed()) {
                        allowedRecipes.add(recipe);
                    }
                });

                String unlockedPercentage = String.format("%.2f", (allowedRecipes.size() / (double) typeRecipes.size()) * 100);
                String baseLoadingBar = "─────────────────";
                int maxBarLength = baseLoadingBar.length();
                int completedLength = (int) ((allowedRecipes.size() / (double) typeRecipes.size()) * maxBarLength);
                String completedLoadingBar = "§2§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
                int formattingCodeLength = 4;
                String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                        completedLoadingBar.length() - formattingCodeLength, maxBarLength));
                String progressBar = completedLoadingBar + uncompletedLoadingBar + "§r §e" + allowedRecipes.size() + "§6/§e" + typeRecipes.size();

                String categoryName = StringUtility.toNormalCase(type.name());
                return ItemStackCreator.getStack(I18n.string("gui_sbmenu.recipe.book.category", Map.of("category_name", categoryName)),
                        type.getMaterial(), 1,
                        I18n.lore("gui_sbmenu.recipe.book.category.lore", Map.of("category_name", categoryName, "percent", unlockedPercentage, "progress_bar", progressBar)));
            }, (_, c) -> c.push(new GUIRecipeCategory(type), GUIRecipeCategory.createInitialState((SkyBlockPlayer) c.player(), type)));
        }

        // Slayer recipes
        layout.slot(32, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockRecipe.RecipeType type = SkyBlockRecipe.RecipeType.SLAYER;

            ArrayList<SkyBlockRecipe.RecipeType> recipeTypes = new ArrayList<>();
            recipeTypes.add(SkyBlockRecipe.RecipeType.REVENANT_HORROR);
            recipeTypes.add(SkyBlockRecipe.RecipeType.TARANTULA_BROODFATHER);
            recipeTypes.add(SkyBlockRecipe.RecipeType.SVEN_PACKMASTER);
            recipeTypes.add(SkyBlockRecipe.RecipeType.VOIDGLOOM_SERAPH);
            recipeTypes.add(SkyBlockRecipe.RecipeType.INFERNO_DEMONLORD);

            ArrayList<SkyBlockRecipe> typeRecipes = new ArrayList<>();
            ArrayList<SkyBlockRecipe> allowedRecipes = new ArrayList<>();
            allRecipes.forEach(recipe -> {
                if (recipeTypes.contains(recipe.getRecipeType())) {
                    typeRecipes.add(recipe);
                }
            });

            typeRecipes.forEach(recipe -> {
                SkyBlockRecipe.CraftingResult result = (SkyBlockRecipe.CraftingResult) recipe.getCanCraft().apply(player);
                if (result.allowed()) {
                    allowedRecipes.add(recipe);
                }
            });

            String unlockedPercentage = String.format("%.2f", (allowedRecipes.size() / (double) typeRecipes.size()) * 100);
            String categoryName = StringUtility.toNormalCase(type.name());

            String baseLoadingBar = "─────────────────";
            int maxBarLength = baseLoadingBar.length();
            int completedLength = (int) ((allowedRecipes.size() / (double) typeRecipes.size()) * maxBarLength);
            String completedLoadingBar = "§2§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
            int formattingCodeLength = 4;
            String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                    completedLoadingBar.length() - formattingCodeLength, maxBarLength));
            String progressBar = completedLoadingBar + uncompletedLoadingBar + "§r §e" + allowedRecipes.size() + "§6/§e" + typeRecipes.size();

            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.recipe.book.category", Map.of("category_name", categoryName)),
                    type.getMaterial(), 1,
                    I18n.lore("gui_sbmenu.recipe.book.category.lore", Map.of("category_name", categoryName, "percent", unlockedPercentage, "progress_bar", progressBar)));
        }, (click, c) -> c.push(new GUIRecipeSlayers()));
    }
}
