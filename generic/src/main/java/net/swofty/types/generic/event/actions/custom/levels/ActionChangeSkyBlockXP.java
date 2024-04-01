package net.swofty.types.generic.event.actions.custom.levels;

import net.minestom.server.event.Event;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.SkyBlockXPModificationEvent;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;
import net.swofty.types.generic.levels.SkyBlockLevelUnlock;
import net.swofty.types.generic.levels.causes.LevelCause;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;

@EventParameters(description = "Handles the displays when changing SkyBlock XP",
        node = EventNodes.CUSTOM,
        requireDataLoaded = true)
public class ActionChangeSkyBlockXP extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return SkyBlockXPModificationEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        SkyBlockXPModificationEvent event = (SkyBlockXPModificationEvent) tempEvent;
        if (event.getNewXP() <= event.getOldXP()) return;
        SkyBlockPlayer player = event.getPlayer();

        SkyBlockLevelRequirement oldLevel = SkyBlockLevelRequirement.getFromTotalXP(event.getOldXP());
        SkyBlockLevelRequirement newLevel = SkyBlockLevelRequirement.getFromTotalXP(event.getNewXP());

        if (oldLevel == newLevel) {
            if (event.getCause().shouldDisplayMessage(player))
                player.sendMessage("§b+§3" + (event.getNewXP() - event.getOldXP()) + " §bSkyBlock XP");
        } else {
            if (!(event.getCause() instanceof LevelCause)) {
                player.getSkyBlockExperience().addExperience(
                        new LevelCause(newLevel.asInt())
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
