package net.swofty.types.generic.mission.missions;

import net.minestom.server.item.Material;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.ItemCraftEvent;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionCraftWorkbench extends SkyBlockMission {
    @SkyBlockEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onCraftEvent(ItemCraftEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (!data.isCurrentlyActive(MissionCraftWorkbench.class) || data.hasCompleted(MissionCraftWorkbench.class)) return;

        if (event.getCraftedItem().getMaterial() != Material.CRAFTING_TABLE) return;

        data.setSkyBlockPlayer(event.getPlayer());
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
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionCraftWoodenPickaxe.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.PRIVATE_ISLAND);
    }
}
