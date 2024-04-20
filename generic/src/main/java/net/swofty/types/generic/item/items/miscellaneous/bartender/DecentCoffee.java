package net.swofty.types.generic.item.items.miscellaneous.bartender;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.DecorationHead;
import net.swofty.types.generic.item.impl.ExtraUnderNameDisplay;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class DecentCoffee implements CustomSkyBlockItem, DecorationHead, ExtraUnderNameDisplay, CustomDisplayName {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "7ea0f7757185be9df5b2fc9d85d40642ea4fdb4515f314da18f59c696e5be9";
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Basic Brew";
    }

    @Override
    public String getDisplayName() {
        return "Decent Coffee";
    }
}
