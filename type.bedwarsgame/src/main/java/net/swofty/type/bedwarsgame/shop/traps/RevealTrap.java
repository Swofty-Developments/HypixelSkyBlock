package net.swofty.type.bedwarsgame.shop.traps;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.Trap;

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
	public void onTrigger(Game game, String teamName, Player triggerer) {
		triggerer.setGlowing(true);
		MinecraftServer.getSchedulerManager().buildTask(() -> triggerer.setGlowing(false))
				.delay(TaskSchedule.seconds(10)).schedule();
	}
}

