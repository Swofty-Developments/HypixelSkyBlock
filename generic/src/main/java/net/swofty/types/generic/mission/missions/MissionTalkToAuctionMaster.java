package net.swofty.types.generic.mission.missions;

import net.minestom.server.event.Event;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EventParameters(description = "Talk to Auction Master mission",
        node = EventNodes.CUSTOM,
        requireDataLoaded = false)
public class MissionTalkToAuctionMaster extends SkyBlockMission {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChangeEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerRegionChangeEvent regionChangeEvent = (PlayerRegionChangeEvent) event;
        MissionData data = ((PlayerRegionChangeEvent) event).getPlayer().getMissionData();

        if (regionChangeEvent.getTo() == null || !regionChangeEvent.getTo().equals(RegionType.AUCTION_HOUSE)) {
            return;
        }

        if (data.isCurrentlyActive("talk_to_auction_master") || data.hasCompleted("talk_to_auction_master")) {
            return;
        }

        data.startMission(MissionTalkToAuctionMaster.class);
    }

    @Override
    public String getID() {
        return "talk_to_auction_master";
    }

    @Override
    public String getName() {
        return "Talk to the Auction Master";
    }

    @Override
    public HashMap<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {

    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.AUCTION_HOUSE);
    }
}

