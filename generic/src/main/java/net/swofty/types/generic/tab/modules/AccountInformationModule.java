package net.swofty.types.generic.tab.modules;

import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointSkillCategory;
import net.swofty.types.generic.data.datapoints.DatapointSkills;
import net.swofty.types.generic.data.datapoints.DatapointString;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.tab.TablistModule;
import net.swofty.types.generic.tab.TablistSkinRegistry;
import net.swofty.types.generic.user.PlayerProfiles;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.List;

public class AccountInformationModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(SkyBlockPlayer player) {
        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered("§6§lAccount Info"), TablistSkinRegistry.ORANGE)
        ));

        DataHandler dataHandler = player.getDataHandler();

        entries.add(new TablistEntry("§e§lProfile: §a" +
                dataHandler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue(),
                TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Pet Sitter: §bN/A", TablistSkinRegistry.GRAY));
        entries.add(getGrayEntry());

        SkillCategories skillCategory = dataHandler.get(DataHandler.Data.LAST_EDITED_SKILL, DatapointSkillCategory.class).getValue();
        DatapointSkills.PlayerSkills skills = player.getSkills();
        ItemStatistics playerStatistics = player.getStatistics().allStatistics();
        entries.add(new TablistEntry("§e§lSkills: §a" +
                skillCategory + " " + skills.getCurrentLevel(skillCategory) + ": §3" + skills.getPercentage(skillCategory) + "%",
                TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Speed: " + ItemStatistic.SPEED.getColour() +
                ItemStatistic.SPEED.getSymbol() + playerStatistics.get(ItemStatistic.SPEED),
                TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Strength: " + ItemStatistic.STRENGTH.getColour() +
                ItemStatistic.STRENGTH.getSymbol() + playerStatistics.get(ItemStatistic.STRENGTH),
                TablistSkinRegistry.GRAY));

        fillRestWithGray(entries);

        return entries;
    }
}
