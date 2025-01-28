package net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.crafting.ShapedRecipe;
import net.swofty.types.generic.item.crafting.ShapelessRecipe;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIRecipeSlayers extends SkyBlockAbstractInventory {
    private static final int[] categorySlots = {
            20, 21, 22, 23, 24
    };

    public GUIRecipeSlayers() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Slayer Recipes")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Close and back buttons
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Recipe Book").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIRecipeBook());
                    return true;
                })
                .build());

        ArrayList<SkyBlockRecipe> allRecipes = new ArrayList<>();
        allRecipes.addAll(ShapedRecipe.CACHED_RECIPES);
        allRecipes.addAll(ShapelessRecipe.CACHED_RECIPES);

        List<SkyBlockRecipe.RecipeType> recipeTypes = Arrays.asList(
                SkyBlockRecipe.RecipeType.REVENANT_HORROR,
                SkyBlockRecipe.RecipeType.TARANTULA_BROODFATHER,
                SkyBlockRecipe.RecipeType.SVEN_PACKMASTER,
                SkyBlockRecipe.RecipeType.VOIDGLOOM_SERAPH,
                SkyBlockRecipe.RecipeType.INFERNO_DEMONLORD
        );

        // Add category slots
        for (int i = 0; i < categorySlots.length; i++) {
            int slot = categorySlots[i];
            SkyBlockRecipe.RecipeType type = recipeTypes.get(i);
            attachCategoryItem(slot, type, allRecipes);
        }

        // Add overview item
        attachOverviewItem(4, allRecipes, recipeTypes);
    }

    private void attachCategoryItem(int slot, SkyBlockRecipe.RecipeType type, ArrayList<SkyBlockRecipe> allRecipes) {
        ArrayList<SkyBlockRecipe> typeRecipes = new ArrayList<>();
        ArrayList<SkyBlockRecipe> allowedRecipes = new ArrayList<>();

        allRecipes.forEach(recipe -> {
            if (recipe.getRecipeType() == type) {
                typeRecipes.add(recipe);
            }
        });

        attachItem(GUIItem.builder(slot)
                .item(() -> {
                    ArrayList<String> lore = new ArrayList<>(Arrays.asList(
                            "§7View all of the " + StringUtility.toNormalCase(type.name()) + " Recipes",
                            "§7that you have unlocked!",
                            " "));

                    typeRecipes.forEach(recipe -> {
                        if (((SkyBlockRecipe.CraftingResult) recipe.getCanCraft().apply(owner)).allowed()) {
                            allowedRecipes.add(recipe);
                        }
                    });

                    addProgressToLore(lore, allowedRecipes.size(), typeRecipes.size());

                    return ItemStackCreator.getStack("§a" + StringUtility.toNormalCase(type.name()) + " Recipes",
                            type.getMaterial(), 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIInventoryRecipeCategory(type, this));
                    return true;
                })
                .build());
    }

    private void attachOverviewItem(int slot, ArrayList<SkyBlockRecipe> allRecipes, List<SkyBlockRecipe.RecipeType> recipeTypes) {
        ArrayList<SkyBlockRecipe> typeRecipes = new ArrayList<>();
        ArrayList<SkyBlockRecipe> allowedRecipes = new ArrayList<>();

        allRecipes.forEach(recipe -> {
            if (recipeTypes.contains(recipe.getRecipeType())) {
                typeRecipes.add(recipe);
            }
        });

        attachItem(GUIItem.builder(slot)
                .item(() -> {
                    ArrayList<String> lore = new ArrayList<>(Arrays.asList(
                            "§7View all of the Slayer Recipes",
                            "§7that you have unlocked!",
                            " "));

                    typeRecipes.forEach(recipe -> {
                        if (((SkyBlockRecipe.CraftingResult) recipe.getCanCraft().apply(owner)).allowed()) {
                            allowedRecipes.add(recipe);
                        }
                    });

                    addProgressToLore(lore, allowedRecipes.size(), typeRecipes.size(), true);

                    return ItemStackCreator.getStack("§aSlayer Recipes",
                            SkyBlockRecipe.RecipeType.SLAYER.getMaterial(), 1, lore).build();
                })
                .build());
    }

    private void addProgressToLore(ArrayList<String> lore, int completed, int total) {
        addProgressToLore(lore, completed, total, false);
    }

    private void addProgressToLore(ArrayList<String> lore, int completed, int total, boolean isSlayer) {
        String unlockedPercentage = String.format("%.2f", (completed / (double) total) * 100);
        lore.add("§7" + (isSlayer ? "Slayer " : "") + "Recipes Unlocked: §e" + unlockedPercentage + "§6%");

        String baseLoadingBar = "─────────────────";
        int maxBarLength = baseLoadingBar.length();
        int completedLength = (int) ((completed / (double) total) * maxBarLength);

        String completedLoadingBar = "§2§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
        int formattingCodeLength = 4;
        String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                completedLoadingBar.length() - formattingCodeLength,
                maxBarLength
        ));

        lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §e" + completed + "§6/§e" + total);

        if (!isSlayer) {
            lore.add(" ");
            lore.add("§eClick to view!");
        }
    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {}
}