package net.swofty.type.skyblockgeneric.mission.missions;

import net.minestom.server.item.Material;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.custom.ItemCraftEvent;
import net.swofty.type.generic.mission.MissionData;
import net.swofty.type.generic.mission.HypixelMission;
import net.swofty.type.generic.region.RegionType;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionCraftWorkbench extends HypixelMission {
    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onCraftEvent(ItemCraftEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (!data.isCurrentlyActive(MissionCraftWorkbench.class) || data.hasCompleted(MissionCraftWorkbench.class)) return;

        if (event.getCraftedItem().getMaterial() != Material.CRAFTING_TABLE) return;

        data.setHypixelPlayer(event.getPlayer());
        data.endMission(MissionCraftWorkbench.class);
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
    public Map<String, Object> onStart(HypixelPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(HypixelPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionCraftWoodenPickaxe.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.PRIVATE_ISLAND);
    }
}
