package net.swofty.types.generic.item.components;

import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;

import java.util.List;

public class EnchantedComponent extends SkyBlockItemComponent {
    public EnchantedComponent(SkyBlockRecipe.RecipeType type, String itemId) {
        addInheritedComponent(new DisableAnimationComponent(
                List.of(PlayerItemAnimationEvent.ItemAnimationType.EAT)
        ));
        addInheritedComponent(new CraftableComponent(getStandardEnchantedRecipe(
                type,
                ItemType.valueOf(itemId)
        )));
    }

    public EnchantedComponent() {
        addInheritedComponent(new DisableAnimationComponent(
                List.of(PlayerItemAnimationEvent.ItemAnimationType.EAT)
        ));
    }

    public SkyBlockRecipe<?> getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType type, ItemType material) {
        return SkyBlockRecipe.getStandardEnchantedRecipe(type, material);
    }
}