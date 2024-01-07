package net.swofty.commons.skyblock.mission.missions;

import net.minestom.server.event.Event;
import net.swofty.commons.skyblock.data.DataHandler;
import net.swofty.commons.skyblock.data.datapoints.DatapointDouble;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.custom.PlayerRegionChangeEvent;
import net.swofty.commons.skyblock.mission.MissionData;
import net.swofty.commons.skyblock.mission.SkyBlockMission;
import net.swofty.commons.skyblock.region.RegionType;

import java.util.*;

@EventParameters(description = "Use teleporter mission",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = false)
public class MissionUseTeleporter extends SkyBlockMission {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChangeEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerRegionChangeEvent event = (PlayerRegionChangeEvent) tempEvent;

        if (event.getTo() == null || !event.getTo().equals(RegionType.VILLAGE)) {
            return;
        }

        if (!event.getPlayer().getMissionData().isCurrentlyActive(this.getClass())) {
            return;
        }

        MissionData data = event.getPlayer().getMissionData();
        data.endMission(this.getClass());
    }

    @Override
    public String getID() {
        return "use_teleporter";
    }

    @Override
    public String getName() {
        return "Use the teleporter";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("§61000 Gold"))).forEach(player::sendMessage);
        player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(
                player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue() + 1000
        );
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.PRIVATE_ISLAND);
    }
}