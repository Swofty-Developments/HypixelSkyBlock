package net.swofty.utility;

import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.SkyBlock;
import net.swofty.Utility;
import net.swofty.entity.hologram.HologramEntity;

public class DamageIndicator
{
      private static final char _DefaultColourPallet[] = { 'F', 'F', 'E', '6', 'C', 'C' };

      private int damage;
      private Pos pos;
      private Color color;
      private boolean crit;
      private int randomnessMultiplier;

      public DamageIndicator() {
            randomnessMultiplier = 1;
            color = null;
            crit = false;
      }

      public void display() {
            if (this.pos == null) return;

            String name = crit ? utilRainbowize("✧" + damage + "✧") : "§7" + damage;

            Entity entity = new HologramEntity(name);
            entity.setInstance(SkyBlock.getInstanceContainer(),
                    utilRandomizeLoc(pos));
            entity.setAutoViewable(true);
            entity.spawn();
            MinecraftServer.getSchedulerManager().scheduleTask(entity::remove, TaskSchedule.seconds(1), TaskSchedule.stop());
      }

      public DamageIndicator damage(float d) {
            this.damage = Math.round(d);
            return this;
      }

      public DamageIndicator pos(Pos pos) {
            this.pos = pos;
            return this;
      }

      public DamageIndicator color(Color c) {
            this.color = c;
            return this;
      }

      public DamageIndicator critical(boolean c) {
            this.crit = c;
            return this;
      }

      public DamageIndicator randomnessMultiplier(int mulp) {
            this.randomnessMultiplier = mulp;
            return this;
      }

      private String utilRainbowize(String string) {
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (String c : string.split("")) {
                  if (i > _DefaultColourPallet.length - 1) i = 0;
                  builder.append("§" + _DefaultColourPallet[i]).append(c);
                  i++;
            }
            return builder.toString();
      }

      private Pos utilRandomizeLoc(Pos pos) {
            return pos.add(Utility.random(-1.5, 1.5) + randomnessMultiplier, 1, Utility.random(-1.5, 1.5) * randomnessMultiplier);
      }
}
