package net.swofty.mission.missions;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.SkyBlock;
import net.swofty.entity.villager.SkyBlockVillagerNPC;
import net.swofty.entity.villager.villagers.VillagerDuke;
import net.swofty.entity.villager.villagers.VillagerFelix;
import net.swofty.entity.villager.villagers.VillagerLeo;
import net.swofty.entity.villager.villagers.VillagerVex;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.custom.PlayerRegionChangeEvent;
import net.swofty.event.custom.VillagerSpokenToEvent;
import net.swofty.mission.MissionData;
import net.swofty.mission.MissionRepeater;
import net.swofty.mission.SkyBlockMission;
import net.swofty.mission.SkyBlockProgressMission;
import net.swofty.region.RegionType;
import net.swofty.user.SkyBlockPlayer;

import java.util.*;

@EventParameters(description = "Talk to Librarian mission",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.HUB,
        requireDataLoaded = true)
public class MissionTalkToLibrarian extends SkyBlockMission {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChangeEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerRegionChangeEvent event = (PlayerRegionChangeEvent) tempEvent;
        MissionData data = event.getPlayer().getMissionData();

        if (data.isCurrentlyActive("speak_to_librarian") || data.hasCompleted("speak_to_librarian")) {
            return;
        }

        data.setSkyBlockPlayer(event.getPlayer());
        data.startMission(MissionTalkToLibrarian.class);
    }

    @Override
    public String getID() {
        return "speak_to_librarian";
    }

    @Override
    public String getName() {
        return "Talk to the Librarian";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.LIBRARY);
    }
}
