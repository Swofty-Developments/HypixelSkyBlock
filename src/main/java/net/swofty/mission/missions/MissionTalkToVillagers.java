package net.swofty.mission.missions;

import net.minestom.server.event.Event;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.custom.PlayerRegionChangeEvent;
import net.swofty.event.custom.VillagerSpokenToEvent;
import net.swofty.mission.MissionData;
import net.swofty.mission.MissionRepeater;
import net.swofty.mission.SkyBlockProgressMission;
import net.swofty.region.RegionType;
import net.swofty.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EventParameters(description = "Talk to Villagers mission",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.HUB,
        requireDataLoaded = false)
public class MissionTalkToVillagers extends SkyBlockProgressMission implements MissionRepeater {
    @Override
    public Class<? extends Event> getEvent() {
        return VillagerSpokenToEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        VillagerSpokenToEvent event = (VillagerSpokenToEvent) tempEvent;
        MissionData data = event.getPlayer().getMissionData();

        if (!data.isCurrentlyActive("speak_to_villagers") && !data.hasCompleted("speak_to_villagers")) {
            data.setSkyBlockPlayer(event.getPlayer());
            data.startMission(MissionTalkToVillagers.class);
            return;
        }

        if (data.hasCompleted("speak_to_villagers")) return;

        MissionData.ActiveMission mission = data.getMission(MissionTalkToVillagers.class).getKey();
        mission.setMissionProgress(mission.getMissionProgress() + 1);

        Map<String, Object> customData = mission.getCustomData();

        if (customData.values().stream().anyMatch(value -> value.equals(event.getVillager().getID()))) return;
        customData.put("villager_" + mission.getMissionProgress(), event.getVillager().getID());

        mission.checkIfMissionEnded(event.getPlayer());
    }

    @Override
    public String getID() {
        return "speak_to_villagers";
    }

    @Override
    public String getName() {
        return "Talk to the Villagers";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        player.sendMessage("On start thrown");
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData) {
        player.sendMessage("On end thrown");

        for (Map.Entry<String, Object> entry : customData.entrySet()) {
            player.sendMessage(entry.getKey() + " " + entry.getValue());
        }
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.VILLAGE);
    }

    @Override
    public int getMaxProgress() {
        return 12;
    }

    @Override
    public Task getTask(Scheduler scheduler) {
        return scheduler.scheduleTask(() -> {

        }, TaskSchedule.seconds(1), TaskSchedule.seconds(1));
    }
}
