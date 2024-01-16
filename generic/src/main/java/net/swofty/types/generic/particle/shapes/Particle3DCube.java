package net.swofty.types.generic.particle.shapes;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

public class Particle3DCube extends ParticleShape
{
      private final double radius;
      private final int intensity;
      private final boolean wireframe;

      public Particle3DCube(double radius, int intensity, boolean wireframe) {
            this.radius = radius;
            this.intensity = intensity;
            this.wireframe = wireframe;
      }

      @Override
      public void update(Pos pos) {
            Pos corner1 = pos.add(radius, radius, radius);
            Pos corner2 = pos.add(-radius, -radius, -radius);

            double minX = Math.min(corner1.x(), corner2.x());
            double minY = Math.min(corner1.y(), corner2.y());
            double minZ = Math.min(corner1.z(), corner2.z());
            double maxX = Math.max(corner1.x(), corner2.x());
            double maxY = Math.max(corner1.y(), corner2.y());
            double maxZ = Math.max(corner1.z(), corner2.z());

            double intensityDouble = 1 - Double.parseDouble("0." + intensity);

            if (!wireframe) {
                  // 2 areas
                  for (double x = minX; x <= maxX; x += intensityDouble) {
                        for (double z = minZ; z <= maxZ; z += intensityDouble) {
                              addPacket(packet(new Pos(x, minY, z), Vec.ZERO, 0.0f, 1));
                              addPacket(packet(new Pos(x, maxY, z), Vec.ZERO, 0.0f, 1));
                        }
                  }

                  // 2 sides (front & back)
                  for (double x = minX; x <= maxX; x += intensityDouble) {
                        for (double y = minY; y <= maxY; y += intensityDouble) {
                              addPacket(packet(new Pos(x, y, minZ), Vec.ZERO, 0.0f, 1));
                              addPacket(packet(new Pos(x, y, maxZ), Vec.ZERO, 0.0f, 1));
                        }
                  }

                  // 2 sides (left & right)
                  for (double z = minZ; z <= maxZ; z += intensityDouble) {
                        for (double y = minY; y <= maxY; y += intensityDouble) {
                              addPacket(packet(new Pos(minX, y, z), Vec.ZERO, 0.0f, 1));
                              addPacket(packet(new Pos(maxX, y, z), Vec.ZERO, 0.0f, 1));
                        }
                  }
            } else {
                  // 2 areas
                  for (double x = minX; x <= maxX; x += intensityDouble) {
                        for (double z = minZ; z <= maxZ; z += intensityDouble) {
                              if ((z == minZ || z == maxZ) || (x == maxX || x == minX)) {
                                    addPacket(packet(new Pos(x, minY, z), Vec.ZERO, 0.0f, 1));
                                    addPacket(packet(new Pos(x, maxY, z), Vec.ZERO, 0.0f, 1));
                              }
                        }
                  }

                  // 2 sides (front & back)
                  for (double x = minX; x <= maxX; x += intensityDouble) {
                        for (double y = minY; y <= maxY; y += intensityDouble) {
                              if ((x == minX || x == maxX) || (y >= maxY || y == minY)) {
                                    addPacket(packet(new Pos(x, y, minZ), Vec.ZERO, 0.0f, 1));
                                    addPacket(packet(new Pos(x, y, maxZ), Vec.ZERO, 0.0f, 1));
                              }
                        }
                  }

                  // 2 sides (left & right)
                  for (double z = minZ; z <= maxZ; z += intensityDouble) {
                        for (double y = minY; y <= maxY; y += intensityDouble) {
                              if ((z == minZ || z == maxZ) || (y == maxY || y == minY)) {
                                    addPacket(packet(new Pos(minX, y, z), Vec.ZERO, 0.0f, 1));
                                    addPacket(packet(new Pos(maxX, y, z), Vec.ZERO, 0.0f, 1));
                              }
                        }
                  }
            }
      }
}
