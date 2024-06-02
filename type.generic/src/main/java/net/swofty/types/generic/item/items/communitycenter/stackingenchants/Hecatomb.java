package net.swofty.types.generic.item.items.communitycenter.stackingenchants;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.List;

public class Hecatomb implements CustomSkyBlockItem, Enchanted, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§9Hecatomb I",
                "§7Gain §a+0.28% §cCatacombs §7XP \u0026 §a+0.56%",
                "§a§3Class §7XP, doubled §7on §b§lS+ §7runs.",
                "§7Grants §c+2.6❤ §7per 10 §cCatacombs §7levels.");
    }
}
