package net.swofty.type.skyblockgeneric.item.components;

import net.minestom.server.item.ItemAnimation;
import net.swofty.commons.item.ItemType;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;

import java.util.List;

public class EnchantedComponent extends SkyBlockItemComponent {
    public EnchantedComponent(SkyBlockRecipe.RecipeType type, String enchantedItem, String nonEnchantedID) {
        addInheritedComponent(new DisableAnimationComponent(
                List.of(ItemAnimation.EAT)
        ));
        addInheritedComponent(new CraftableComponent(getStandardEnchantedRecipe(
                type,
                ItemType.valueOf(enchantedItem),
                ItemType.valueOf(nonEnchantedID)
        ), true));
    }

    public EnchantedComponent() {
        addInheritedComponent(new DisableAnimationComponent(
                List.of(ItemAnimation.EAT)
        ));
    }

    public SkyBlockRecipe<?> getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType type, ItemType enchantedItem, ItemType nonEnchantedID) {
        return SkyBlockRecipe.getStandardEnchantedRecipe(type, enchantedItem, nonEnchantedID);
    }
}