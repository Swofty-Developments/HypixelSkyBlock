package net.swofty.types.generic.item.items.mining.vanilla;

import net.swofty.types.generic.block.BlockType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.PlaceableCustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkillableMine;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class Mycelium implements PlaceableCustomSkyBlockItem, Sellable, SkillableMine {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public @Nullable BlockType getAssociatedBlockType() {
        return null;
    }

    @Override
    public double getSellValue() {
        return 5;
    }

    @Override
    public SkillCategories getSkillCategory() {
        return SkillCategories.MINING;
    }

    @Override
    public double getMiningValueForSkill() {
        return 3;
    }
}
