package net.swofty.types.generic.mission.missions;

import net.minestom.server.event.Event;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.custom.ItemCraftEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EventParameters(description = "Craft workbench mission",
        node = EventNodes.CUSTOM,
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

        if (!SkyBlockConst.isIslandServer()) return;

        if (type != ItemType.CRAFTING_TABLE) {
            return;
        }

        MissionData data = event.getPlayer().getMissionData();

        if (data.isCurrentlyActive(MissionCraftWorkbench.class)) {
            data.endMission(MissionCraftWorkbench.class);
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
