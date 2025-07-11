package net.swofty.types.generic.event.actions.custom.bestiary;

import net.swofty.commons.StringUtility;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.types.generic.bestiary.BestiaryData;
import net.swofty.types.generic.entity.mob.BestiaryMob;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.BestiaryUpdateEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

//TODO make messages clickable to open the right mob gui

public class ActionBestiaryLevelUp implements SkyBlockEventClass {

    private final BestiaryData bestiaryData = new BestiaryData();

    @SkyBlockEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void run(BestiaryUpdateEvent event) {
        SkyBlockPlayer player = event.getPlayer();
        BestiaryMob mob = event.getBestiaryMob();
        int oldKills = event.getOldTotalValue();
        int newKills = event.getNewTotalValue();
        String mobName = mob.getDisplayName();

        int oldTier = bestiaryData.getCurrentBestiaryTier(mob, oldKills);
        int newTier = bestiaryData.getCurrentBestiaryTier(mob, newKills);

        if (newKills == 1) {
            player.sendMessage("  §3§lBESTIARY FAMILY UNLOCKED §b" + mobName);
            return;
        }

        if (newTier == oldTier + 1) {
            String romanNew = StringUtility.getAsRomanNumeral(newTier);
            String transitionLine = oldTier == 0
                    ? "§b" + mobName + " §b" + romanNew
                    : "§b" + mobName + " §8" + StringUtility.getAsRomanNumeral(oldTier) + " ➡ §b" + romanNew;

            int magicFind = bestiaryData.getMagicFind(newTier);
            int strength = bestiaryData.getStrength(newTier);
            int coinBonus = bestiaryData.getExtraCoinPercentage(newTier);
            int xpBonus = bestiaryData.getExtraXpPercentage(newTier);


            List<String> lines = new ArrayList<>();
            lines.add("");
            lines.add("§6§lREWARDS");
            lines.add("§6§lBESTIARY");
            lines.add(transitionLine);
            lines.add("");
            lines.add("§6§lREWARDS");
            lines.add("§8+§a" + magicFind + " " + mobName + " " + StringUtility.getFormatedStatistic(ItemStatistic.MAGIC_FIND));
            lines.add("§8+§a" + strength + " " + mobName + " " + StringUtility.getFormatedStatistic(ItemStatistic.STRENGTH));
            lines.add("§8+§6" + coinBonus + "% §a" + mobName + " §7coins");
            lines.add("§8+§a" + xpBonus + "% §7chance for extra XP orbs");
            lines.add("§8+§b1 SkyBlock XP");
            lines.add("");

            List<String> centered = StringUtility.centerLines(lines);

            StringBuilder message = new StringBuilder();
            message.append("§3§l").append("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬").append("\n");

            for (String line : centered) {
                message.append(line).append("\n");
            }

            message.append("§3§l").append("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

            System.out.println(message);
            player.sendMessage(message.toString());
        }
    }
}
