package net.swofty.types.generic.item.items.combat.slayer.spider.drops;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.RightClickRecipe;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class SpiderCatalyst implements CustomSkyBlockItem, SkullHead, Sellable, RightClickRecipe {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 2000;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "983b30e9d135b05190eea2c3ac61e2ab55a2d81e1a58dbb26983a14082664";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Drops rarely from Tarantula",
                "§7Broodfather when you are at",
                "§7least Spider Slayer LVL 2.",
                "",
                "§5Catalysts upgrade other items!"));
    }

    @Override
    public ItemType getRecipeItem() {
        return ItemType.RECLUSE_FANG;
    }
}
