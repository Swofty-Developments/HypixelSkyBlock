package net.swofty.commons.skyblock.mission.missions;

import net.minestom.server.event.Event;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.custom.ItemCraftEvent;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.mission.MissionData;
import net.swofty.commons.skyblock.mission.SkyBlockMission;
import net.swofty.commons.skyblock.region.RegionType;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

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
        return ItemCraftEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        ItemCraftEvent event = (ItemCraftEvent) tempEvent;
        ItemType type = event.getCraftedItem().getAttributeHandler().getItemTypeAsType();

        if (type == ItemType.CRAFTING_TABLE) {
            MissionData data = event.getPlayer().getMissionData();

            if (data.isCurrentlyActive("craft_workbench")) {
                data.endMission("craft_workbench");
            }
        }
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
