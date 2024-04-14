package net.swofty.types.generic.mission.missions;

import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.custom.PlayerKilledSkyBlockMobEvent;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockProgressMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EventParameters(description = "Kill Zombies mission",
    node = EventNodes.CUSTOM,
    requireDataLoaded = true)
public class MissionKillZombies extends SkyBlockProgressMission {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerKilledSkyBlockMobEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerKilledSkyBlockMobEvent event = (PlayerKilledSkyBlockMobEvent) tempEvent;
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
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        //TODO move bartender to the bar
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.GRAVEYARD);
    }

    @Override
    public int getMaxProgress() {
        return 12;
    }
}
