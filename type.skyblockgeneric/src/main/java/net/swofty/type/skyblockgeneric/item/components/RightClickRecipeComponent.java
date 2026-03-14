package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

public class RightClickRecipeComponent extends SkyBlockItemComponent {
    @Getter
    private final ItemType recipeItem;

    public RightClickRecipeComponent(String recipeItem) {
        this.recipeItem = ItemType.valueOf(recipeItem);
        addInheritedComponent(new InteractableComponent(
                (player, item) -> player.openView(new GUIRecipe(this.recipeItem)),
                null,
                null
        ));
    }
}