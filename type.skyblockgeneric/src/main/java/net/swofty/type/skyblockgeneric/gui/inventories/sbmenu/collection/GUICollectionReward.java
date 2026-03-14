package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.collection;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.collection.CollectionCategories;
import net.swofty.type.skyblockgeneric.collection.CollectionCategory;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe.GUIMinionRecipes;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.MinionComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;

public class GUICollectionReward extends StatelessView {
    private static final Map<Integer, int[]> SLOTS = new HashMap<>(Map.of(
            0, new int[]{},
            1, new int[]{22},
            2, new int[]{20, 24},
            3, new int[]{20, 22, 24},
            4, new int[]{19, 21, 23, 25},
            5, new int[]{20, 21, 22, 23, 24},
            6, new int[]{20, 21, 22, 23, 24, 31},
            7, new int[]{19, 20, 21, 22, 23, 24, 25},
            8, new int[]{19, 21, 23, 25, 28, 29, 30, 31},
            9, new int[]{18, 19, 20, 21, 22, 23, 24, 25, 26}
    ));

    private final ItemType item;
    private final CollectionCategory.ItemCollection category;
    private final CollectionCategory.ItemCollectionReward reward;
    private final int placement;

    public GUICollectionReward(ItemType type, CollectionCategory.ItemCollectionReward reward) {
        this.item = type;
        this.category = CollectionCategories.getCategory(type).getCollection(type);
        this.reward = reward;
        this.placement = category.getPlacementOf(reward);
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(item.getDisplayName() + " " +
                StringUtility.getAsRomanNumeral(placement + 1) + " Rewards", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            List<String> lore = new ArrayList<>(Arrays.asList(
                    "§7View your " + item.getDisplayName() + " " + StringUtility.getAsRomanNumeral(placement + 1) + " Collection rewards!",
                    " "
            ));
            player.getCollection().getDisplay(lore, category, reward);
            return ItemStackCreator.getStack("§a" + item.getDisplayName() + " " + StringUtility.getAsRomanNumeral(placement + 1),
                    item.material, 1, lore);
        });

        int[] slots = SLOTS.getOrDefault(reward.unlocks().length, new int[]{});
        for (int i = 0; i < reward.unlocks().length && i < slots.length; i++) {
            CollectionCategory.Unlock unlock = reward.unlocks()[i];
            int slot = slots[i];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                return unlock.getDisplay(player);
            }, (click, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                if (unlock instanceof CollectionCategory.UnlockRecipe) {
                    try {
                        SkyBlockItem skyBlockItem = ((CollectionCategory.UnlockRecipe) unlock).getRecipes().getFirst().getResult();
                        if (skyBlockItem.hasComponent(MinionComponent.class)) {
                            c.push(new GUIMinionRecipes(skyBlockItem.getAttributeHandler().getMinionType()));
                        } else {
                            c.push(new GUIRecipe(skyBlockItem.getAttributeHandler().getPotentialType()));
                        }
                    } catch (NullPointerException exception) {
                        player.sendMessage("There is no recipe available for this item!");
                    }
                }
            });
        }
    }
}
