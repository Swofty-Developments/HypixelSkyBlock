package net.swofty.types.generic.item.items.combat;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class ZombieHeart implements CustomSkyBlockItem, NotFinishedYet, Sellable, SkullHead, TrackedUniqueItem, StandardItem {

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 50D)
                .build();
    }

    @Override
    public double getSellValue() {
        return 123000;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "71d7c816fc8c636d7f50a93a0ba7aaeff06c96a561645e9eb1bef391655c531";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: Healing Boost",
                "§7Doubles your §a☄ Mending §7and",
                "§7§4♨ Vitality §7while wearing.",
                "",
                "§2This armor piece is undead ༕!"));
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HELMET;
    }
}
