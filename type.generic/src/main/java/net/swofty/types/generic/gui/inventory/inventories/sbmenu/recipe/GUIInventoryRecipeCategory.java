package net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedInventory;
import net.swofty.types.generic.item.crafting.ShapedRecipe;
import net.swofty.types.generic.item.crafting.ShapelessRecipe;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GUIInventoryRecipeCategory extends SkyBlockPaginatedInventory<SkyBlockRecipe> {
    private final SkyBlockRecipe.RecipeType type;
    private final SkyBlockAbstractInventory previousGUI;

    public GUIInventoryRecipeCategory(SkyBlockRecipe.RecipeType type, SkyBlockAbstractInventory previousGUI) {
        super(InventoryType.CHEST_6_ROW);
        this.type = type;
        this.previousGUI = previousGUI;
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
            ItemType type = recipe.getResult().getAttributeHandler().getPotentialType();

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
        ).getUpdatedItem().build().get(ItemComponent.CUSTOM_NAME)).toLowerCase().contains(query.toLowerCase());
    }

    @Override
    protected void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Search button
        attachItem(createSearchItem(50, query));

        // Back button
        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To " + previousGUI.getTitle()).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(previousGUI);
                    return true;
                })
                .build());

        // Recipe type info display
        attachItem(createRecipeTypeInfoDisplay());

        if (page > 1) {
            attachItem(createNavigationButton(45, query, page, false));
        }
        if (page < maxPage) {
            attachItem(createNavigationButton(53, query, page, true));
        }
    }

    private GUIItem createRecipeTypeInfoDisplay() {
        ArrayList<SkyBlockRecipe> allRecipes = new ArrayList<>();
        allRecipes.addAll(ShapedRecipe.CACHED_RECIPES);
        allRecipes.addAll(ShapelessRecipe.CACHED_RECIPES);

        return GUIItem.builder(4)
                .item(() -> {
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
                        SkyBlockRecipe.CraftingResult result = (SkyBlockRecipe.CraftingResult) recipe.getCanCraft().apply(owner);
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

                    return ItemStackCreator.getStack("§a" + StringUtility.toNormalCase(type.name()) + " Recipes",
                            type.getMaterial(), 1, lore).build();
                })
                .build();
    }

    @Override
    protected Component getTitle(SkyBlockPlayer player, String query, int page, PaginationList<SkyBlockRecipe> paged) {
        return Component.text("(" + page + "/" + paged.getPages().size() + ") " + StringUtility.toNormalCase(type.name()) + " Recipes");
    }

    @Override
    protected GUIItem createItemFor(SkyBlockRecipe item, int slot, SkyBlockPlayer player) {
        SkyBlockRecipe.CraftingResult result = (SkyBlockRecipe.CraftingResult) item.getCanCraft().apply(player);
        ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(player, item.getResult().getItemStack());

        if (result.allowed()) {
            return GUIItem.builder(slot)
                    .item(() -> {
                        ArrayList<String> lore = new ArrayList<>(
                                itemStack.build().get(ItemComponent.LORE).stream()
                                        .map(StringUtility::getTextFromComponent)
                                        .toList()
                        );

                        lore.add("§e ");
                        lore.add("§eClick to view recipe!");

                        return itemStack.set(ItemComponent.LORE,
                                        lore.stream()
                                                .map(line -> Component.text(line)
                                                        .decoration(TextDecoration.ITALIC, false))
                                                .collect(Collectors.toList()))
                                .build();
                    })
                    .onClick((ctx, clickedItem) -> {
                        ctx.player().openInventory(new GUIRecipe(
                                item.getResult().getAttributeHandler().getPotentialType(),
                                this));
                        return true;
                    })
                    .build();
        } else {
            return GUIItem.builder(slot)
                    .item(() -> {
                        List<String> lore = Arrays.stream(result.errorMessage())
                                .map(line -> "§7" + line)
                                .collect(Collectors.toList());
                        return ItemStackCreator.getStack("§c???", Material.GRAY_DYE, 1, lore).build();
                    })
                    .onClick((ctx, clickedItem) -> {
                        ctx.player().sendMessage("§cYou haven't unlocked that recipe!");
                        return true;
                    })
                    .build();
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}
}