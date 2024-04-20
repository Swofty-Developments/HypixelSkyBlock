package net.swofty.types.generic.item.items.miscellaneous.bartender;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.DecorationHead;
import net.swofty.types.generic.item.impl.ExtraUnderNameDisplay;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class KnockoffCola implements CustomSkyBlockItem, DecorationHead, ExtraUnderNameDisplay, CustomDisplayName {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "c14befd4d3797a8a6ab53ceedebab3c12b7b1f03440bf0085010025c35717628";
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Basic Brew";
    }

    @Override
    public String getDisplayName() {
        return "KnockOff™ Cola";
    }
}
