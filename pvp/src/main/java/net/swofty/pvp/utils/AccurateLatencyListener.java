package net.swofty.pvp.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.client.common.ClientKeepAlivePacket;
import net.minestom.server.network.packet.server.common.KeepAlivePacket;
import net.minestom.server.tag.Tag;

public class AccurateLatencyListener {
	private static final Component KICK_MESSAGE = Component.text("Bad Keep Alive packet", NamedTextColor.RED);
	
	private static final Tag<Long> SEND_TIME = Tag.Transient("keepalive_send_time");
	
	public static void listener(ClientKeepAlivePacket packet, Player player) {
		final long packetId = packet.id();
		if (packetId != player.getLastKeepAlive()) {
			player.kick(KICK_MESSAGE);
			return;
		}
		player.refreshAnswerKeepAlive(true);
		long sendTime = player.getTag(SEND_TIME);
		// Update latency
		final int latency = (int) (System.currentTimeMillis() - sendTime);
		player.refreshLatency(latency);
	}
	
	public static void onSend(PlayerPacketOutEvent event) {
		// This will get called right before writing the packet, so more accuracy
		if (event.getPacket() instanceof KeepAlivePacket) {
			event.getPlayer().setTag(SEND_TIME, System.currentTimeMillis());
		}
	}
}
