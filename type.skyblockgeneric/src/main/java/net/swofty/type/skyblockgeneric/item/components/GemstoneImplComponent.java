package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.generic.gems.GemRarity;
import net.swofty.type.generic.gems.Gemstone;
import net.swofty.type.generic.item.SkyBlockItemComponent;

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
