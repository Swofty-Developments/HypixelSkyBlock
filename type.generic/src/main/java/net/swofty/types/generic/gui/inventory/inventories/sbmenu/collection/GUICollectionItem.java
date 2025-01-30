package net.swofty.types.generic.gui.inventory.inventories.sbmenu.collection;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.data.datapoints.DatapointCollection;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUICollectionItem extends SkyBlockAbstractInventory {
    private static final String STATE_CURRENT_TIER = "current_tier";
    private static final String STATE_COMPLETED_TIER = "completed_tier";
    private static final String STATE_LOCKED_TIER = "locked_tier";

    private final ItemType item;
    private final CollectionCategory category;
    private final CollectionCategory.ItemCollection collection;

    public GUICollectionItem(ItemType item) {
        super(InventoryType.CHEST_6_ROW);

        this.item = item;
        this.category = CollectionCategories.getCategory(item);
        this.collection = category.getCollection(item);

        doAction(new SetTitleAction(Component.text(item.getDisplayName() + " Collection")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

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
                        "§7To " + category.getName()).build())
                .onClick((ctx, clickedItem) -> {
                    ctx.player().openInventory(new GUICollectionCategory(
                            category,
                            ctx.player().getCollection().getDisplay(new ArrayList<>(), category)
                    ));
                    return true;
                })
                .build());

        // Collection info display
        attachItem(GUIItem.builder(4)
                .item(() -> ItemStackCreator.getStack("§e" + item.getDisplayName(),
                        item.material, 1,
                        "§7View all your " + item.getDisplayName() + " Collection",
                        "§7progress and rewards!",
                        " ",
                        "§7Total Collected: §e" + owner.getCollection().get(item)).build())
                .build());

        setupCollectionRewards(player);
    }

    private void setupCollectionRewards(SkyBlockPlayer player) {
        DatapointCollection.PlayerCollection playerCollection = player.getCollection();
        CollectionCategory.ItemCollectionReward currentReward = playerCollection.getReward(collection);

        int slot = 17;
        for (CollectionCategory.ItemCollectionReward reward : collection.rewards()) {
            slot++;
            final int currentSlot = slot;

            if (currentReward == reward) {
                doAction(new AddStateAction(STATE_CURRENT_TIER + "_" + currentSlot));
            } else if (currentReward != null &&
                    collection.getPlacementOf(currentReward) > collection.getPlacementOf(reward)) {
                doAction(new AddStateAction(STATE_COMPLETED_TIER + "_" + currentSlot));
            } else {
                doAction(new AddStateAction(STATE_LOCKED_TIER + "_" + currentSlot));
            }

            attachItem(GUIItem.builder(currentSlot)
                    .item(() -> {
                        List<String> lore = new ArrayList<>();
                        lore.add(" ");
                        playerCollection.getDisplay(lore, collection, reward);
                        lore.add(" ");
                        lore.add("§eClick to view rewards!");

                        if (currentReward == null) {
                            return ItemStackCreator.getStack(
                                    "§7" + item.getDisplayName() + " " +
                                            StringUtility.getAsRomanNumeral(collection.getPlacementOf(reward) + 1),
                                    Material.GREEN_STAINED_GLASS_PANE,
                                    1,
                                    lore
                            ).build();
                        }

                        Material material;
                        String colour;
                        if (currentReward == reward) {
                            material = Material.YELLOW_STAINED_GLASS_PANE;
                            colour = "§e";
                        } else if (collection.getPlacementOf(currentReward) > collection.getPlacementOf(reward)) {
                            material = Material.LIME_STAINED_GLASS_PANE;
                            colour = "§a";
                        } else {
                            material = Material.RED_STAINED_GLASS_PANE;
                            colour = "§c";
                        }

                        return ItemStackCreator.getStack(
                                colour + item.getDisplayName() + " " +
                                        StringUtility.getAsRomanNumeral(collection.getPlacementOf(reward) + 1),
                                material,
                                1,
                                lore
                        ).build();
                    })
                    .onClick((ctx, clickedItem) -> {
                        ctx.player().openInventory(new GUICollectionReward(item, reward));
                        return true;
                    })
                    .requireAnyState(
                            STATE_CURRENT_TIER + "_" + currentSlot,
                            STATE_COMPLETED_TIER + "_" + currentSlot,
                            STATE_LOCKED_TIER + "_" + currentSlot
                    )
                    .build());
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
    }
}