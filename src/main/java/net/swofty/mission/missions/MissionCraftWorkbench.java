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

@EventParameters(description = "Craft workbench mission",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.ISLAND,
        requireDataLoaded = false)
public class MissionCraftWorkbench extends SkyBlockMission {
    @Override
    public Class<? extends Event> getEvent() {
        return CustomBlockBreak.class;
    }

    @Override
    public void run(Event tempEvent) {

    }

    @Override
    public String getID() {
        return "craft_workbench";
    }

    @Override
    public String getName() {
        return "Craft a workbench";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText(player).forEach(player::sendMessage);

        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData) {

    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.PRIVATE_ISLAND);
    }
}
