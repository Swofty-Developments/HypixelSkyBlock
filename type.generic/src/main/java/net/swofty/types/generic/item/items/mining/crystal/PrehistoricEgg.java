package net.swofty.types.generic.item.items.mining.crystal;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class PrehistoricEgg implements CustomSkyBlockItem, SkullHead, Sellable, TrackedUniqueItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "bc8b15e85ed225c21c32367da7be244779e36afe7f7563a6a570a717cb5be1b1";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7§7After §bwalking §7a certain amount of blocks,",
                "§7has a §achance §7to spawn an §fArmadillo§7!",
                "",
                "§b4,000§7: §a50% §7for a §fCommon Armadillo!",
                "§b10,000§7: §a50% §7for an §aUncommon Armadillo!",
                "§b20,000§7: §a50% §7for a §9Rare Armadillo!",
                "§b40,000§7: §a50% §7for an §5Epic Armadillo!",
                "§b100,000§7: §a100% §7for a §6Legendary Armadillo!"));
    }

    @Override
    public double getSellValue() {
        return 1000;
    }
}
