package net.swofty.type.bedwarsgame.shop.impl;

import net.kyori.adventure.key.Key;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import org.intellij.lang.annotations.Subst;

public class Wool extends ShopItem {

	public Wool() {
		super("Wool", "Great for bridging across islands. Turns into your team's color.", 4, 16, Currency.IRON, Material.WHITE_WOOL);
	}

	@Override
	public void onPurchase(Player player) {
		@Subst("white_wool") String woolName;
		if (!player.hasTag(Tag.String("javaColor"))) {
			woolName = "white_wool";
		} else {
			woolName = player.getTag(Tag.String("javaColor")) + "_wool";
		}
		Material whiteWool = Material.fromKey(Key.key(Key.MINECRAFT_NAMESPACE, woolName));
		if (whiteWool == null) {
			whiteWool = Material.WHITE_WOOL;
		}
		player.getInventory().addItemStack(ItemStack.builder(whiteWool)
				.amount(16)
				.build());
	}

}
