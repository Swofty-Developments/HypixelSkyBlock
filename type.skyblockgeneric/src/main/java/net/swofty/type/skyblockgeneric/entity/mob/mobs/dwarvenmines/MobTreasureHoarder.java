package net.swofty.type.skyblockgeneric.entity.mob.mobs.dwarvenmines;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.gui.inventory.item.GUIMaterial;
import net.swofty.type.skyblockgeneric.entity.mob.BestiaryMob;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.entity.mob.impl.RegionPopulator;
import net.swofty.type.skyblockgeneric.loottable.OtherLoot;
import net.swofty.type.skyblockgeneric.loottable.SkyBlockLootTable;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MobTreasureHoarder extends BestiaryMob implements RegionPopulator {

	public MobTreasureHoarder() {
		super(EntityType.PLAYER);
	}

	@Override
	public int getMaxBestiaryTier() {
		return 0;
	}

	@Override
	public int getBestiaryBracket() {
		return 0;
	}

	@Override
	public String getMobID() {
		return "TREASURE_HOARDER";
	}

	@Override
	public GUIMaterial getGuiMaterial() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return "Treasure Hoarder";
	}

	@Override
	public Integer getLevel() {
		return 0;
	}

	@Override
	public List<GoalSelector> getGoalSelectors() {
		return List.of();
	}

	@Override
	public List<TargetSelector> getTargetSelectors() {
		return List.of();
	}

	@Override
	public ItemStatistics getBaseStatistics() {
		return null;
	}

	@Override
	public @Nullable SkyBlockLootTable getLootTable() {
		return null;
	}

	@Override
	public SkillCategories getSkillCategory() {
		return null;
	}

	@Override
	public long damageCooldown() {
		return 0;
	}

	@Override
	public OtherLoot getOtherLoot() {
		return null;
	}

	@Override
	public List<MobType> getMobTypes() {
		return List.of();
	}

	@Override
	public List<Populator> getPopulators() {
		return List.of();
	}
}
