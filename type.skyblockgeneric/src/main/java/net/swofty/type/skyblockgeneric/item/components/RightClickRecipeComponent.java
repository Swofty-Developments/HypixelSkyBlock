package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.commons.item.ItemType;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

public class RightClickRecipeComponent extends SkyBlockItemComponent {
    @Getter
    private final ItemType recipeItem;

    public RightClickRecipeComponent(String recipeItem) {
        this.recipeItem = ItemType.valueOf(recipeItem);
        addInheritedComponent(new InteractableComponent(
                (player, item) -> new GUIRecipe(this.recipeItem, null).open(player),
                null,
                null
        ));
    }
}