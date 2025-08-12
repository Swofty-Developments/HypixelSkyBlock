package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.collection;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.skyblockgeneric.collection.CollectionCategories;
import net.swofty.type.skyblockgeneric.collection.CollectionCategory;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe.GUIMinionRecipes;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.MinionComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;

public class GUICollectionReward extends HypixelInventoryGUI {
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
        super(type.getDisplayName() + " "
                + StringUtility.getAsRomanNumeral(
                        CollectionCategories.getCategory(type).getCollection(type).getPlacementOf(reward) + 1)
                + " Rewards", InventoryType.CHEST_6_ROW);

        this.item = type;
        this.category = CollectionCategories.getCategory(type).getCollection(type);
        this.reward = reward;
        this.placement = category.getPlacementOf(reward);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUICollectionItem(item)));

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(net.swofty.type.generic.user.HypixelPlayer p) {
                net.swofty.type.skyblockgeneric.user.SkyBlockPlayer player = (net.swofty.type.skyblockgeneric.user.SkyBlockPlayer) p; 
                List<String> lore = new ArrayList<>(Arrays.asList(
                        "§7View your " + item.getDisplayName() + " " + StringUtility.getAsRomanNumeral(placement) + " Collection rewards!",
                        " "
                ));

                player.getCollection().getDisplay(lore, category, reward);

                return ItemStackCreator.getStack("§a" + item.getDisplayName() + " " + StringUtility.getAsRomanNumeral(placement),
                        item.material, 1, lore);
            }
        });

        int[] slots = SLOTS.get(reward.unlocks().length);
        int i = 0;
        for (CollectionCategory.Unlock unlock : reward.unlocks()) {
            int slot = slots[i];
            i++;

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, net.swofty.type.generic.user.HypixelPlayer p) {
                net.swofty.type.skyblockgeneric.user.SkyBlockPlayer player = (net.swofty.type.skyblockgeneric.user.SkyBlockPlayer) p; 
                    if (unlock instanceof CollectionCategory.UnlockRecipe) {
                        try {
                            SkyBlockItem skyBlockItem = ((CollectionCategory.UnlockRecipe) unlock).getRecipes().getFirst().getResult();
                            if (skyBlockItem.hasComponent(MinionComponent.class)) {
                                new GUIMinionRecipes(skyBlockItem.getAttributeHandler().getMinionType(), new GUICollectionReward(item, reward)).open(player);
                            } else {
                                new GUIRecipe(skyBlockItem.getAttributeHandler().getPotentialType(), null).open(player);
                            }
                        } catch (NullPointerException exception) {
                            player.sendMessage("There is no recipe available for this item!");
                        }
                    }
                }

                @Override
                public ItemStack.Builder getItem(net.swofty.type.generic.user.HypixelPlayer p) {
                net.swofty.type.skyblockgeneric.user.SkyBlockPlayer player = (net.swofty.type.skyblockgeneric.user.SkyBlockPlayer) p; 
                    return unlock.getDisplay(player);
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
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
}
