package net.swofty.type.bedwarsgame.item.impl;

import net.minestom.server.entity.Player;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.bedwarsgame.item.SimpleInteractableItem;

public class GoldenApple extends SimpleInteractableItem {

	public GoldenApple() {
		super("golden_apple");
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStack.of(Material.GOLDEN_APPLE);
	}

	@Override
	public void onItemFinishUse(PlayerFinishItemUseEvent event) {
		Player player = event.getPlayer();
		player.setHealth((float) (player.getHealth() + 4.0));
		player.setAdditionalHearts((float) (player.getAdditionalHearts() + 2.0));
		player.addEffect(new Potion(
				PotionEffect.REGENERATION,
				1,
				20 * 20
		));
		player.addEffect(new Potion(
				PotionEffect.RESISTANCE,
				1,
				5 * 60 * 20
		));
	}

}