package net.swofty.types.generic.gui.inventory.inventories.sbmenu.collection;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUICollections extends SkyBlockAbstractInventory {
    private static final int[] DISPLAY_SLOTS = {
            20, 21, 22, 23, 24,
            31
    };

    public GUICollections() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Collections")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());

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
                        "§7To SkyBlock Menu").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockMenu());
                    return true;
                })
                .build());

        // Collections overview
        attachItem(GUIItem.builder(4)
                .item(() -> {
                    List<String> lore = new ArrayList<>(List.of(
                            "§7View all of the items available in",
                            "§7SkyBlock. Collect more of an item to",
                            "§7unlock rewards on your way to",
                            "§7becoming a master of SkyBlock!",
                            " "
                    ));

                    owner.getCollection().getDisplay(lore);

                    lore.add(" ");
                    lore.add("§eClick to view!");
                    return ItemStackCreator.getStack("§aCollections",
                            Material.PAINTING,
                            1,
                            lore).build();
                })
                .build());

        // Crafted minions button
        attachItem(GUIItem.builder(50)
                .item(ItemStackCreator.getStackHead("§aCrafted Minions",
                        "ebcc099f3a00ece0e5c4b31d31c828e52b06348d0a4eac11f3fcbef3c05cb407",
                        1,
                        "§7View all the unique minions that you",
                        "§7have crafted.",
                        "",
                        "§eClick to view!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIInventoryCraftedMinions(new GUICollections()));
                    return true;
                })
                .build());

        // Category buttons
        setupCategoryButtons(player);
    }

    private void setupCategoryButtons(SkyBlockPlayer player) {
        ArrayList<CollectionCategory> allCategories = CollectionCategories.getCategories();

        int index = 0;
        for (int slot : DISPLAY_SLOTS) {
            CollectionCategory category = allCategories.get(index);
            ArrayList<String> display = new ArrayList<>();
            player.getCollection().getDisplay(display, category);

            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        ArrayList<String> lore = new ArrayList<>(Arrays.asList(
                                "§7View your " + category.getName() + " Collections!",
                                " "
                        ));

                        lore.addAll(display);

                        return ItemStackCreator.getStack(
                                "§a" + category.getName() + " Collections",
                                category.getDisplayIcon(),
                                1,
                                lore).build();
                    })
                    .onClick((ctx, item) -> {
                        ctx.player().openInventory(new GUIInventoryCollectionCategory(category, display));
                        return true;
                    })
                    .build());

            index++;
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