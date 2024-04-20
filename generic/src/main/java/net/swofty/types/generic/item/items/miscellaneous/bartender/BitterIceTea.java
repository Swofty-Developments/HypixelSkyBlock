package net.swofty.types.generic.item.items.miscellaneous.bartender;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.DecorationHead;
import net.swofty.types.generic.item.impl.ExtraUnderNameDisplay;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class BitterIceTea implements CustomSkyBlockItem, DecorationHead, ExtraUnderNameDisplay, CustomDisplayName {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "e6f4e84795c16daa3df79538f2eb7a9db6bc2488f21533133b266a5ff9d2b1f1";
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Basic Brew";
    }

    @Override
    public String getDisplayName() {
        return "Bitter Iced Tea";
    }
}
