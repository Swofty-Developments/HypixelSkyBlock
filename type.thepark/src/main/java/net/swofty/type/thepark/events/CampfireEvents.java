package net.swofty.type.thepark.events;

import net.minestom.server.event.player.PlayerTickEvent;
import net.minestom.server.instance.block.Block;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.darkthicket.MissionCompleteTrialOfFireOne;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.thepark.TrialOfFire;
import org.tinylog.Logger;

public class CampfireEvents implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
	public void onTick(PlayerTickEvent event) {
		SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
		if (!player.getMissionData().isCurrentlyActive(MissionCompleteTrialOfFireOne.class)) {
			return;
		};

		Block block = player.getInstance().getBlock(player.getPosition().sub(0, 0.3, 0));
		if (block.key() == Block.CAMPFIRE.key()) {
			TrialOfFire.start(player);
		}
	}

}
