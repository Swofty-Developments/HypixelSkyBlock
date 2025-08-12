package net.swofty.type.skyblockgeneric.mission.missions.barn;

import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.custom.ItemCraftEvent;
import net.swofty.type.generic.mission.MissionData;
import net.swofty.type.generic.mission.HypixelMission;
import net.swofty.type.generic.region.RegionType;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Map;
import java.util.Set;

public class MissionCraftWheatMinion extends HypixelMission {
    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onCraftEvent(ItemCraftEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (data.isCurrentlyActive(MissionCraftWheatMinion.class) || data.hasCompleted(MissionCraftWheatMinion.class)) {
            return;
        }

        if (event.getCraftedItem().getAttributeHandler().getPotentialType() != ItemType.WHEAT_MINION) {
            return;
        }

        data.setHypixelPlayer(event.getPlayer());
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
    public Map<String, Object> onStart(HypixelPlayer player, MissionData.ActiveMission mission) {
        return Map.of();
    }

    @Override
    public void onEnd(HypixelPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionTalkToFarmhandAgain.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.THE_BARN);
    }
}
