package net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.Arrays;

public class GUIRecipeSlayers extends SkyBlockInventoryGUI {
    public GUIRecipeSlayers() {
        super("Slayer Recipes", InventoryType.CHEST_6_ROW);
    }

    private static final int[] categorySlots = {
            20, 21, 22, 23, 24
    };

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUIRecipeBook()));

        ArrayList<SkyBlockRecipe> allRecipes = new ArrayList<>();
        allRecipes.addAll(ShapedRecipe.CACHED_RECIPES);
        allRecipes.addAll(ShapelessRecipe.CACHED_RECIPES);

        ArrayList<SkyBlockRecipe.RecipeType> recipeTypes = new ArrayList<>();
        recipeTypes.add(SkyBlockRecipe.RecipeType.REVENANT_HORROR);
        recipeTypes.add(SkyBlockRecipe.RecipeType.TARANTULA_BROODFATHER);
        recipeTypes.add(SkyBlockRecipe.RecipeType.SVEN_PACKMASTER);
        recipeTypes.add(SkyBlockRecipe.RecipeType.VOIDGLOOM_SERAPH);
        recipeTypes.add(SkyBlockRecipe.RecipeType.INFERNO_DEMONLORD);

        int index = 0;
        for (int slot : categorySlots) {

            SkyBlockRecipe.RecipeType type = recipeTypes.get(index++);

            ArrayList<SkyBlockRecipe> typeRecipes = new ArrayList<>();
            ArrayList<SkyBlockRecipe> allowedRecipes = new ArrayList<>();
            allRecipes.forEach(recipe -> {
                if (recipe.getRecipeType() == type) {
                    typeRecipes.add(recipe);
                }
            });

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIRecipeCategory(type, new GUIRecipeSlayers()).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
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
                    int formattingCodeLength = 4;  // Adjust this if you add or remove formatting codes
                    String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                            completedLoadingBar.length() - formattingCodeLength,  // Adjust for added formatting codes
                            maxBarLength
                    ));

                    lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §e" + allowedRecipes.size() + "§6/§e" + typeRecipes.size());
                    lore.add(" ");
                    lore.add("§eClick to view!");

                    return ItemStackCreator.getStack("§a" + StringUtility.toNormalCase(type.name()) + " Recipes",
                            type.getMaterial(), (short) 0, 1, lore);
                }
            });
            set(new GUIItem(4) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {

                    SkyBlockRecipe.RecipeType type = SkyBlockRecipe.RecipeType.SLAYER;

                    ArrayList<SkyBlockRecipe> typeRecipes = new ArrayList<>();
                    ArrayList<SkyBlockRecipe> allowedRecipes = new ArrayList<>();
                    allRecipes.forEach(recipe -> {
                        if (recipeTypes.contains(recipe.getRecipeType())) {
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
                    int formattingCodeLength = 4;  // Adjust this if you add or remove formatting codes
                    String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                            completedLoadingBar.length() - formattingCodeLength,  // Adjust for added formatting codes
                            maxBarLength
                    ));

                    lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §e" + allowedRecipes.size() + "§6/§e" + typeRecipes.size());

                    return ItemStackCreator.getStack("§a" + StringUtility.toNormalCase(type.name()) + " Recipes",
                            type.getMaterial(), (short) 0, 1, lore);
                }
            });
        }
        updateItemStacks(getInventory(), getPlayer());

    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
