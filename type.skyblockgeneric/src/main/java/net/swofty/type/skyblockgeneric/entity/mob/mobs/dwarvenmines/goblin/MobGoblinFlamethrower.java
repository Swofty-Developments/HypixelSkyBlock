package net.swofty.type.skyblockgeneric.entity.mob.mobs.dwarvenmines.goblin;

import lombok.NonNull;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.loottable.OtherLoot;
import net.swofty.type.skyblockgeneric.loottable.SkyBlockLootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MobGoblinFlamethrower extends MobGoblin {

	@Override
	public Integer getLevel() {
		return 100;
	}

	@Override
	public String getMobID() {
		return "GOBLIN_FLAMETHROWER";
	}

	@Override
	public String getDisplayName() {
		return "Goblin Flamethrower";
	}

	@Override
	public ItemStatistics getBaseStatistics() {
		return ItemStatistics.builder()
				.withBase(ItemStatistic.HEALTH, 20000D)
				.withBase(ItemStatistic.DAMAGE,  500D)
				.withBase(ItemStatistic.SPEED, 100D)
				.build();
	}

	@Override
	public @Nullable SkyBlockLootTable getLootTable() {
		return new SkyBlockLootTable() {
			@Override
			public @NonNull List<LootRecord> getLootTable() {
				return List.of(
						new LootRecord(ItemType.GOBLIN_EGG, 1, 0.6),
						new LootRecord(ItemType.GREEN_GOBLIN_EGG, 1, 0.25),
						new LootRecord(ItemType.YELLOW_GOBLIN_EGG, 1, 0.1),
						new LootRecord(ItemType.RED_GOBLIN_EGG, 1, 0.04),
						new LootRecord(ItemType.BLUE_GOBLIN_EGG, 1, 0.02)
				);
			}

			@Override
			public @NotNull CalculationMode getCalculationMode() {
				return CalculationMode.CALCULATE_INDIVIDUAL;
			}
		};
	}

	@Override
	public OtherLoot getOtherLoot() {
		return new OtherLoot(40, 10, 10);
	}
}
