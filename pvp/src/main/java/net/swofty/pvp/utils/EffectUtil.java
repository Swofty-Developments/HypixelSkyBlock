package net.swofty.pvp.utils;

import net.minestom.server.network.packet.server.play.WorldEventPacket;
import net.minestom.server.worldevent.WorldEvent;
import org.jetbrains.annotations.NotNull;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.PacketSendingUtils;

public class EffectUtil {
	public static void sendNearby(@NotNull Instance instance, @NotNull WorldEvent effect,
	                              int x, int y, int z, int data, double distance, boolean global) {
		WorldEventPacket packet = new WorldEventPacket(effect.id(), new Pos(x, y, z), data, global);
		
		double distanceSquared = distance * distance;
		PacketSendingUtils.sendGroupedPacket(instance.getPlayers(), packet, player -> {
			Pos position = player.getPosition();
			double dx = x - position.x();
			double dy = y - position.y();
			double dz = z - position.z();
			
			return dx * dx + dy * dy + dz * dz < distanceSquared;
		});
	}
}
