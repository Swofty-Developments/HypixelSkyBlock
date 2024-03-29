package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public interface RightClickRecipe extends Interactable {

    ItemType getRecipeItem();
    @Override
    default void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        new GUIRecipe(getRecipeItem(), null).open(player);
    }
}
