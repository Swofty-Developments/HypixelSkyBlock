package net.swofty.item.items.enchanted;


import net.swofty.item.ItemType;
import net.swofty.item.impl.Enchanted;

public class EnchantedCoal implements Enchanted {
      @Override
      public ItemType getCraftingMaterial() {
            return ItemType.COAL;
      }
}