package net.swofty.item.items.enchanted;


import net.swofty.item.ItemType;
import net.swofty.item.impl.Enchanted;

public class EnchantedDiamondBlock implements Enchanted {
      @Override
      public ItemType getCraftingMaterial() {
            return ItemType.DIAMOND_BLOCK;
      }
}