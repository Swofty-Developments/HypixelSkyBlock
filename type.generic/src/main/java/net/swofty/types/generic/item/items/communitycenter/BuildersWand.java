package net.swofty.types.generic.item.items.communitycenter;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.List;

public class BuildersWand implements CustomSkyBlockItem, Enchanted, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "",
                "§6Ability: Grand Architect  §e§lRIGHT CLICK",
                "§7Right-Click the face of a block to",
                "§7extend all connected block faces.",
                "",
                "§6Ability: Built-in Storage  §e§lLEFT CLICK",
                "§7Opens the wand storage. Blocks will",
                "§7be placed from your inventory or",
                "§7the wand storage.");
    }
}
