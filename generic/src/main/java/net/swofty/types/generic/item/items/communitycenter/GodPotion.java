package net.swofty.types.generic.item.items.communitycenter;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Interactable;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GodPotion implements CustomSkyBlockItem, SkullHead, Interactable, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "60226d4c1d30fbebecae939da900603e4cd0fed8592a1bb3e11f9ac92391a45a";
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(List.of(
                "§7Consume this potion to receive an",
                "§7assortment of positive §dpotion",
                "§deffects§7!",
                "",
                "§7Duration:",
                "§a12 hours §7+ §a0 hours §7(§bAlchemy Level§7)",
                "",
                "§eRight-click to consume."
        ));
    }
}
