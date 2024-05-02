package net.swofty.types.generic.mission.missions.farmer;

import net.minestom.server.item.Material;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.CustomBlockBreakEvent;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockProgressMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.*;

public class MissionCollectWheat extends SkyBlockProgressMission {
    @SkyBlockEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onBlockBreak(CustomBlockBreakEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (!data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
            return;
        }

        if (event.getMaterial().equals(Material.WHEAT)) {
            MissionData.ActiveMission mission = data.getMission(this.getClass()).getKey();
            mission.setMissionProgress(mission.getMissionProgress() + 1);
            mission.checkIfMissionEnded(event.getPlayer());
        }
    }

    @Override
    public String getID() {
        return "mission_collect_wheat";
    }

    @Override
    public String getName() {
        return "Collect Wheat";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("§b20 Farming XP"))).forEach(player::sendMessage);
        player.getSkills().setRaw(player, SkillCategories.FARMING, player.getSkills().getRaw(SkillCategories.FARMING) + 20);
        player.getMissionData().startMission(MissionTalkToFarmerAgain.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.FARM);
    }

    @Override
    public Double getAttachedSkyBlockXP() {
        return 5D;
    }

    @Override
    public int getMaxProgress() {
        return 10;
    }
}
