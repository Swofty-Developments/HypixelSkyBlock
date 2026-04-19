package net.swofty.type.skyblockgeneric.tabmodules;

import net.kyori.adventure.text.Component;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.TablistSkinRegistry;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointBankData;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointSkillCategory;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointSkills;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AccountInformationModule extends TablistModule {

    public List<TablistEntry> getEntries(SkyBlockPlayer player) {
        Locale l = player.getLocale();
        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered(I18n.string("tablist.module.account_info", l)), TablistSkinRegistry.ORANGE)
        ));

        SkyBlockDataHandler dataHandler = player.getSkyblockDataHandler();
        DatapointBankData.BankData bankData = dataHandler.get(SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class).getValue();

        entries.add(new TablistEntry(I18n.string("tablist.account_info.profile_label", l, Component.text(dataHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue())), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(I18n.string("tablist.account_info.pet_sitter", l), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(I18n.string("tablist.account_info.bank", l, Component.text(StringUtility.shortenNumber(bankData.getAmount())), Component.text(StringUtility.shortenNumber(bankData.getBalanceLimit()))), TablistSkinRegistry.GRAY));
        entries.add(getGrayEntry());

        SkillCategories skillCategory = dataHandler.get(SkyBlockDataHandler.Data.LAST_EDITED_SKILL, DatapointSkillCategory.class).getValue();
        DatapointSkills.PlayerSkills skills = player.getSkills();
        ItemStatistics playerStatistics = player.getStatistics().allStatistics();
        entries.add(new TablistEntry(I18n.string("tablist.account_info.skills_label", l, Component.text(String.valueOf(skillCategory)), Component.text(String.valueOf(skills.getCurrentLevel(skillCategory))), Component.text(String.valueOf(skills.getPercentage(skillCategory)))), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(I18n.string("tablist.account_info.speed", l, Component.text(ItemStatistic.SPEED.getDisplayColor()), Component.text(ItemStatistic.SPEED.getSymbol()), Component.text(String.valueOf(playerStatistics.getOverall(ItemStatistic.SPEED)))), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(I18n.string("tablist.account_info.strength", l, Component.text(ItemStatistic.STRENGTH.getDisplayColor()), Component.text(ItemStatistic.STRENGTH.getSymbol()), Component.text(String.valueOf(playerStatistics.getOverall(ItemStatistic.STRENGTH)))), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(I18n.string("tablist.account_info.crit_chance", l, Component.text(ItemStatistic.CRITICAL_CHANCE.getDisplayColor()), Component.text(ItemStatistic.CRITICAL_CHANCE.getSymbol()), Component.text(String.valueOf(playerStatistics.getOverall(ItemStatistic.CRITICAL_CHANCE)))), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(I18n.string("tablist.account_info.crit_damage", l, Component.text(ItemStatistic.CRITICAL_DAMAGE.getDisplayColor()), Component.text(ItemStatistic.CRITICAL_DAMAGE.getSymbol()), Component.text(String.valueOf(playerStatistics.getOverall(ItemStatistic.CRITICAL_DAMAGE)))), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(I18n.string("tablist.account_info.attack_speed", l, Component.text(ItemStatistic.BONUS_ATTACK_SPEED.getDisplayColor()), Component.text(ItemStatistic.BONUS_ATTACK_SPEED.getSymbol()), Component.text(String.valueOf(playerStatistics.getOverall(ItemStatistic.BONUS_ATTACK_SPEED)))), TablistSkinRegistry.GRAY));
        entries.add(getGrayEntry());
        fillRestWithGray(entries);
        return entries;
    }

    @Override
    public List<TablistEntry> getEntries(HypixelPlayer player) {
        return getEntries((SkyBlockPlayer) player);
    }
}
