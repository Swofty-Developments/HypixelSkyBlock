package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItemComponent;

public class GemstoneImplComponent extends SkyBlockItemComponent {
    @Getter
    private final GemRarity gemRarity;
    @Getter
    private final Gemstone gemstone;

    public GemstoneImplComponent(GemRarity gemRarity, Gemstone gemstone, String skullTexture) {
        this.gemRarity = gemRarity;
        this.gemstone = gemstone;
        addInheritedComponent(new SkullHeadComponent((item) -> {
            return skullTexture;
        }));
    }

    public String getName() {
        return this.getClass().getSimpleName().split("(?=\\p{Upper})")[1];
    }
}
