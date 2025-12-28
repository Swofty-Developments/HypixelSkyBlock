package net.swofty.type.skyblockgeneric.entity.mob.mobs.dwarvenmines.goblin;

import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.loottable.OtherLoot;

public class MobKnifethrower extends MobGoblin {

	@Override
	public String getMobID() {
		return "GOBLIN_KNIFE_THROWER";
	}

	@Override
	public String getDisplayName() {
		return "Knifethrower";
	}

	@Override
	public ItemStatistics getBaseStatistics() {
		return ItemStatistics.builder()
				.withBase(ItemStatistic.HEALTH, 800D)
				.withBase(ItemStatistic.DAMAGE, 300D)
				.withBase(ItemStatistic.SPEED, 100D)
				.build();
	}

	@Override
	public OtherLoot getOtherLoot() {
		return new OtherLoot(28, 10, 5);
	}

}
