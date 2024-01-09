package net.swofty.types.generic.utility;

import lombok.Getter;
import net.swofty.types.generic.item.SkyBlockItem;

@Getter
public class MaterialQuantifiableRandom {
    private SkyBlockItem material;
    private int bounds1;
    private int bounds2;

    public MaterialQuantifiableRandom(SkyBlockItem material, int bounds1, int bounds2) {
        this.material = material;
        this.bounds1 = bounds1;
        this.bounds2 = bounds2;
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
        return MathUtility.random(bounds1, bounds2);
    }
}
