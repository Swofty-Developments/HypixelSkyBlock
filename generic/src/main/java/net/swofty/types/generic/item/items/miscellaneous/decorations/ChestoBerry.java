package net.swofty.types.generic.item.items.miscellaneous.decorations;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ChestoBerry implements CustomSkyBlockItem, DecorationHead, ExtraUnderNameDisplay, CustomDisplayName {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.EMPTY;
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Decoration item";
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "436f4ff183cbaa49122455614911b84942b5f92d4d82cc3b890bcc7d57c6";
    }

    @Override
    public String getAbsoluteName(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "§fChesto Berry";
    }

    @Override
    public String getDisplayName() {
        return "Chesto Berry";
    }
}
