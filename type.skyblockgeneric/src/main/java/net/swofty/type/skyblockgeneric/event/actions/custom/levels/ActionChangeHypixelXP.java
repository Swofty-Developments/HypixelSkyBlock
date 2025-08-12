package net.swofty.type.skyblockgeneric.event.actions.custom.levels;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.custom.SkyBlockXPModificationEvent;
import net.swofty.type.generic.levels.SkyBlockLevelCause;
import net.swofty.type.generic.levels.SkyBlockLevelRequirement;
import net.swofty.type.generic.levels.SkyBlockLevelUnlock;
import net.swofty.type.generic.levels.causes.LevelCause;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;

public class ActionChangeHypixelXP implements HypixelEventClass {


    @HypixelEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
    public void run(SkyBlockXPModificationEvent event) {
        if (event.getNewXP() <= event.getOldXP()) return;
        HypixelPlayer player = event.getPlayer();

        SkyBlockLevelRequirement oldLevel = SkyBlockLevelRequirement.getFromTotalXP(event.getOldXP());
        SkyBlockLevelRequirement newLevel = SkyBlockLevelRequirement.getFromTotalXP(event.getNewXP());

        if (oldLevel == newLevel) {
            if (event.getCause().shouldDisplayMessage(player))
                player.sendMessage("§b+§3" + (event.getNewXP() - event.getOldXP()) + " §bSkyBlock XP");
        } else {
            if (!(event.getCause() instanceof LevelCause)) {
                player.getSkyBlockExperience().addExperience(
                        SkyBlockLevelCause.getLevelCause(newLevel.asInt())
                );
            }
            List<SkyBlockLevelUnlock> unlocks = newLevel.getUnlocks();

            player.sendMessage("§3§m---------------------------------");
            player.sendMessage("  §b§lSKYBLOCK LEVEL UP! §7" + oldLevel.asInt() + " §8§l-> §3" + newLevel.asInt());
            if (!unlocks.isEmpty()) {
                player.sendMessage(" ");
                player.sendMessage("  §a§lREWARDS");
                unlocks.forEach(unlock -> {
                    unlock.getDisplay(player, newLevel.asInt()).forEach(line -> {
                        player.sendMessage("  §8" + line);
                    });
                });
            }
            player.sendMessage("§3§m---------------------------------");
        }
    }
}
