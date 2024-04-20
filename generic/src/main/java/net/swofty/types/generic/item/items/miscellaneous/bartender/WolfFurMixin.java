package net.swofty.types.generic.item.items.miscellaneous.bartender;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.DecorationHead;
import net.swofty.types.generic.item.impl.ExtraUnderNameDisplay;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class WolfFurMixin implements CustomSkyBlockItem, DecorationHead, ExtraUnderNameDisplay, CustomDisplayName {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "595231147189ac4b9f53487ce35ba0205bf377f3986c0e5ceb942e77f1c39cc";
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Brewing Ingredient";
    }

    @Override
    public String getDisplayName() {
        return "Wolf Fur Mixin";
    }
}
