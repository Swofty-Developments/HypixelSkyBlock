package net.swofty.types.generic.mission.missions;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.CustomBlockBreakEvent;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionBreakLog extends SkyBlockMission {
    @SkyBlockEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void endMission(CustomBlockBreakEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (!data.isCurrentlyActive(MissionBreakLog.class) || data.hasCompleted(MissionBreakLog.class)) return;

        data.setSkyBlockPlayer(event.getPlayer());
        data.endMission(MissionBreakLog.class);
    }

    @SkyBlockEvent(node = EventNodes.PLAYER, requireDataLoaded = true, isAsync = true)
    public void startMission(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;
        MissionData data = ((SkyBlockPlayer) event.getPlayer()).getMissionData();

        if (data.isCurrentlyActive(MissionBreakLog.class) || data.hasCompleted(MissionBreakLog.class)) return;

        data.setSkyBlockPlayer(((SkyBlockPlayer) event.getPlayer()));
        data.startMission(MissionBreakLog.class);
    }

    @Override
    public String getID() {
        return "break_log";
    }

    @Override
    public String getName() {
        return "Break a log";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionCraftWorkbench.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.PRIVATE_ISLAND);
    }
}
