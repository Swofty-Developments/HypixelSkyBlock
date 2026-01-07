package net.swofty.type.thepark.events;

import net.minestom.server.entity.Entity;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.birchpark.MissionTravelToThePark;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.darkthicket.MissionFindTheCampfire;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.darkthicket.MissionTravelToTheDarkThicket;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.jungle.MissionTalkToMolbertAgainAgainAgain;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.savanna.MissionTravelToTheSavannaWoodland;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.spruce.MissionTravelToTheSpruceWoods;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.thepark.TypeTheParkLoader;
import net.swofty.type.thepark.npcs.NPCWorkerXavier;

public class ActionContinueParkMission implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void run(PlayerRegionChangeEvent event) {
        if (event.getTo() == null) return;

		SkyBlockPlayer player = event.getPlayer();
        MissionData data = event.getPlayer().getMissionData();
		switch (event.getTo()) {
			case BIRCH_PARK -> {
				if (data.isCurrentlyActive(MissionTravelToThePark.class)) {
					data.endMission(MissionTravelToThePark.class);
				}
			}
			case SPRUCE_WOODS -> {
				if (data.isCurrentlyActive(MissionTravelToTheSpruceWoods.class)) {
					data.endMission(MissionTravelToTheSpruceWoods.class);
				}
			}
			case DARK_THICKET -> {
				if (data.isCurrentlyActive(MissionTravelToTheDarkThicket.class)) {
					data.endMission(MissionTravelToTheDarkThicket.class);
				}
			}
			case TRIALS_OF_FIRE -> {
				if (data.isCurrentlyActive(MissionFindTheCampfire.class)) {
					data.endMission(MissionFindTheCampfire.class);
				}
			}
			case SAVANNA_WOODLAND -> {
				if (data.isCurrentlyActive(MissionTravelToTheSavannaWoodland.class)) {
					NPCWorkerXavier xavier = HypixelNPC.getRegisteredNPCs().stream()
							.filter(npc -> npc instanceof NPCWorkerXavier)
							.map(npc -> (NPCWorkerXavier) npc)
							.findFirst()
							.orElse(null);
					if (xavier == null) {
						return;
					}
					xavier.setDialogue(player, "intro").thenRun(() -> {
						data.endMission(MissionTravelToTheSavannaWoodland.class);
					});
				}
			}
			case JUNGLE_ISLAND -> {
				if (data.isCurrentlyActive(MissionTalkToMolbertAgainAgainAgain.class)) {
					TypeTheParkLoader.entities.forEach(Entity::updateViewableRule);
				}
			}
			case null, default -> {
			}
		}

    }
}
