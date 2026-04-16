package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe;

import net.minestom.server.inventory.InventoryType;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.item.crafting.ShapedRecipe;
import net.swofty.type.skyblockgeneric.item.crafting.ShapelessRecipe;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        return ViewConfiguration.translatable("gui_sbmenu.recipe.slayers.title", InventoryType.CHEST_6_ROW);
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
            Locale l = player.getLocale();

            SkyBlockRecipe.RecipeType type = SkyBlockRecipe.RecipeType.SLAYER;

            ArrayList<SkyBlockRecipe> typeRecipes = new ArrayList<>();
            ArrayList<SkyBlockRecipe> allowedRecipes = new ArrayList<>();
            allRecipes.forEach(recipe -> {
                if (SLAYER_TYPES.contains(recipe.getRecipeType())) {
                    typeRecipes.add(recipe);
                }
            });

            typeRecipes.forEach(recipe -> {
                SkyBlockRecipe.CraftingResult result =
                        (SkyBlockRecipe.CraftingResult) recipe.getCanCraft().apply(player);

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
                    completedLoadingBar.length() - formattingCodeLength,
                    maxBarLength
            ));
            String progressBar = completedLoadingBar + uncompletedLoadingBar + "§r §e" + allowedRecipes.size() + "§6/§e" + typeRecipes.size();

            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.recipe.book.category", l, Map.of("category_name", categoryName)),
                    type.getMaterial(), 1,
                    I18n.lore("gui_sbmenu.recipe.book.category.lore", l, Map.of("category_name", categoryName, "percent", unlockedPercentage, "progress_bar", progressBar)));
        });

        // Category items
        for (int i = 0; i < CATEGORY_SLOTS.length && i < SLAYER_TYPES.size(); i++) {
            SkyBlockRecipe.RecipeType type = SLAYER_TYPES.get(i);
            int slot = CATEGORY_SLOTS[i];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                Locale l = player.getLocale();

                ArrayList<SkyBlockRecipe> typeRecipes = new ArrayList<>();
                ArrayList<SkyBlockRecipe> allowedRecipes = new ArrayList<>();
                allRecipes.forEach(recipe -> {
                    if (recipe.getRecipeType() == type) {
                        typeRecipes.add(recipe);
                    }
                });

                typeRecipes.forEach(recipe -> {
                    SkyBlockRecipe.CraftingResult result =
                            (SkyBlockRecipe.CraftingResult) recipe.getCanCraft().apply(player);

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
                        completedLoadingBar.length() - formattingCodeLength,
                        maxBarLength
                ));
                String progressBar = completedLoadingBar + uncompletedLoadingBar + "§r §e" + allowedRecipes.size() + "§6/§e" + typeRecipes.size();

                return ItemStackCreator.getStack(I18n.string("gui_sbmenu.recipe.book.category", l, Map.of("category_name", categoryName)),
                        type.getMaterial(), 1,
                        I18n.lore("gui_sbmenu.recipe.book.category.lore", l, Map.of("category_name", categoryName, "percent", unlockedPercentage, "progress_bar", progressBar)));
            }, (_, c) -> c.push(new GUIRecipeCategory(type), GUIRecipeCategory.createInitialState((SkyBlockPlayer) c.player(), type)));
        }
    }
}
