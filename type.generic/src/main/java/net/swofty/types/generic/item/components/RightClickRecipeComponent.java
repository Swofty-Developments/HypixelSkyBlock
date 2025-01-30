package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.types.generic.item.SkyBlockItemComponent;

public class RightClickRecipeComponent extends SkyBlockItemComponent {
    @Getter
    private final ItemType recipeItem;

    public RightClickRecipeComponent(String recipeItem) {
        this.recipeItem = ItemType.valueOf(recipeItem);
        addInheritedComponent(new InteractableComponent(
                (player, item) -> player.openInventory(new GUIRecipe(this.recipeItem, null)),
                null,
                null
        ));
    }
}