package net.swofty.item.items.enchanted;


import net.swofty.item.ItemType;
import net.swofty.item.impl.Enchanted;

public class EnchantedCarrot implements Enchanted {
      @Override
      public ItemType getCraftingMaterial() {
            return ItemType.CARROT;
      }
}