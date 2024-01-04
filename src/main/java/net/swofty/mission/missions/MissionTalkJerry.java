package net.swofty.mission.missions;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.custom.JerryClickedEvent;
import net.swofty.event.custom.PlayerRegionChangeEvent;
import net.swofty.mission.MissionData;
import net.swofty.mission.MissionRepeater;
import net.swofty.mission.SkyBlockMission;
import net.swofty.region.RegionType;
import net.swofty.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EventParameters(description = "Talk to Jerry mission",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.ISLAND,
        requireDataLoaded = false)
public class MissionTalkJerry extends SkyBlockMission {
    @Override
    public Class<? extends Event> getEvent() {
        return JerryClickedEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        JerryClickedEvent event = (JerryClickedEvent) tempEvent;
        MissionData data = event.getPlayer().getMissionData();
        SkyBlockPlayer player = event.getPlayer();

        if (!data.isCurrentlyActive("talk_to_jerry")) {
            return;
        }

        event.setCancelled(true);

        if (data.getMission(this.getClass()).getKey().getCustomData().containsKey("talking")) {
            return;
        }

        data.getMission(this.getClass()).getKey().getCustomData().put("talking", true);

        SchedulerManager scheduler = MinecraftServer.getSchedulerManager();
        player.sendMessage("§e[NPC] Jerry§f: Your SkyBlock island is part of a much larger universe.");
        scheduler.scheduleTask(() -> {
            player.sendMessage("§e[NPC] Jerry§f: The SkyBlock universe is full of islands to explore and resources to discover!");
        }, TaskSchedule.tick(20 * 1), TaskSchedule.stop());
        scheduler.scheduleTask(() -> {
            player.sendMessage("§e[NPC] Jerry§f: Use the §dPortal§f to warp to the first of those islands - the SkyBlock Hub!");
        }, TaskSchedule.tick(20 * 2), TaskSchedule.stop());
        scheduler.scheduleTask(() -> {
            data.endMission(this.getClass());
        }, TaskSchedule.tick(20 * 3), TaskSchedule.stop());
    }

    @Override
    public String getID() {
        return "talk_to_jerry";
    }

    @Override
    public String getName() {
        return "Talk to Jerry";
    }

    @Override
    public HashMap<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionUseTeleporter.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.PRIVATE_ISLAND);
    }
}
