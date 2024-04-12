package net.swofty.types.generic.item.items.miscellaneous.decorations;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class AncientFruit implements CustomSkyBlockItem , DecorationHead, ExtraUnderNameDisplay, CustomDisplayName {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "af072bcf2be6f3c5ffb52d2875e41294f21dd57cee3f428c7986a189714ad79f";
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Decoration item";
    }

    @Override
    public String getDisplayName() {
        return "Ancient Fruit";
    }

}
