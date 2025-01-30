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
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe.GUIMinionRecipes;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.MinionComponent;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.*;

public class GUICollectionReward extends SkyBlockAbstractInventory {
    private static final Map<Integer, int[]> SLOTS = new HashMap<>(Map.of(
            0, new int[] {  },
            1, new int[] { 22 },
            2, new int[] { 20, 24 },
            3, new int[] { 20, 22, 24 },
            4, new int[] { 19, 21, 23, 25 },
            5, new int[] { 20, 21, 22, 23, 24 },
            6, new int[] { 20, 21, 22, 23, 24, 31},
            7, new int[] { 19, 20, 21, 22, 23, 24, 25},
            8, new int[] { 19, 21, 23, 25, 28, 29, 30, 31 },
            9, new int[] { 18, 19, 20, 21, 22, 23, 24, 25, 26 }
    ));

    private final ItemType item;
    private final CollectionCategory.ItemCollection category;
    private final CollectionCategory.ItemCollectionReward reward;
    private final int placement;

    public GUICollectionReward(ItemType type, CollectionCategory.ItemCollectionReward reward) {
        super(InventoryType.CHEST_6_ROW);
        this.item = type;
        this.category = CollectionCategories.getCategory(type).getCollection(type);
        this.reward = reward;
        this.placement = category.getPlacementOf(reward);

        doAction(new SetTitleAction(Component.text(type.getDisplayName() + " " +
                StringUtility.getAsRomanNumeral(placement + 1) + " Rewards")));
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
                        "§7To Collection Menu").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUICollectionItem(this.item));
                    return true;
                })
                .build());

        // Info display
        attachItem(GUIItem.builder(4)
                .item(() -> {
                    List<String> lore = new ArrayList<>(Arrays.asList(
                            "§7View your " + item.getDisplayName() + " " +
                                    StringUtility.getAsRomanNumeral(placement) + " Collection rewards!",
                            " "
                    ));

                    player.getCollection().getDisplay(lore, category, reward);

                    return ItemStackCreator.getStack("§a" + item.getDisplayName() + " " +
                                    StringUtility.getAsRomanNumeral(placement),
                            item.material, 1, lore).build();
                })
                .build());

        // Reward unlocks
        setupRewardUnlocks(player);
    }

    private void setupRewardUnlocks(SkyBlockPlayer player) {
        int[] slots = SLOTS.get(reward.unlocks().length);
        int i = 0;
        for (CollectionCategory.Unlock unlock : reward.unlocks()) {
            int slot = slots[i];
            i++;

            attachItem(GUIItem.builder(slot)
                    .item(() -> unlock.getDisplay(player).build())
                    .onClick((ctx, clickedItem) -> {
                        if (unlock instanceof CollectionCategory.UnlockRecipe) {
                            try {
                                SkyBlockItem skyBlockItem = ((CollectionCategory.UnlockRecipe) unlock)
                                        .getRecipes().getFirst().getResult();
                                if (skyBlockItem.hasComponent(MinionComponent.class)) {
                                    ctx.player().openInventory(new GUIMinionRecipes(
                                            skyBlockItem.getAttributeHandler().getMinionType(),
                                            new GUICollectionReward(item, reward)));
                                } else {
                                    ctx.player().openInventory(new GUIRecipe(
                                            skyBlockItem.getAttributeHandler().getPotentialType(), null));
                                }
                            } catch (NullPointerException exception) {
                                ctx.player().sendMessage("There is no recipe available for this item!");
                            }
                        }
                        return true;
                    })
                    .build());
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