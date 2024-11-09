package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.types.generic.item.SkyBlockItemComponent;

public class RightClickRecipeComponent extends SkyBlockItemComponent {
    @Getter
    private final ItemTypeLinker recipeItem;

    public RightClickRecipeComponent(String recipeItem) {
        this.recipeItem = ItemTypeLinker.valueOf(recipeItem);
        addInheritedComponent(new InteractableComponent(
                (player, item) -> new GUIRecipe(this.recipeItem, null).open(player),
                null,
                null
        ));
    }
}