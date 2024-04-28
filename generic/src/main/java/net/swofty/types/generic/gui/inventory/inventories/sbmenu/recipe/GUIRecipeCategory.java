package net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GUIRecipeCategory extends SkyBlockPaginatedGUI<SkyBlockRecipe> {
    private final SkyBlockRecipe.RecipeType type;
    private final SkyBlockInventoryGUI previousGUI;

    protected GUIRecipeCategory(SkyBlockRecipe.RecipeType type, SkyBlockInventoryGUI previousGUI) {
        super(InventoryType.CHEST_6_ROW);
        this.type = type;
        this.previousGUI = previousGUI;
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

    @Override
    protected int[] getPaginatedSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
    }

    @Override
    protected PaginationList<SkyBlockRecipe> fillPaged(SkyBlockPlayer player, PaginationList<SkyBlockRecipe> paged) {
        paged.addAll(ShapedRecipe.CACHED_RECIPES);
        paged.addAll(ShapelessRecipe.CACHED_RECIPES);

        paged.removeIf(recipe -> recipe.getRecipeType() != type);

        List<ItemType> shownItems = new ArrayList<>();
        paged.removeIf(recipe -> {
            ItemType type = recipe.getResult().getAttributeHandler().getItemTypeAsType();

            if (shownItems.contains(type)) {
                return true;
            } else {
                shownItems.add(type);
                return false;
            }
        });

        return paged;
    }

    @Override
    protected boolean shouldFilterFromSearch(String query, SkyBlockRecipe item) {
        return !StringUtility.getTextFromComponent(new NonPlayerItemUpdater(
                item.getResult()
        ).getUpdatedItem().build().getDisplayName()).toLowerCase().contains(query.toLowerCase());
    }

    @Override
    protected void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(createSearchItem(this, 50, query));
        set(GUIClickableItem.getGoBackItem(48, previousGUI));

        ArrayList<SkyBlockRecipe> allRecipes = new ArrayList<>();
        allRecipes.addAll(ShapedRecipe.CACHED_RECIPES);
        allRecipes.addAll(ShapelessRecipe.CACHED_RECIPES);
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {

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

        if (page > 1) {
            set(createNavigationButton(this, 45, query, page, false));
        }
        if (page < maxPage) {
            set(createNavigationButton(this, 53, query, page, true));
        }
    }

    @Override
    protected String getTitle(SkyBlockPlayer player, String query, int page, PaginationList<SkyBlockRecipe> paged) {
        return "(" + page + "/" + paged.getPages().size() + ") " + StringUtility.toNormalCase(type.name()) + " Recipes";
    }

    @Override
    protected GUIClickableItem createItemFor(SkyBlockRecipe item, int slot, SkyBlockPlayer player) {
        SkyBlockRecipe.CraftingResult result = (SkyBlockRecipe.CraftingResult) item.getCanCraft().apply(player);
        ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(
                player, item.getResult().getItemStack()
        );

        if (result.allowed()) {
            return new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIRecipe(
                            item.getResult().getAttributeHandler().getItemTypeAsType(),
                            GUIRecipeCategory.this).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    ArrayList<String> lore = new ArrayList<>(
                            itemStack.build().getLore().stream().map(StringUtility::getTextFromComponent).toList()
                    );

                    lore.add("§e ");
                    lore.add("§eClick to view recipe!");

                    return itemStack.lore(
                            lore.stream().map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false))
                                    .collect(Collectors.toList()));
                }
            };
        } else {
            return new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    player.sendMessage("§cYou haven't unlocked that recipe!");
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    List<String> lore = Arrays.asList(result.errorMessage());
                    // Add gray text to the start of each line
                    lore = lore.stream().map(line -> "§7" + line).toList();

                    return ItemStackCreator.getStack("§c???", Material.GRAY_DYE, (short) 0, 1, lore);
                }
            };
        }
    }
}
