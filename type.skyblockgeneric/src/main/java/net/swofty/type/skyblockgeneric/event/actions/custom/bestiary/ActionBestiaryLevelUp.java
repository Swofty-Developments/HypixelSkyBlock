package net.swofty.type.skyblockgeneric.event.actions.custom.bestiary;

import net.swofty.commons.ChatUtility;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.bestiary.BestiaryData;
import net.swofty.type.skyblockgeneric.entity.mob.BestiaryMob;
import net.swofty.type.skyblockgeneric.event.custom.BestiaryUpdateEvent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

//TODO make messages clickable to open the right mob gui

public class ActionBestiaryLevelUp implements HypixelEventClass {

	private final BestiaryData bestiaryData = new BestiaryData();

	@HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
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
			lines.add("§6§lBESTIARY");
			lines.add(transitionLine);
			lines.add("");
			lines.add("§6§lREWARDS");
			lines.add("§8+§a" + magicFind + " " + mobName + " " + ItemStatistic.MAGIC_FIND.getFullDisplayName());
			lines.add("§8+§a" + strength + " " + mobName + " " + ItemStatistic.STRENGTH.getFullDisplayName());
			lines.add("§8+§6" + coinBonus + "% §a" + mobName + " §7coins");
			lines.add("§8+§a" + xpBonus + "% §7chance for extra XP orbs");
			lines.add("§8+§b1 SkyBlock XP");
			lines.add("");

			// This could be sent in a single message, but the way it was done was incorrect. So we'll keep it like this for now.
			List<String> centered = ChatUtility.FontInfo.centerLines(lines);
			player.sendMessage("§3§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
			for (String line : centered) {
				player.sendMessage(line);
			}
			player.sendMessage("§3§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		}
	}
}
