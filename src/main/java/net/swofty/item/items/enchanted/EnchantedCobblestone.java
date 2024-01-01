package net.swofty.item.items.enchanted;


import net.swofty.item.ItemType;
import net.swofty.item.impl.Enchanted;
import net.swofty.item.impl.SkyBlockRecipe;

public class EnchantedCobblestone implements Enchanted {
      @Override
      public ItemType getCraftingMaterial() {
            return ItemType.COBBLESTONE;
      }

      @Override
      public SkyBlockRecipe.RecipeType getRecipeType() {
            return SkyBlockRecipe.RecipeType.MINING;
      }
}