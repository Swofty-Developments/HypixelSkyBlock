package net.swofty.types.generic.item.items.mining.dwarvenMines;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkillableMine;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class Mithril implements CustomSkyBlockItem, Sellable, SkillableMine {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public double getSellValue() {
        return 8;
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7§7§o\"The Man called it \"true-silver\" while",
                "§7§othe Dwarves, who loved it above all",
                "§7§othings, had their own, secret name",
                "§7§ofor it.\""));
    }

    @Override
    public SkillCategories getSkillCategory() {
        return SkillCategories.MINING;
    }

    @Override
    public double getMiningValueForSkill() {
        return 45;
    }
}
