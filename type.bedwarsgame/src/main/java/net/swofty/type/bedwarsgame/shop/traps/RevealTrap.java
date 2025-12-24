package net.swofty.type.bedwarsgame.shop.traps;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.Trap;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.utility.MathUtility;

public class RevealTrap extends Trap {

	public RevealTrap() {
		super(
				"reveal_trap",
				"Reveal Trap",
				ItemStack.of(Material.REDSTONE_TORCH),
				"Reveals intruders by glowing for 10s.",
				Currency.DIAMOND
		);
	}

	@Override
	public void onTrigger(Game game, BedWarsMapsConfig.TeamKey teamName, BedWarsPlayer triggerer) {
		triggerer.setGlowing(true);
		MathUtility.delay(
				() -> triggerer.setGlowing(false),
				10 * 20
		);
	}
}

