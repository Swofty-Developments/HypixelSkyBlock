package net.swofty.types.generic.item.items.mining.vanilla;

import com.mongodb.lang.Nullable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.List;

public class Coal implements CustomSkyBlockItem, SkillableMine, Sellable, MinionFuelItem, ExtraUnderNameDisplay {

    @Override
    public SkillCategories getSkillCategory() {
        return SkillCategories.MINING;
    }

    @Override
    public double getMiningValueForSkill() {
        return 5;
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Brewing Ingredient";
    }

    @Override
    public List<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return List.of("§7Increases the speed of",
                "§7your minion by §a5% §7for 30",
                "§7minutes.");
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 2;
    }

    @Override
    public double getMinionFuelPercentage() {
        return 5;
    }

    @Override
    public long getFuelLastTimeInMS() {
        return 1800000; // 30 Minutes
    }
}
