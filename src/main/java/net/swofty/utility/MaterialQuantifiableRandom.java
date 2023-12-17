package net.swofty.utility;

import lombok.Getter;
import net.swofty.Utility;
import net.swofty.item.SkyBlockItem;

public class MaterialQuantifiableRandom {
      @Getter
      private SkyBlockItem material;
      @Getter
      private int bounds1;
      @Getter
      private int bounds2;

      public MaterialQuantifiableRandom(SkyBlockItem material, int bounds1, int bounds2) {
            this.material = material;
            this.bounds1 = bounds1;
            this.bounds2 = bounds2;
      }

      public MaterialQuantifiableRandom(SkyBlockItem material) {
            this(material, 1, 1);
      }

      public MaterialQuantifiableRandom setMaterial(SkyBlockItem material) {
            this.material = material;
            return this;
      }

      public MaterialQuantifiableRandom setBound1(int amount) {
            this.bounds1 = amount;
            return this;
      }

      public MaterialQuantifiableRandom setBound2(int amount) {
            this.bounds2 = amount;
            return this;
      }

      public int getAmount() {
            return (int) Utility.random(bounds1, bounds2);
      }
}
