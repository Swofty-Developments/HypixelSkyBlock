package net.swofty.type.bedwarsgame.shop.traps;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.Trap;

public class BlindnessTrap extends Trap {

	public BlindnessTrap() {
		super(
				"blindness_trap",
				"Blindness Trap",
				ItemStack.of(Material.TRIPWIRE_HOOK),
				"Blinds intruders for 10 seconds.",
				Currency.DIAMOND
		);
	}

	@Override
	public void onTrigger(Game game, String teamName, Player triggerer) {
		triggerer.addEffect(new Potion(PotionEffect.BLINDNESS, (byte) 1, 200));
	}
}

