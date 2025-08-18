package net.swofty.type.bedwarsgame.shop.impl;

import net.kyori.adventure.key.Key;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import org.intellij.lang.annotations.Subst;

public class HardenedClay extends ShopItem {

	public HardenedClay() {
		super("Hardened Clay", "Great for bridging across islands. Turns into your team's color.", 12, 16, Currency.IRON, Material.TERRACOTTA);
	}

	@Override
	public void onBought(Player player) {
		@Subst("white_terracotta") String woolName;
		if (!player.hasTag(Tag.String("teamColor"))) {
			woolName = "white_terracotta";
		} else {
			woolName = player.getTag(Tag.String("teamColor")) + "_terracotta";
		}
		Material whiteWool = Material.fromKey(Key.key(Key.MINECRAFT_NAMESPACE, woolName));
		if (whiteWool == null) {
			whiteWool = Material.WHITE_TERRACOTTA;
		}
		player.getInventory().addItemStack(ItemStack.builder(whiteWool)
				.amount(16)
				.build());
	}

}
