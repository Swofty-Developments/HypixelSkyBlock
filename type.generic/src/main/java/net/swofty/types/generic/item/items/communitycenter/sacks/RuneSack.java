package net.swofty.types.generic.item.items.communitycenter.sacks;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Sack;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RuneSack implements SkullHead, NotFinishedYet, Sack {

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Rune pickups go directly into your",
                "§7sacks.",
                "",
                "§7Capacity: §e64 per rune tier",
                "§8Sacks sum their capacity.",
                "",
                "§eRight Click to open sack!");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "e91eb4ee3ecce3447483a1f151c11facc9de25577ab7b51fbf3d9c2b2a4b69fc";
    }

    @Override
    public List<ItemTypeLinker> getSackItems() {
        return List.of(ItemTypeLinker.BLOOD_RUNE);
    }

    @Override
    public int getMaximumCapacity() {
        return 0;
    }
}
