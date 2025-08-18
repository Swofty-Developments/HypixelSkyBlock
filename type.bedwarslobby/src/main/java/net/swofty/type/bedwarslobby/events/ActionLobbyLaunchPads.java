package net.swofty.type.bedwarslobby.events;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.swofty.type.bedwarslobby.util.LaunchPads;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.MathUtility;

import java.util.List;

public class ActionLobbyLaunchPads implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true , isAsync = true)
	public void run(PlayerMoveEvent event) {
		HypixelPlayer player = (HypixelPlayer) event.getPlayer();

		LaunchPads pad;
		try {
			pad = LaunchPads.getLaunchPadInRange(player.getPosition(), 2);
		} catch (ExceptionInInitializerError err) {
			return;
		}
		if (pad == null) return;

		player.sendPacket(new ParticlePacket(
				Particle.EXPLOSION,
				false,
				false,
				(float) player.getPosition().x(),
				(float) player.getPosition().y(),
				(float) player.getPosition().z(),
				0,
				0,
				0,
				0,
				1
		));
		player.playSound(Sound.sound(Key.key("entity.firework_rocket.launch"), Sound.Source.PLAYER, 1, 1));

		Vec toGoTo = pad.getDestination().asVec();
		Vec direction = toGoTo.sub(player.getPosition().asVec()).normalize();
		player.setVelocity(direction.mul(50, 15, 50));
		pad.getAfterFinished().accept(player);
	}


}
