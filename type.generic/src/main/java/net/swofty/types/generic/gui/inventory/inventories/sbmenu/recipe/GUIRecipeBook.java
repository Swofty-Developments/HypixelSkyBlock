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
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.item.crafting.ShapedRecipe;
import net.swofty.types.generic.item.crafting.ShapelessRecipe;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIRecipeBook extends SkyBlockAbstractInventory {
    private final int[] categorySlots = {
            20, 21, 22, 23, 24,
            29, 30, 31, 33
    };

    public GUIRecipeBook() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Recipe Book")));
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
                        "§7To SkyBlock Menu").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockMenu());
                    return true;
                })
                .build());

        ArrayList<SkyBlockRecipe> allRecipes = new ArrayList<>();
        allRecipes.addAll(ShapedRecipe.CACHED_RECIPES);
        allRecipes.addAll(ShapelessRecipe.CACHED_RECIPES);

        // Recipe book info
        attachItem(GUIItem.builder(4)
                .item(() -> {
                    List<String> lore = new ArrayList<>(List.of(
                            "§7Through your adventure, you will",
                            "§7unlock recipes for all kinds of",
                            "§7special items! You can view how to",
                            "§7craft these items here.",
                            " "
                    ));
                    SkyBlockRecipe.getMissionDisplay(lore, player.getUuid());
                    return ItemStackCreator.getStack("§aRecipe Book",
                            Material.BOOK, 1, lore.toArray(new String[0])).build();
                })
                .build());

        // Category slots
        int index = 0;
        for (int slot : categorySlots) {
            SkyBlockRecipe.RecipeType type = SkyBlockRecipe.RecipeType.values()[index++];
            attachCategoryItem(slot, type, allRecipes);
        }

        // Slayer recipes
        attachSlayerRecipeItem(32, allRecipes);
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

    private void attachSlayerRecipeItem(int slot, ArrayList<SkyBlockRecipe> allRecipes) {
        ArrayList<SkyBlockRecipe.RecipeType> slayerTypes = new ArrayList<>(Arrays.asList(
                SkyBlockRecipe.RecipeType.REVENANT_HORROR,
                SkyBlockRecipe.RecipeType.TARANTULA_BROODFATHER,
                SkyBlockRecipe.RecipeType.SVEN_PACKMASTER,
                SkyBlockRecipe.RecipeType.VOIDGLOOM_SERAPH,
                SkyBlockRecipe.RecipeType.INFERNO_DEMONLORD
        ));

        ArrayList<SkyBlockRecipe> typeRecipes = new ArrayList<>();
        ArrayList<SkyBlockRecipe> allowedRecipes = new ArrayList<>();

        allRecipes.forEach(recipe -> {
            if (slayerTypes.contains(recipe.getRecipeType())) {
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

                    addProgressToLore(lore, allowedRecipes.size(), typeRecipes.size());

                    return ItemStackCreator.getStack("§aSlayer Recipes",
                            SkyBlockRecipe.RecipeType.SLAYER.getMaterial(), 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIRecipeSlayers());
                    return true;
                })
                .build());
    }

    private void addProgressToLore(ArrayList<String> lore, int completed, int total) {
        String unlockedPercentage = String.format("%.2f", (completed / (double) total) * 100);
        lore.add("§7Recipes Unlocked: §e" + unlockedPercentage + "§6%");

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
        lore.add(" ");
        lore.add("§eClick to view!");
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