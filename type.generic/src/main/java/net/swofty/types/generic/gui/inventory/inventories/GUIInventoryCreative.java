package net.swofty.types.generic.gui.inventory.inventories;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedInventory;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.TrackedUniqueComponent;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIInventoryCreative extends SkyBlockPaginatedInventory<SkyBlockItem> {

    public GUIInventoryCreative() {
        super(InventoryType.CHEST_6_ROW);
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
    public PaginationList<SkyBlockItem> fillPaged(SkyBlockPlayer player, PaginationList<SkyBlockItem> paged) {
        paged.addAll(Arrays.stream(ItemType.values()).map(SkyBlockItem::new).toList());

        List<SkyBlockItem> vanilla = new ArrayList<>(Material.values().stream().map(SkyBlockItem::new).toList());
        vanilla.removeIf((element) -> ItemType.isVanillaReplaced(element.getAttributeHandler().getTypeAsString()));
        paged.addAll(vanilla);
        return paged;
    }

    @Override
    public boolean shouldFilterFromSearch(String query, SkyBlockItem item) {
        return !item.getAttributeHandler().getTypeAsString().toLowerCase().contains(query.replaceAll(" ", "_").toLowerCase());
    }

    @Override
    protected void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        attachItem(GUIItem.builder(50)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        attachItem(createSearchItem(48, query));

        if (page > 1) {
            attachItem(createNavigationButton(45, query, page, false));
        }
        if (page < maxPage) {
            attachItem(createNavigationButton(53, query, page, true));
        }
    }

    @Override
    protected Component getTitle(SkyBlockPlayer player, String query, int page, PaginationList<SkyBlockItem> paged) {
        return Component.text("Creative Menu | Page " + page + "/" + paged.getPageCount());
    }

    @Override
    protected GUIItem createItemFor(SkyBlockItem skyBlockItem, int slot, SkyBlockPlayer player) {
        ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(
                player, skyBlockItem.getItemStack()
        );

        boolean stackable = !skyBlockItem.hasComponent(TrackedUniqueComponent.class);

        return GUIItem.builder(slot)
                .item(() -> {
                    ArrayList<String> lore = new ArrayList<>(skyBlockItem.getLore());
                    lore.add(" ");
                    if (stackable)
                        lore.add("§bRight Click for §7x64 §eof this item.");
                    lore.add("§eLeft Click for §7x1 §eof this item.");
                    return ItemStackCreator.updateLore(itemStack, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));

                    if (ctx.clickType().equals(ClickType.RIGHT_CLICK) && stackable) {
                        skyBlockItem.setAmount(64);
                        ctx.player().addAndUpdateItem(skyBlockItem);
                        ctx.player().sendMessage(Component.text("§aYou have been given §7x64 " + skyBlockItem.getDisplayName() + "§a."));
                    } else {
                        skyBlockItem.setAmount(1);
                        ctx.player().addAndUpdateItem(skyBlockItem);
                        ctx.player().sendMessage(Component.text("§aYou have been given §7x1 " + skyBlockItem.getDisplayName() + "§a."));
                    }
                    return true;
                })
                .build();
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
