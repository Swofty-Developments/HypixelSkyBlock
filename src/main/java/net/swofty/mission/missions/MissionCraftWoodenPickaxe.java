package net.swofty.mission.missions;

import net.minestom.server.event.Event;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.custom.ItemCraftEvent;
import net.swofty.item.ItemType;
import net.swofty.mission.MissionData;
import net.swofty.mission.SkyBlockMission;
import net.swofty.region.RegionType;
import net.swofty.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EventParameters(description = "Craft wooden pickaxe mission",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.ISLAND,
        requireDataLoaded = false)
public class MissionCraftWoodenPickaxe extends SkyBlockMission {
    @Override
    public Class<? extends Event> getEvent() {
        return ItemCraftEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        ItemCraftEvent event = (ItemCraftEvent) tempEvent;
        ItemType type = event.getCraftedItem().getAttributeHandler().getItemTypeAsType();

        if (type == ItemType.WOODEN_PICKAXE) {
            MissionData data = event.getPlayer().getMissionData();

            if (data.isCurrentlyActive("craft_wood_pickaxe")) {
                data.endMission("craft_wood_pickaxe");
            }
        }
    }

    @Override
    public String getID() {
        return "craft_wood_pickaxe";
    }

    @Override
    public String getName() {
        return "Craft a wood pickaxe";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
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