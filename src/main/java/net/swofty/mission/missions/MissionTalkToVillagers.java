package net.swofty.mission.missions;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.SkyBlock;
import net.swofty.entity.villager.SkyBlockVillagerNPC;
import net.swofty.entity.villager.villagers.VillagerDuke;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.custom.PlayerRegionChangeEvent;
import net.swofty.event.custom.VillagerSpokenToEvent;
import net.swofty.mission.MissionData;
import net.swofty.mission.MissionRepeater;
import net.swofty.mission.SkyBlockProgressMission;
import net.swofty.region.RegionType;
import net.swofty.user.SkyBlockPlayer;

import java.util.*;

@EventParameters(description = "Talk to Villagers mission",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.HUB,
        requireDataLoaded = false)
public class MissionTalkToVillagers extends SkyBlockProgressMission implements MissionRepeater {
    private static final List<Class<? extends SkyBlockVillagerNPC>> villagers = List.of(
            VillagerDuke.class
    );

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

        Map<String, Object> customData = mission.getCustomData();

        if (customData.values().stream().anyMatch(value -> value.toString().contains(event.getVillager().getID()))) return;
        // Check if villager is a part of the mission
        if (villagers.stream().noneMatch(villager ->
                {
                    try {
                        return event.getVillager().getID().contains(villager.newInstance().getID());
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
        )) return;

        customData.put("villager_" + mission.getMissionProgress(), event.getVillager().getID());
        customData.put("last_updated", System.currentTimeMillis());

        mission.setMissionProgress(mission.getMissionProgress() + 1);
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
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
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
            getPlayersWithMissionActive().forEach(player -> {
                if (player.getInstance() != SkyBlock.getInstanceContainer()) return;

                Map<String, Object> customData = player.getMissionData().getMission(MissionTalkToVillagers.class).getKey().getCustomData();
                List<Class<? extends SkyBlockVillagerNPC>> villagersNotSpokenTo = new ArrayList<>(villagers);
                villagersNotSpokenTo.removeIf(villager ->
                        customData.values()
                                .stream()
                                .anyMatch(value ->
                                {
                                    try {
                                        return value.toString().contains(villager.newInstance().getID());
                                    } catch (InstantiationException | IllegalAccessException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));

                villagersNotSpokenTo.forEach(villager -> {
                    try {
                        Pos villagerPosition = villager.newInstance().getParameters().position();

                        player.sendPacket(new ParticlePacket(
                                37,
                                false,
                                villagerPosition.x(),
                                villagerPosition.y() + 3f,
                                villagerPosition.z(),
                                0.1f,
                                0.1f,
                                0.1f,
                                0f,
                                5,
                                new byte[]{}
                        ));
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        }, TaskSchedule.tick(5), TaskSchedule.tick(5));
    }
}
