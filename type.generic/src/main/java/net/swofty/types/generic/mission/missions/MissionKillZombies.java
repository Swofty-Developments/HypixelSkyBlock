package net.swofty.types.generic.mission.missions;

import net.minestom.server.entity.EntityType;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.PlayerKilledSkyBlockMobEvent;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockProgressMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.*;

public class MissionKillZombies extends SkyBlockProgressMission {
    @SkyBlockEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onKilledZombie(PlayerKilledSkyBlockMobEvent event) {
        if (event.getKilledMob().getEntityType() != EntityType.ZOMBIE) return;

        MissionData data = event.getPlayer().getMissionData();
        if (!data.isCurrentlyActive(MissionKillZombies.class) || data.hasCompleted(MissionKillZombies.class)) return;

        MissionData.ActiveMission mission = data.getMission(MissionKillZombies.class).getKey();
        mission.setMissionProgress(mission.getMissionProgress() + 1);
        mission.checkIfMissionEnded(event.getPlayer());
    }

    @Override
    public String getID() {
        return "kill_zombies";
    }

    @Override
    public String getName() {
        return "Kill Zombies";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("§b5 SkyBlock XP", "§3100 §7Combat XP"))).forEach(player::sendMessage);
        player.getSkills().setRaw(player, SkillCategories.COMBAT, player.getSkills().getRaw(SkillCategories.COMBAT) + 100);
        player.getSkyBlockExperience().addExperience(SkyBlockLevelCause.getMissionCause(getID()));
        player.getMissionData().startMission(MissionTalkToBartender.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.GRAVEYARD);
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