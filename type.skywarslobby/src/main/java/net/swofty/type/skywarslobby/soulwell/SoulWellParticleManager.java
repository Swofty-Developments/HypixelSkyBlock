package net.swofty.type.skywarslobby.soulwell;

import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.timer.TaskSchedule;
import org.tinylog.Logger;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Manages purple particle lines orbiting around the Soul Well.
 * Creates 6 vertical particle lines that orbit in a circle with radius 3.
 */
public class SoulWellParticleManager {
    // Soul well center position (shifted up from floor at Y=67)
    private static final double CENTER_X = 33.5;
    private static final double CENTER_Y = 67.5;
    private static final double CENTER_Z = 0.5;

    // Orbit configuration
    private static final double RADIUS = 3.0;
    private static final int LINE_COUNT = 6;
    private static final int PARTICLES_PER_LINE = 3;
    private static final double PARTICLE_SPACING = 0.5;

    // ~3 seconds per full rotation at 2 ticks per update
    // Angular speed = (2 * PI) / (3 seconds * 20 ticks/sec / 2 ticks per update)
    private static final double ANGULAR_SPEED = (2 * Math.PI) / 30.0;

    // Purple color (153, 0, 255)
    private static final Particle PURPLE_DUST = Particle.DUST.withColor(new Color(153, 0, 255));

    private static Instance instance;
    private static boolean initialized = false;

    public static void initialize(Instance inst) {
        if (initialized) return;
        initialized = true;
        instance = inst;

        AtomicLong tickCount = new AtomicLong(0);

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (instance == null) return;

            long ticks = tickCount.getAndIncrement();
            double baseAngle = ticks * ANGULAR_SPEED;

            // Spawn 6 particle lines, evenly spaced around the circle
            for (int lineIndex = 0; lineIndex < LINE_COUNT; lineIndex++) {
                // Each line is offset by (2*PI / 6) = 60 degrees
                double angle = baseAngle + (lineIndex * (2 * Math.PI / LINE_COUNT));

                double x = CENTER_X + Math.cos(angle) * RADIUS;
                double z = CENTER_Z + Math.sin(angle) * RADIUS;

                // Spawn 3 particles vertically for each line
                for (int particleIndex = 0; particleIndex < PARTICLES_PER_LINE; particleIndex++) {
                    double y = CENTER_Y + (particleIndex * PARTICLE_SPACING);

                    ParticlePacket packet = new ParticlePacket(
                            PURPLE_DUST,
                            false,
                            false,
                            x, y, z,
                            0, 0, 0,
                            0,
                            1
                    );

                    // Send to all players in the instance
                    instance.getPlayers().forEach(player -> player.sendPacket(packet));
                }
            }
        }).delay(TaskSchedule.seconds(2))
          .repeat(TaskSchedule.tick(2))
          .schedule();

        Logger.info("SoulWellParticleManager initialized - orbiting purple particles active");
    }

    public static void shutdown() {
        initialized = false;
        instance = null;
    }
}
