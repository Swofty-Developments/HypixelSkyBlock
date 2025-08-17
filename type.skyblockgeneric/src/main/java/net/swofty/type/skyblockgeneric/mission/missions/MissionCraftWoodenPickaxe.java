package net.swofty.type.skyblockgeneric.mission.missions;

import net.minestom.server.item.Material;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.skyblockgeneric.event.custom.ItemCraftEvent;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.HypixelMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionCraftWoodenPickaxe extends HypixelMission {
    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onCraftEvent(ItemCraftEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (!data.isCurrentlyActive(MissionCraftWoodenPickaxe.class) || data.hasCompleted(MissionCraftWoodenPickaxe.class)) return;

        if (!event.getCraftedItem().getMaterial().equals(Material.WOODEN_PICKAXE)) return;

        data.setSkyBlockPlayer(event.getPlayer());
        data.endMission(MissionCraftWoodenPickaxe.class);
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
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionTalkJerry.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.PRIVATE_ISLAND);
    }
}