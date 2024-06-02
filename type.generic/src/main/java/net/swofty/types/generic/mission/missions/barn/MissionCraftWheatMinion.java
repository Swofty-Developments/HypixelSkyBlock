package net.swofty.types.generic.mission.missions.barn;

import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.ItemCraftEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public class MissionCraftWheatMinion extends SkyBlockMission {
    @SkyBlockEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onCraftEvent(ItemCraftEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (data.isCurrentlyActive(MissionCraftWheatMinion.class) || data.hasCompleted(MissionCraftWheatMinion.class)) {
            return;
        }

        if (event.getCraftedItem().getAttributeHandler().getItemTypeAsType() != ItemType.WHEAT_MINION) {
            return;
        }

        data.setSkyBlockPlayer(event.getPlayer());
        data.startMission(MissionCraftWheatMinion.class);
    }

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
}
