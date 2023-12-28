package net.swofty.item.items.enchanted;


import net.swofty.item.ItemType;
import net.swofty.item.impl.Enchanted;

public class EnchantedSugar implements Enchanted {
	@Override
	public ItemType getCraftingMaterial() {
		return ItemType.SUGAR;
	}
}