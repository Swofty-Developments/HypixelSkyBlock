package net.swofty.types.generic.event.actions.custom.skill;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.Event;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.data.datapoints.DatapointSkills;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.SkillUpdateEvent;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.skill.SkillCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.Arrays;

@EventParameters(description = "Handles the level up message in regards to Skills",
        node = EventNodes.CUSTOM,
        requireDataLoaded = false)
public class ActionSkillLevelUp extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return SkillUpdateEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        SkillUpdateEvent event = (SkillUpdateEvent) tempEvent;
        if (event.getNewValue() <= event.getOldValue()) return;

        SkyBlockPlayer player = event.getPlayer();
        DatapointSkills.PlayerSkills skills = player.getSkills();
        SkillCategories skillCategory = event.getSkillCategory();

        int oldLevel = skillCategory.asCategory().getLevel(event.getOldValue());
        int newLevel = skillCategory.asCategory().getLevel(event.getNewValue());

        if (oldLevel == newLevel) return;

        String oldLevelDisplay = StringUtility.getAsRomanNumeral(oldLevel);

        player.sendMessage("§3§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        player.sendMessage(Component.text("  §b§lSKILL LEVEL UP §3" + skillCategory + " §8" +
                        (oldLevelDisplay.isEmpty() ? "0" : oldLevelDisplay) + ">§e" +
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
                    case XP -> {
                        player.sendMessage("    §8+§b" + ((SkillCategory.XPReward) unlock).getXP() + " SkyBlock XP");
                    }
                    case COINS -> {
                        player.sendMessage("    §8+§6" + ((SkillCategory.CoinReward) unlock).getCoins() + " §7Coins");
                    }
                    case STATS -> {
                        player.sendMessage("    §8+§b" + ((SkillCategory.StatisticReward) unlock).getStatistic().getColour() +
                                ((SkillCategory.StatisticReward) unlock).getStatistic().getSymbol() +
                                ((SkillCategory.StatisticReward) unlock).amountAdded()
                                + " " + ((SkillCategory.StatisticReward) unlock).getStatistic().getDisplayName());
                    }
                    case REGION_ACCESS -> {
                        player.sendMessage("    §8+§aAccess to " + ((SkillCategory.RegionReward) unlock).getRegion());
                    }
                }

                unlock.onUnlock(player);
            });
        }

        player.sendMessage("§3§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }
}
