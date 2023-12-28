package net.swofty.item.items.enchanted;


import net.swofty.item.ItemType;
import net.swofty.item.impl.Enchanted;

public class EnchantedDarkOakWood implements Enchanted {
      @Override
      public ItemType getCraftingMaterial() {
            return ItemType.DARK_OAK_WOOD;
      }
}