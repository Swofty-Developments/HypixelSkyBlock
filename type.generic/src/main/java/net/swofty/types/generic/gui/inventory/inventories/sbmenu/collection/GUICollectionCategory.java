package net.swofty.types.generic.gui.inventory.inventories.sbmenu.collection;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.data.datapoints.DatapointCollection;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedInventory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUICollectionCategory extends SkyBlockPaginatedInventory<CollectionCategory.ItemCollection> {
    private static final String STATE_COLLECTION_LOCKED = "collection_locked";
    private static final String STATE_COLLECTION_UNLOCKED = "collection_unlocked";

    private final CollectionCategory type;
    private final List<String> display;

    public GUICollectionCategory(CollectionCategory category, List<String> display) {
        super(InventoryType.CHEST_6_ROW);
        this.type = category;
        this.display = display;
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Back button
        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Collections").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUICollections());
                    return true;
                })
                .build());

        // Category info display
        attachItem(GUIItem.builder(4)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§7View your " + type.getName() + " Collections!");
                    lore.add(" ");
                    lore.addAll(display);

                    return ItemStackCreator.getStack("§a" + type.getName() + " Collections",
                            Material.STONE_PICKAXE, 1, lore).build();
                })
                .build());
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
        return false;
    }

    @Override
    public void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
        if (page > 1) {
            attachItem(createNavigationButton(45, query, page, false));
        }
        if (page < maxPage) {
            attachItem(createNavigationButton(53, query, page, true));
        }
    }

    @Override
    public Component getTitle(SkyBlockPlayer player, String query, int page, PaginationList<CollectionCategory.ItemCollection> paged) {
        return Component.text(type.getName() + " Collections");
    }

    @Override
    public GUIItem createItemFor(CollectionCategory.ItemCollection item, int slot, SkyBlockPlayer player) {
        DatapointCollection.PlayerCollection collection = player.getCollection();
        boolean isUnlocked = collection.unlocked(item.type());

        return GUIItem.builder(slot)
                .requireState(isUnlocked ? STATE_COLLECTION_UNLOCKED : STATE_COLLECTION_LOCKED)
                .item(() -> {
                    if (!isUnlocked) {
                        return ItemStackCreator.getStack(
                                "§c" + item.type().getDisplayName(),
                                Material.GRAY_DYE,
                                1,
                                "§7Find this item to add it to your",
                                "§7collection and unlock collection",
                                "§7rewards!").build();
                    }

                    List<String> lore = new ArrayList<>();
                    lore.add("§7View all your " + item.type().getDisplayName() + " Collection");
                    lore.add("§7progress and rewards!");
                    lore.add(" ");
                    collection.getDisplay(lore, item);
                    lore.add(" ");
                    lore.add("§eClick to view!");

                    return ItemStackCreator.getStack("§e" + item.type().getDisplayName(),
                            item.type().material,
                            1,
                            lore).build();
                })
                .onClick((ctx, clickedItem) -> {
                    if (!isUnlocked) {
                        ctx.player().sendMessage("§cYou haven't found this item yet!");
                        return false;
                    }

                    ctx.player().openInventory(new GUICollectionItem(item.type()));
                    return true;
                })
                .build();
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