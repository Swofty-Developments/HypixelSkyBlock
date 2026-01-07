package net.swofty.type.skyblockgeneric.item.components;

import net.minestom.server.item.ItemAnimation;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;

import java.util.List;

public class EnchantedComponent extends SkyBlockItemComponent {
    private CraftableComponent craftableComponent;

    public EnchantedComponent(SkyBlockRecipe.RecipeType type, String enchantedItem, String nonEnchantedID) {
        addInheritedComponent(new DisableAnimationComponent(
                List.of(ItemAnimation.EAT)
        ));

        // Create a standard enchanted recipe
        SkyBlockRecipe<?> recipe = SkyBlockRecipe.getStandardEnchantedRecipe(
                type,
                ItemType.valueOf(nonEnchantedID),  // Base material
                ItemType.valueOf(enchantedItem)    // Result
        );

        this.craftableComponent = new CraftableComponent(List.of(recipe), false);
        addInheritedComponent(craftableComponent);
    }

    public EnchantedComponent(List<SkyBlockRecipe<?>> customRecipes) {
        addInheritedComponent(new DisableAnimationComponent(
                List.of(ItemAnimation.EAT)
        ));

        this.craftableComponent = new CraftableComponent(customRecipes, false);
        addInheritedComponent(craftableComponent);
    }

    public EnchantedComponent() {
        addInheritedComponent(new DisableAnimationComponent(
                List.of(ItemAnimation.EAT)
        ));
    }

    public List<SkyBlockRecipe<?>> getRecipes() {
        return craftableComponent != null ? craftableComponent.getRecipes() : List.of();
    }
}