package net.swofty.types.generic.item.components;

import net.minestom.server.item.ItemAnimation;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;

import java.util.List;

public class EnchantedComponent extends SkyBlockItemComponent {
    public EnchantedComponent(SkyBlockRecipe.RecipeType type, String itemId) {
        addInheritedComponent(new DisableAnimationComponent(
                List.of(ItemAnimation.EAT)
        ));
        addInheritedComponent(new CraftableComponent(getStandardEnchantedRecipe(
                type,
                ItemType.valueOf(itemId)
        ), true));
    }

    public EnchantedComponent() {
        addInheritedComponent(new DisableAnimationComponent(
                List.of(ItemAnimation.EAT)
        ));
    }

    public SkyBlockRecipe<?> getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType type, ItemType material) {
        return SkyBlockRecipe.getStandardEnchantedRecipe(type, material);
    }
}