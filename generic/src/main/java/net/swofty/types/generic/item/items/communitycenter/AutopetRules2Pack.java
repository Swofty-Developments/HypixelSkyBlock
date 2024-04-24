package net.swofty.types.generic.item.items.communitycenter;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AutopetRules2Pack implements CustomSkyBlockItem, SkullHead, CustomDisplayName, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "c316a8b96cb9aeea5140dd936c6ebb964efcbfd71ae217d9f70888efb14e7c0";
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Adds §b+2 §7rules to your",
                "§7§cAutopet§7!",
                "",
                "§7The §cAutopet §7allows you",
                "§7to §aautomatically §7equip",
                "§7pets depending on rules you",
                "§7choose.",
                "",
                "§eHold and click to consume!",
                "",
                "§7§8Find the Autopet in your",
                "§8Pets menu after unlocking at",
                "§8least 1 rule.");
    }

    @Override
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Autopet Rules 2-Pack";
    }
}
