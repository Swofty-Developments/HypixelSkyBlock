package net.swofty.types.generic.event.actions.custom.skill;

import net.kyori.adventure.text.Component;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.SkillUpdateEvent;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.skill.SkillCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.utility.StringUtility;

import java.util.Arrays;

public class ActionSkillLevelUp implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(SkillUpdateEvent event) {
        if (event.getNewValueRaw() <= event.getOldValueRaw()) return;

        SkyBlockPlayer player = event.getPlayer();
        SkillCategories skillCategory = event.getSkillCategory();

        int oldLevel = skillCategory.asCategory().getLevel(event.getOldValueRaw());
        int newLevel = skillCategory.asCategory().getLevel(event.getNewValueRaw());

        if (oldLevel == newLevel) return;

        String oldLevelDisplay = StringUtility.getAsRomanNumeral(oldLevel);

        player.sendMessage("§3§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        player.sendMessage(Component.text("  §b§lSKILL LEVEL UP §3" + skillCategory + " §8" +
                        (oldLevelDisplay.isEmpty() ? "§e" : oldLevelDisplay + ">§e") +
                        StringUtility.getAsRomanNumeral(newLevel))
                .hoverEvent(Component.text("§eClick to view your " + skillCategory + " Skill progress!"))
                .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/viewskill " + skillCategory.toString().toUpperCase()))
        );

        SkillCategory.SkillReward reward = skillCategory.asCategory().getReward(newLevel);

        if (reward.unlocks().length != 0) {
            player.sendMessage(" ");
            player.sendMessage("  §a§lREWARDS");
            Arrays.stream(reward.unlocks()).forEach(unlock -> {
                switch (unlock.type()) {
                    case XP -> player.sendMessage("    §8+§b" + ((SkillCategory.XPReward) unlock).getXP() + " SkyBlock XP");
                    case COINS -> player.sendMessage("    §8+§6" + ((SkillCategory.CoinReward) unlock).getCoins() + " §7Coins");
                    case STATS_BASE -> {
                        ItemStatistic statistic = ((SkillCategory.BaseStatisticReward) unlock).getStatistic();
                        player.sendMessage("    §8+§a" + StringUtility.decimalify(((SkillCategory.BaseStatisticReward) unlock).amountAdded(), 1)
                                + statistic.getSuffix() + " " + statistic.getDisplayColor() + statistic.getSymbol() +  " " + statistic.getDisplayName());
                    }
                    case STATS_ADDITIVE_PERCENTAGE -> {
                        ItemStatistic statistic = ((SkillCategory.BaseStatisticReward) unlock).getStatistic();
                        player.sendMessage("    §8+§a" + StringUtility.decimalify(((SkillCategory.BaseStatisticReward) unlock).amountAdded(), 1) +
                                "% " + statistic.getDisplayColor() + statistic.getSymbol() + " " + statistic.getDisplayName());
                    }
                    case REGION_ACCESS -> player.sendMessage("    §8+§aAccess to " + ((SkillCategory.RegionReward) unlock).getRegion());
                }

                unlock.onUnlock(player);
            });
        }

        player.sendMessage("§3§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }
}
