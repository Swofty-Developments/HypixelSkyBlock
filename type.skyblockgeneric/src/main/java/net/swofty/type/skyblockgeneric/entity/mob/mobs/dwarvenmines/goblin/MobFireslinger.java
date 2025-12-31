package net.swofty.type.skyblockgeneric.entity.mob.mobs.dwarvenmines.goblin;

import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.loottable.OtherLoot;

public class MobFireslinger extends MobGoblin {

	@Override
	public Integer getLevel() {
		return 25;
	}

	@Override
	public String getMobID() {
		return "GOBLIN_FIRESLINGER";
	}

	@Override
	public String getDisplayName() {
		return "Goblin Flamethrower";
	}

	@Override
	public ItemStatistics getBaseStatistics() {
		double health = 200 + (Math.random() * ((800 - 200) + 1));
		double damage = 20 + (Math.random() * ((30 - 20) + 1));
		return ItemStatistics.builder()
				.withBase(ItemStatistic.HEALTH, health)
				.withBase(ItemStatistic.DAMAGE,  damage)
				.withBase(ItemStatistic.SPEED, 100D)
				.build();
	}

	@Override
	public OtherLoot getOtherLoot() {
		return new OtherLoot(0, 10, 5);
	}

}
