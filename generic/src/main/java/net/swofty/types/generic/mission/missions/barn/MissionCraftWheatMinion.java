package net.swofty.types.generic.mission.missions.barn;

import net.minestom.server.event.Event;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.custom.ItemCraftEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

@EventParameters(description = "Craft Wheat Minion",
        node = EventNodes.CUSTOM,
        requireDataLoaded = false)
public class MissionCraftWheatMinion extends SkyBlockMission {
    @Override
    public String getID() {
        return "craft_minion";
    }

    @Override
    public String getName() {
        return "Craft a Wheat Minion";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return Map.of();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionTalkToFarmhandAgain.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.THE_BARN);
    }

    @Override
    public Class<? extends Event> getEvent() {
        return ItemCraftEvent.class;
    }

    @Override
    public void run(Event event) {
        ItemCraftEvent craftEvent = (ItemCraftEvent) event;
        ItemType type = craftEvent.getCraftedItem().getAttributeHandler().getItemTypeAsType();

        if (type != ItemType.WHEAT) { // WHEAT MINION
            return;
        }
        MissionData data = craftEvent.getPlayer().getMissionData();

        if (data.isCurrentlyActive(MissionCraftWheatMinion.class)) {
            data.endMission(MissionCraftWheatMinion.class);
        }
    }
}
