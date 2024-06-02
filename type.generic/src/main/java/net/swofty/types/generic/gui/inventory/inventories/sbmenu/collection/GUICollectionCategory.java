package net.swofty.types.generic.gui.inventory.inventories.sbmenu.collection;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.data.datapoints.DatapointCollection;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        String itemName = item.type().getDisplayName(null);
        return !itemName.toLowerCase().contains(query.toLowerCase());
    }

    @Override
    public void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, ""));
        set(GUIClickableItem.getCloseItem(50));
        set(createSearchItem(this, 48, query));
        set(GUIClickableItem.getGoBackItem(49, new GUICollections()));
        set(new GUIItem(4) {
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
        DatapointCollection.PlayerCollection collection = player.getCollection();

        if (!collection.unlocked(item.type())) {
            return new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    player.sendMessage("§cYou haven't found this item yet!");
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack(
                            "§c" + item.type().getDisplayName(null), Material.GRAY_DYE, 1,
                            "§7Find this item to add it to your",
                            "§7collection and unlock collection",
                            "§7rewards!");
                }
            };
        }

        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUICollectionItem(item.type()).open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>(List.of(
                        "§7View all your " + item.type().getDisplayName(null) + " Collection",
                        "§7progress and rewards!",
                        " "
                ));

                collection.getDisplay(lore, item);

                lore.add(" ");
                lore.add("§eClick to view!");

                return ItemStackCreator.getStack("§e" + item.type().getDisplayName(null), item.type().material, 1, lore);
            }
        };
    }
}
