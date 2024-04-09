package net.swofty.types.generic.item.items.miscellaneous;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Interactable;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class GodPotion implements CustomSkyBlockItem, SkullHead, Interactable, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.EMPTY;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjAyMjZkNGMxZDMwZmJlYmVjYWU5MzlkYTkwMDYwM2U0Y2QwZmVkODU5MmExYmIzZTExZjlhYzkyMzkxYTQ1YSJ9fX0=";
    }

    @Override
    public List<String> getAbsoluteLore(SkyBlockPlayer player, SkyBlockItem item) {
        return Arrays.asList(
                "§7Consume this potion to receive an",
                "§7assortment of positive §dpotion",
                "§deffects§7!",
                "",
                "§7Duration:",
                "§a12 hours §7+ §a0 hours §7(§bAlchemy Level§7)",
                "",
                "§eRight-click to consume."
        );
    }
}
