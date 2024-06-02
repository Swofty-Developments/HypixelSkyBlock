package net.swofty.types.generic.item.items.miscellaneous.gifts;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultSoulbound;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GameAnnihilator implements CustomSkyBlockItem, DefaultSoulbound, SkullHead {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(List.of(
                "§7This item was given to a player",
                "§7who reported enough game",
                "§7breaking bugs to make the",
                "§7SkyBlock developers cry."
        ));
    }

    @Override
    public boolean isCoopAllowed() {
        return true;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8da332abde333a15a6c6fcfeca83f0159ea94b68e8f274bafc04892b6dbfc";
    }
}
