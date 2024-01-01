package net.swofty.item.items.enchanted;


import net.swofty.item.ItemType;
import net.swofty.item.impl.Enchanted;
import net.swofty.item.impl.SkyBlockRecipe;

public class EnchantedPumpkin implements Enchanted {
      @Override
      public ItemType getCraftingMaterial() {
            return ItemType.PUMPKIN;
      }

      @Override
      public SkyBlockRecipe.RecipeType getRecipeType() {
            return SkyBlockRecipe.RecipeType.FARMING;
      }
}