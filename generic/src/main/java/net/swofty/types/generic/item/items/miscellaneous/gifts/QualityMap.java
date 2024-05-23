package net.swofty.types.generic.item.items.miscellaneous.gifts;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.List;

public class QualityMap implements CustomSkyBlockItem, Sellable, Enchanted {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(List.of(
                "§7It took motivation, skills, effort",
                "§7and a lot of love and caring to",
                "§7create this map. By far the best",
                "§7quality on the market!"
        ));
    }
    @Override
    public double getSellValue() {
        return 1337;
    }
}
