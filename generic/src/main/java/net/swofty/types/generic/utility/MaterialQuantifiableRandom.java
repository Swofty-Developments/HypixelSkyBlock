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

    public int getAmount(Double multiplicator) {
        if (bounds1 == bounds2) {
            Integer x = (int) Math.floor(bounds1*multiplicator);
            Integer y = (int) (((bounds2*multiplicator)-x)*100);

            if (MathUtility.random(0, 100) <= y) {
                return (int) Math.ceil(bounds2*multiplicator);
            } else {
                return (int) Math.floor(bounds1*multiplicator);
            }
        } else {
            return MathUtility.random((int) Math.floor(bounds1*multiplicator), (int) Math.ceil(bounds2*multiplicator));
        }
    }
}
