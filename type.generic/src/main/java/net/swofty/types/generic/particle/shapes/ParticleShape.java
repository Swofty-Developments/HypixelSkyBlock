package net.swofty.types.generic.particle.shapes;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.swofty.types.generic.particle.ParticleEngine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ParticleShape
{
      private final List<ParticlePacket> packets;
      private Particle particle;

      public ParticleShape() {
            this.packets = new ArrayList<>();
            // Set a default value for it
            this.particle = Particle.SPLASH;
      }

      // Updated every tick
      public abstract void update(Pos pos);

      /**
       * This method is only to be used by the {@link ParticleEngine}  class
       * @param particle the ID to be set
       */
      @Deprecated
      public void setParticle(Particle particle) {
            this.particle = particle;
      }

      /**
       * This method is only to be used by the {@link ParticleEngine}  class
       * @param pos the position provided by the user
       */
      @Deprecated
      public void internalUpdate(Pos pos) {
            packets.clear();
            update(pos);
      }

      // Utility methods
      protected void addPacket(ParticlePacket packet) {
            this.packets.add(packet);
      }

      public List<ParticlePacket> getPackets() {
            return new ArrayList<>(this.packets);
      }

      public Collection<SendablePacket> getPacketsAsSendable() {
            return new ArrayList<>(this.packets).stream().map((packet) -> (SendablePacket) packet).toList();
      }

      protected ParticlePacket packet(Pos position, Vec offsets, float data, int count) {
            return new ParticlePacket(
                    particle,
                    false, // Long distance must always be false to not cause lag
                    false,
                    position.x(),
                    position.y(),
                    position.z(),
                    (float) offsets.x(),
                    (float) offsets.y(),
                    (float) offsets.z(),
                    data,
                    count
            );
      }
}
