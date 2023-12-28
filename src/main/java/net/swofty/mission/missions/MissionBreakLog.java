package net.swofty.mission.missions;

import net.minestom.server.event.Event;
import net.minestom.server.item.Material;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.custom.CustomBlockBreak;
import net.swofty.mission.MissionData;
import net.swofty.mission.SkyBlockMission;
import net.swofty.region.RegionType;
import net.swofty.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EventParameters(description = "Break log mission",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.ISLAND,
        requireDataLoaded = false)
public class MissionBreakLog extends SkyBlockMission {
    @Override
    public Class<? extends Event> getEvent() {
        return CustomBlockBreak.class;
    }

    @Override
    public void run(Event tempEvent) {
        CustomBlockBreak event = (CustomBlockBreak) tempEvent;
        Material material = Material.fromNamespaceId(event.getBlock().namespace());
        MissionData data = event.getPlayer().getMissionData();

        if (data.isCurrentlyActive("break_log")
                && (material == Material.OAK_LOG || material == Material.OAK_WOOD)) {
            data.endMission("break_log");
        }
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
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData) {
        player.getMissionData().startMission(MissionCraftWorkbench.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.PRIVATE_ISLAND);
    }
}
