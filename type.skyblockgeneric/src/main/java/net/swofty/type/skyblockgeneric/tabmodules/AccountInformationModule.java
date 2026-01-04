package net.swofty.type.skyblockgeneric.tabmodules;

import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointBankData;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointSkillCategory;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointSkills;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.TablistSkinRegistry;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class AccountInformationModule extends TablistModule {

    public List<TablistEntry> getEntries(SkyBlockPlayer player) {
        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered("§6§lAccount Info"), TablistSkinRegistry.ORANGE)
        ));

        SkyBlockDataHandler dataHandler = player.getSkyblockDataHandler();
        DatapointBankData.BankData bankData = dataHandler.get(SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class).getValue();

        entries.add(new TablistEntry("§e§lProfile: §a" +
                dataHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue(),
                TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Pet Sitter: §bN/A", TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Bank: §6" + StringUtility.shortenNumber(bankData.getAmount())
                + " §7/ §6" + StringUtility.shortenNumber(bankData.getBalanceLimit()), TablistSkinRegistry.GRAY));
        entries.add(getGrayEntry());

        SkillCategories skillCategory = dataHandler.get(SkyBlockDataHandler.Data.LAST_EDITED_SKILL, DatapointSkillCategory.class).getValue();
        DatapointSkills.PlayerSkills skills = player.getSkills();
        ItemStatistics playerStatistics = player.getStatistics().allStatistics();
        entries.add(new TablistEntry("§e§lSkills: §a" +
                skillCategory + " " + skills.getCurrentLevel(skillCategory) + ": §3" + skills.getPercentage(skillCategory) + "%",
                TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Speed: " + ItemStatistic.SPEED.getDisplayColor() +
                ItemStatistic.SPEED.getSymbol() + playerStatistics.getOverall(ItemStatistic.SPEED),
                TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Strength: " + ItemStatistic.STRENGTH.getDisplayColor() +
                ItemStatistic.STRENGTH.getSymbol() + playerStatistics.getOverall(ItemStatistic.STRENGTH),
                TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Crit Chance: " + ItemStatistic.CRITICAL_CHANCE.getDisplayColor() +
                ItemStatistic.CRITICAL_CHANCE.getSymbol() + playerStatistics.getOverall(ItemStatistic.CRITICAL_CHANCE),
                TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Crit Damage: " + ItemStatistic.CRITICAL_DAMAGE.getDisplayColor() +
                ItemStatistic.CRITICAL_DAMAGE.getSymbol() + playerStatistics.getOverall(ItemStatistic.CRITICAL_DAMAGE),
                TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Attack Speed: " + ItemStatistic.BONUS_ATTACK_SPEED.getDisplayColor() +
                ItemStatistic.BONUS_ATTACK_SPEED.getSymbol() + playerStatistics.getOverall(ItemStatistic.BONUS_ATTACK_SPEED),
                TablistSkinRegistry.GRAY));
        entries.add(getGrayEntry());
        fillRestWithGray(entries);
        return entries;
    }

    @Override
    public List<TablistEntry> getEntries(HypixelPlayer player) {
        return getEntries((SkyBlockPlayer) player);
    }
}
