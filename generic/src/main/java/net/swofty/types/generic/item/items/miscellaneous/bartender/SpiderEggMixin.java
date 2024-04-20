package net.swofty.types.generic.item.items.miscellaneous.bartender;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.DecorationHead;
import net.swofty.types.generic.item.impl.ExtraUnderNameDisplay;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class SpiderEggMixin implements CustomSkyBlockItem, DecorationHead, ExtraUnderNameDisplay, CustomDisplayName {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "a5ac5e39ea511af1e7ab1c5956d1615f7ebb6f226a4ed3394c6a0b4c6a6d106f";
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Brewing Ingredient";
    }

    @Override
    public String getDisplayName() {
        return "Spider Egg Mixin";
    }
}
