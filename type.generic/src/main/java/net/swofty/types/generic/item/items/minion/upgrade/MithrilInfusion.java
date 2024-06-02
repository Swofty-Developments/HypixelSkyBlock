package net.swofty.types.generic.item.items.minion.upgrade;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.AnvilCombinable;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MithrilInfusion implements CustomSkyBlockItem, SkullHead, AnvilCombinable {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "7e051df4dd2151481f5145b93fb7a9aa62888fbcb90add9890ad07caf1faca73";
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7When applied to minions, permanently",
                "§7increases minion speed by §a10%§7."
        );
    }

    @Override
    public void apply(SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem) {
        upgradeItem.getAttributeHandler().setMithrilInfused(true);
    }

    @Override
    public boolean canApply(SkyBlockPlayer player, SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem) {
        if(!(upgradeItem.getGenericInstance() instanceof Minion))
            return false;
        return !upgradeItem.getAttributeHandler().isMithrilInfused();
    }
}
