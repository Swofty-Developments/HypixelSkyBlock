package net.swofty.types.generic.item.items.weapon;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.user.statistics.TemporaryStatistic;

import java.util.ArrayList;

public class RogueSword implements CustomSkyBlockItem, CustomSkyBlockAbility, StandardItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 20D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return null;
    }

    @Override
    public String getAbilityName() {
        return "Speed Boost";
    }

    @Override
    public String getAbilityDescription() {
        return "Grants §f+100 speed §7for §a30s";
    }

    @Override
    public void onAbilityUse(SkyBlockPlayer player, SkyBlockItem sItem) {
        player.getStatistics().boostStatistic(TemporaryStatistic.builder()
                .withStatistic(ItemStatistic.SPEED)
                .withValue(100D)
                .withExpirationInTicks(30 * 20)
                .build());
    }

    @Override
    public int getManaCost() {
        return 50;
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 30;
    }

    @Override
    public AbilityActivation getAbilityActivation() {
        return AbilityActivation.RIGHT_CLICK;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }
}
