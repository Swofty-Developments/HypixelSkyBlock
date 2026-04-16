package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.PaginatedView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.item.crafting.ShapedRecipe;
import net.swofty.type.skyblockgeneric.item.crafting.ShapelessRecipe;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class GUIRecipeCategory extends PaginatedView<SkyBlockRecipe<?>, GUIRecipeCategory.RecipeCategoryState> {

    private static final int[] PAGINATED_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private final SkyBlockRecipe.RecipeType type;

    public GUIRecipeCategory(SkyBlockRecipe.RecipeType type) {
        this.type = type;
    }

    @Override
    public ViewConfiguration<RecipeCategoryState> configuration() {
        return ViewConfiguration.withString(
            (state, ctx) -> I18n.string("gui_sbmenu.recipe.category.title", ctx.player().getLocale(), Component.text(String.valueOf(state.page() + 1)), Component.text(String.valueOf(Math.max(1, (int) Math.ceil((double) getFilteredItems(state).size() / PAGINATED_SLOTS.length)))), Component.text(StringUtility.toNormalCase(type.name()))),
                InventoryType.CHEST_6_ROW
        );
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected ItemStack.Builder renderItem(SkyBlockRecipe<?> item, int index, HypixelPlayer p) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        Locale l = player.getLocale();
        SkyBlockRecipe.CraftingResult result = item.getCanCraft().apply(player);
        ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(
                player, item.getResult().getItemStack()
        );

        if (result.allowed()) {
            ArrayList<String> lore = new ArrayList<>(
                    Objects.requireNonNull(itemStack.build().get(DataComponents.LORE)).stream().map(StringUtility::getTextFromComponent).toList()
            );
            lore.add("§e ");
            lore.add(I18n.string("gui_sbmenu.recipe.category.click_to_view", l));

            return itemStack.set(DataComponents.LORE,
                    lore.stream().map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false))
                            .collect(Collectors.toList()));
        } else {
            List<String> lore = Arrays.asList(result.errorMessage());
            lore = lore.stream().map(line -> "§7" + line).toList();
            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.recipe.category.locked", l), Material.GRAY_DYE, 1, lore);
        }
    }

    @Override
    protected void onItemClick(ClickContext<RecipeCategoryState> click, ViewContext ctx, SkyBlockRecipe<?> item, int index) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        SkyBlockRecipe.CraftingResult result = item.getCanCraft().apply(player);

        if (result.allowed()) {
            ctx.push(new GUIRecipe(item.getResult().getAttributeHandler().getPotentialType()));
        } else {
            player.sendMessage(I18n.string("gui_sbmenu.recipe.category.msg.not_unlocked", player.getLocale()));
        }
    }

    @Override
    protected boolean shouldFilterFromSearch(RecipeCategoryState query, SkyBlockRecipe<?> item) {
        return false;
    }

    @Override
    protected void layoutCustom(ViewLayout<RecipeCategoryState> layout, RecipeCategoryState state, ViewContext ctx) {
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        ArrayList<SkyBlockRecipe<?>> allRecipes = new ArrayList<>();
        allRecipes.addAll(ShapedRecipe.CACHED_RECIPES);
        allRecipes.addAll(ShapelessRecipe.CACHED_RECIPES);

        // Title item
        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            Locale l = player.getLocale();

            ArrayList<SkyBlockRecipe<?>> typeRecipes = new ArrayList<>();
            ArrayList<SkyBlockRecipe<?>> allowedRecipes = new ArrayList<>();
            allRecipes.forEach(recipe -> {
                if (recipe.getRecipeType() == type) {
                    typeRecipes.add(recipe);
                }
            });

            typeRecipes.forEach(recipe -> {
                SkyBlockRecipe.CraftingResult result = recipe.getCanCraft().apply(player);

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

            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.recipe.category.info", l, Component.text(categoryName)),
                    type.getMaterial(), 1,
                I18n.iterable("gui_sbmenu.recipe.category.info.lore", Component.text(categoryName), Component.text(unlockedPercentage), Component.text(progressBar)));
        });
    }

    @Override
    protected int getPreviousPageSlot() {
        return 45;
    }

    @Override
    protected int getNextPageSlot() {
        return 53;
    }

    public static RecipeCategoryState createInitialState(SkyBlockPlayer player, SkyBlockRecipe.RecipeType type) {
        List<SkyBlockRecipe<?>> recipes = new ArrayList<>();
        recipes.addAll(ShapedRecipe.CACHED_RECIPES);
        recipes.addAll(ShapelessRecipe.CACHED_RECIPES);

        recipes.removeIf(recipe -> recipe.getRecipeType() != type);

        List<ItemType> shownItems = new ArrayList<>();
        recipes.removeIf(recipe -> {
            ItemType itemType = recipe.getResult().getAttributeHandler().getPotentialType();
            if (shownItems.contains(itemType)) {
                return true;
            } else {
                shownItems.add(itemType);
                SkyBlockRecipe.CraftingResult result = recipe.getCanCraft().apply(player);
                return !result.allowed();
            }
        });

        return new RecipeCategoryState(List.of(), 0, "");
    }

    public record RecipeCategoryState(
            List<SkyBlockRecipe<?>> items,
            int page,
            String query
    ) implements PaginatedState<SkyBlockRecipe<?>> {
        @Override
        public PaginatedState<SkyBlockRecipe<?>> withPage(int page) {
            return new RecipeCategoryState(items, page, query);
        }

        @Override
        public PaginatedState<SkyBlockRecipe<?>> withItems(List<SkyBlockRecipe<?>> items) {
            return new RecipeCategoryState(items, page, query);
        }
    }
}
