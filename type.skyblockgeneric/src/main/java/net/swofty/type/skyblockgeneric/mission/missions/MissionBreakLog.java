package net.swofty.type.skyblockgeneric.mission.missions;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.custom.CustomBlockBreakEvent;
import net.swofty.type.generic.mission.MissionData;
import net.swofty.type.generic.mission.HypixelMission;
import net.swofty.type.generic.region.RegionType;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionBreakLog extends HypixelMission {
    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void endMission(CustomBlockBreakEvent event) {
        if (event.getPlayerPlaced()) return;

        MissionData data = event.getPlayer().getMissionData();

        if (!data.isCurrentlyActive(MissionBreakLog.class) || data.hasCompleted(MissionBreakLog.class)) return;

        data.setHypixelPlayer(event.getPlayer());
        data.endMission(MissionBreakLog.class);
    }

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true, isAsync = true)
    public void startMission(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;
        MissionData data = ((HypixelPlayer) event.getPlayer()).getMissionData();

        if (data.isCurrentlyActive(MissionBreakLog.class) || data.hasCompleted(MissionBreakLog.class)) return;

        data.setHypixelPlayer(((HypixelPlayer) event.getPlayer()));
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
    public Map<String, Object> onStart(HypixelPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(HypixelPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionCraftWorkbench.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.PRIVATE_ISLAND);
    }
}
