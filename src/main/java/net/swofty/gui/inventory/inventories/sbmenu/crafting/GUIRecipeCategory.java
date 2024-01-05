package net.swofty.gui.inventory.inventories.sbmenu.crafting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.SkyBlockPaginatedGUI;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.item.impl.SkyBlockRecipe;
import net.swofty.item.impl.recipes.ShapedRecipe;
import net.swofty.item.impl.recipes.ShapelessRecipe;
import net.swofty.item.updater.NonPlayerItemUpdater;
import net.swofty.item.updater.PlayerItemUpdater;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.PaginationList;
import net.swofty.utility.StringUtility;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GUIRecipeCategory extends SkyBlockPaginatedGUI<SkyBlockRecipe> {
    private final SkyBlockRecipe.RecipeType type;

    protected GUIRecipeCategory(SkyBlockRecipe.RecipeType type) {
        super(InventoryType.CHEST_6_ROW);
        this.type = type;
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
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, ""));
        set(GUIClickableItem.getCloseItem(50));
        set(createSearchItem(this, 48, query));
        set(GUIClickableItem.getGoBackItem(49, new GUIRecipeBook()));

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
                player, null, item.getResult().getItemStack()
        );

        if (result.allowed()) {
            return new GUIClickableItem() {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIRecipe(
                            item.getResult().getAttributeHandler().getItemTypeAsType(),
                            GUIRecipeCategory.this).open(player);
                }

                @Override
                public int getSlot() {
                    return slot;
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
            return new GUIClickableItem() {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    player.sendMessage("§cYou haven't unlocked that recipe!");
                }

                @Override
                public int getSlot() {
                    return slot;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§c???", Material.GRAY_DYE, (short) 0, 1,
                            "§7" + result.errorMessage()[1]);
                }
            };
        }
    }
}
