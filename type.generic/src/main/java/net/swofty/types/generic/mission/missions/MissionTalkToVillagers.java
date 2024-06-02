package net.swofty.types.generic.mission.missions;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.VillagerSpokenToEvent;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.MissionRepeater;
import net.swofty.types.generic.mission.SkyBlockProgressMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.*;

public class MissionTalkToVillagers extends SkyBlockProgressMission implements MissionRepeater {
    private static final List<String> villagers = List.of(
            "Andrew",
            "Jack",
            "Jamie",
            "Tom",
            "Leo",
            "Felix",
            "Ryu",
            "Duke",
            "Lynn",
            "Stella",
            "Vex",
            "Liam"
    );

    @SkyBlockEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onVillagerSpokenTo(VillagerSpokenToEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (!data.isCurrentlyActive(MissionTalkToVillagers.class) &&
                !data.hasCompleted(MissionTalkToVillagers.class)) {
            data.setSkyBlockPlayer(event.getPlayer());
            data.startMission(MissionTalkToVillagers.class);
            return;
        }

        if (data.hasCompleted("speak_to_villagers")) return;

        MissionData.ActiveMission mission = data.getMission(MissionTalkToVillagers.class).getKey();

        Map<String, Object> customData = mission.getCustomData();

        if (customData.values().stream().anyMatch(value -> value.toString().contains(event.getVillager().getID())))
            return;

        // Check if villager is a part of the mission
        if (villagers.stream().noneMatch(villager ->
                event.getVillager().getID().contains(villager)
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
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("§61000 §7Coins"))).forEach(player::sendMessage);
        player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(
                player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue() + 1000
        );
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
                if (player.getInstance() != SkyBlockConst.getInstanceContainer()) return;

                Map<String, Object> customData = player.getMissionData().getMission(MissionTalkToVillagers.class).getKey().getCustomData();
                List<String> villagersNotSpokenTo = new ArrayList<>(villagers);
                villagersNotSpokenTo.removeIf(villager ->
                        customData.values()
                                .stream()
                                .anyMatch(value -> {
                                    String s = value.toString();
                                    return s.contains(villager);
                                }));

                SkyBlockVillagerNPC.getVillagers().forEach((npc, entity) -> {
                    if (entity.getInstance() != SkyBlockConst.getInstanceContainer()) return;
                    if (villagersNotSpokenTo.stream().noneMatch(villager -> npc.getID().contains(villager))) return;

                    Pos villagerPosition = npc.getParameters().position();

                    player.sendPacket(new ParticlePacket(
                            38,
                            false,
                            villagerPosition.x(),
                            villagerPosition.y() + 3f,
                            villagerPosition.z(),
                            0.1f,
                            0.1f,
                            0.1f,
                            0f,
                            3,
                            null
                    ));
                });
            });
        }, TaskSchedule.tick(5), TaskSchedule.tick(5));
    }
}
