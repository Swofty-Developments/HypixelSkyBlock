package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.emblem;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedInventory;
import net.swofty.types.generic.levels.SkyBlockEmblems;
import net.swofty.types.generic.levels.abstr.CauseEmblem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

import java.util.ArrayList;
import java.util.List;

public class GUIEmblem extends SkyBlockPaginatedInventory<SkyBlockEmblems.SkyBlockEmblem> {
    private static final String STATE_EMBLEM_UNLOCKED = "emblem_unlocked";
    private static final String STATE_EMBLEM_LOCKED = "emblem_locked";

    private final SkyBlockEmblems emblemCategory;

    public GUIEmblem(SkyBlockEmblems emblemCategory) {
        super(InventoryType.CHEST_5_ROW);
        this.emblemCategory = emblemCategory;
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        // Close button
        attachItem(GUIItem.builder(40)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Search item
        attachItem(createSearchItem(41, currentQuery));

        // Back button
        attachItem(GUIItem.builder(39)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Emblems").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIEmblems());
                    return true;
                })
                .build());
    }

    @Override
    public int[] getPaginatedSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
        };
    }

    @Override
    public PaginationList<SkyBlockEmblems.SkyBlockEmblem> fillPaged(SkyBlockPlayer player, PaginationList<SkyBlockEmblems.SkyBlockEmblem> paged) {
        paged.addAll(emblemCategory.getEmblems());
        return paged;
    }

    @Override
    public boolean shouldFilterFromSearch(String query, SkyBlockEmblems.SkyBlockEmblem item) {
        return !item.displayName().toLowerCase().contains(query.toLowerCase());
    }

    @Override
    public void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
        if (page > 1) {
            attachItem(createNavigationButton(36, query, page, false));
        }
        if (page < maxPage) {
            attachItem(createNavigationButton(44, query, page, true));
        }
    }

    @Override
    public Component getTitle(SkyBlockPlayer player, String query, int page, PaginationList<SkyBlockEmblems.SkyBlockEmblem> paged) {
        return Component.text("Emblems - " + emblemCategory.toString() + " (" + page + "/" + paged.getPageCount() + ")");
    }

    @Override
    public GUIItem createItemFor(SkyBlockEmblems.SkyBlockEmblem item, int slot, SkyBlockPlayer player) {
        boolean unlocked = player.hasUnlockedXPCause(item.cause());
        CauseEmblem causeEmblem = (CauseEmblem) item.cause();

        return GUIItem.builder(slot)
                .requireState(unlocked ? STATE_EMBLEM_UNLOCKED : STATE_EMBLEM_LOCKED)
                .item(() -> {
                    String name = (unlocked ? "§a" : "§c") + item.displayName() + " " + item.emblem();
                    List<String> lore = new ArrayList<>();

                    if (unlocked) {
                        lore.addAll(List.of(
                                "§8" + causeEmblem.emblemEisplayName(),
                                " ",
                                "§7Preview: " + player.getFullDisplayName(item),
                                " ",
                                "§eClick to select!"
                        ));
                    } else {
                        lore.addAll(List.of(
                                "§8Locked",
                                " ",
                                "§c" + causeEmblem.getEmblemRequiresMessage()
                        ));
                    }

                    return ItemStackCreator.getStack(name,
                            unlocked ? item.displayMaterial() : Material.GRAY_DYE,
                            1,
                            lore).build();
                })
                .onClick((ctx, clickedItem) -> {
                    if (!unlocked) {
                        ctx.player().sendMessage("§cYou have not unlocked this emblem yet!");
                        return false;
                    }

                    ctx.player().getSkyBlockExperience().setEmblem(emblemCategory, item);
                    ctx.player().sendMessage("§aYou have selected the " + item.displayName() + " emblem!");
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