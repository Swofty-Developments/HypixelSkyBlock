package net.swofty.item.items.enchanted;


import net.swofty.item.ItemType;
import net.swofty.item.impl.Enchanted;
import net.swofty.item.impl.Sellable;

public class EnchantedBone implements Enchanted, Sellable {
      @Override
      public ItemType getCraftingMaterial() {
            return ItemType.BONE;
      }

      @Override
      public double getSellValue() {
            return 500;
      }
}