package net.swofty.types.generic.item.items.brewing;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class DecentCoffee implements CustomSkyBlockItem, DecorationHead, ExtraUnderNameDisplay, CustomDisplayName, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Use this item in place of an",
                "§7Awkward Potion for certain",
                "§7potions.",
                "",
                "§7Adds §f+8✦ Speed§7 to potions with",
                "§7that stat."
        ));
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
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Decent Coffee";
    }
}
