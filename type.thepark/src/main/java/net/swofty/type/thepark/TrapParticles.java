package net.swofty.type.thepark;

import net.minestom.server.coordinate.Point;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TrapParticles {

	public static final List<ParticlePacket> EDGE_PARTICLES = new ArrayList<>();

	static {
		float min = 0f;
		float max = 1f;
		float step = 0.2f;

		for (float x = min; x <= max; x += step) {
			for (float y = min; y <= max; y += step) {
				for (float z = min; z <= max; z += step) {

					int edgeCount = 0;
					if (x == min || x == max) edgeCount++;
					if (y == min || y == max) edgeCount++;
					if (z == min || z == max) edgeCount++;

					if (edgeCount == 2) {
						EDGE_PARTICLES.add(new ParticlePacket(
								Particle.HAPPY_VILLAGER,
								false,
								false,
								x, y, z,
								0.1f, 0.1f, 0.1f,
								0.3f,
								2
						));
					}
				}
			}
		}
	}

	public static @NonNull List<SendablePacket> getParticlePackets(Point position) {
		List<SendablePacket> packets = new ArrayList<>(EDGE_PARTICLES.size());
		for (ParticlePacket base : EDGE_PARTICLES) {
			packets.add(new ParticlePacket(
					base.particle(),
					false,
					false,
					position.x() + base.x() - 0.5f,
					position.y() + base.y(),
					position.z() + base.z() - 0.5f,
					0f, 0f, 0f,
					0f,
					1
			));
		}
		return packets;
	}

}
