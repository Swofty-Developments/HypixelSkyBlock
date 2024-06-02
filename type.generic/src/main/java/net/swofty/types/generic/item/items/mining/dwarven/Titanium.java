package net.swofty.types.generic.item.items.mining.dwarven;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkillableMine;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class Titanium implements CustomSkyBlockItem, Sellable, SkillableMine, SkullHead {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 20;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "a14c6e41a762d37863a9fff6888c738905b92cc6c3898892a38dfdfe2ac4bf";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7§7§oIts strength and resistance",
                "§7§oto corrosion are the highest of",
                "§7§oany metallic element."));
    }

    @Override
    public SkillCategories getSkillCategory() {
        return SkillCategories.MINING;
    }

    @Override
    public double getMiningValueForSkill() {
        return 0; //not on the wiki, might be wrong
    }
}
