package net.swofty.types.generic.item.items.miscellaneous.gifts;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.List;

public class GameBreaker implements CustomSkyBlockItem, Sellable, Enchanted {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }
    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(List.of(
                "§7This item was given to a player",
                "§7who reported a game breaking",
                "§7exploit. What a guy!"
        ));
    }

    @Override
    public double getSellValue() {
        return 777;
    }
}
