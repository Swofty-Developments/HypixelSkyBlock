package net.swofty.types.generic.item.items.brewing;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class CheapCoffee implements CustomSkyBlockItem, DecorationHead, ExtraUnderNameDisplay, CustomDisplayName, NotFinishedYet {
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
                "§7Adds §f+5✦ Speed§7 to potions with",
                "§7that stat."
        ));
    }
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "194221a0de936bac5ce895f2acad19c64795c18ce5555b971594205bd3ec";
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Basic Brew";
    }

    @Override
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Cheap Coffee";
    }
}
