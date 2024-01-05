package net.swofty.gui.inventory.inventories.sbmenu.collection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.collection.CollectionCategory;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.SkyBlockPaginatedGUI;
import net.swofty.gui.inventory.inventories.sbmenu.crafting.GUIRecipe;
import net.swofty.gui.inventory.inventories.sbmenu.crafting.GUIRecipeBook;
import net.swofty.gui.inventory.inventories.sbmenu.crafting.GUIRecipeCategory;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.gui.inventory.item.GUIItem;
import net.swofty.item.impl.SkyBlockRecipe;
import net.swofty.item.impl.recipes.ShapedRecipe;
import net.swofty.item.impl.recipes.ShapelessRecipe;
import net.swofty.item.updater.NonPlayerItemUpdater;
import net.swofty.item.updater.PlayerItemUpdater;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.PaginationList;
import net.swofty.utility.StringUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GUICollectionCategory extends SkyBlockPaginatedGUI<CollectionCategory.ItemCollection> {
    private final CollectionCategory type;
    private final List<String> display;

    public GUICollectionCategory(CollectionCategory category, List<String> display) {
        super(InventoryType.CHEST_6_ROW);

        this.type = category;
        this.display = display;
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
    public int[] getPaginatedSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
    }

    @Override
    public PaginationList<CollectionCategory.ItemCollection> fillPaged(SkyBlockPlayer player, PaginationList<CollectionCategory.ItemCollection> paged) {
        paged.addAll(Arrays.asList(type.getCollections()));
        return paged;
    }

    @Override
    public boolean shouldFilterFromSearch(String query, CollectionCategory.ItemCollection item) {
        String itemName = item.type().getDisplayName();
        return !itemName.toLowerCase().contains(query.toLowerCase());
    }

    @Override
    public void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, ""));
        set(GUIClickableItem.getCloseItem(50));
        set(createSearchItem(this, 48, query));
        set(GUIClickableItem.getGoBackItem(49, new GUICollections()));
        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 4;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>(List.of(
                        "§7View your " + type.getName() + " Collections!",
                        " "
                ));

                lore.addAll(display);

                return ItemStackCreator.getStack("§a" + type.getName() + " Collections",
                        Material.STONE_PICKAXE, 1, lore);
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
    public String getTitle(SkyBlockPlayer player, String query, int page, PaginationList<CollectionCategory.ItemCollection> paged) {
        return type.getName() + " Collections";
    }

    @Override
    public GUIClickableItem createItemFor(CollectionCategory.ItemCollection item, int slot, SkyBlockPlayer player) {
        return null;
    }
}
