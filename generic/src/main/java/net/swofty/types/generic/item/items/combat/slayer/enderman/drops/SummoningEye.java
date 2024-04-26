package net.swofty.types.generic.item.items.combat.slayer.enderman.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class SummoningEye implements CustomSkyBlockItem, SkullHead, TrackedUniqueItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "daa8fc8de6417b48d48c80b443cf5326e3d9da4dbe9b25fcd49549d96168fc0";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Use this at the §5Ender Altar",
                "§5§7in the §5Dragon's Nest §7to",
                "§7summon Ender Dragons!"));
    }
}
