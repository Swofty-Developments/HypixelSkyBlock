package net.swofty.types.generic.particle.shapes;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

public class Particle3DSphere extends ParticleShape
{
      private final double circleRadius;
      private final int circleCount;

      public Particle3DSphere(int circleCount, double rad) {
            this.circleRadius = rad;
            this.circleCount = circleCount;
      }

      @Override
      public void update(Pos pos) {
            for (double i = 0; i <= Math.PI; i += Math.PI / circleCount) {
                  double radius = Math.sin(i) * circleRadius;
                  double y = Math.cos(i) * circleRadius;
                  for (double a = 0; a < Math.PI * 2; a += Math.PI / circleCount) {
                        double x = Math.cos(a) * radius;
                        double z = Math.sin(a) * radius;

                        addPacket(packet(pos.add(x, y, z), Vec.ZERO, 0.0f, 1));
                  }
            }
      }
}
