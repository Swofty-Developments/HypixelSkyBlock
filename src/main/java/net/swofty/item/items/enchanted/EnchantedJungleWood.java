package net.swofty.item.items.enchanted;


import net.swofty.item.ItemType;
import net.swofty.item.impl.Enchanted;

public class EnchantedJungleWood implements Enchanted {
      @Override
      public ItemType getCraftingMaterial() {
            return ItemType.JUNGLE_WOOD;
      }
}