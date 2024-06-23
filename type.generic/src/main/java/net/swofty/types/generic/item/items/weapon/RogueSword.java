package net.swofty.types.generic.item.items.weapon;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockAbility;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.user.statistics.TemporaryStatistic;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RogueSword implements CustomSkyBlockItem, CustomSkyBlockAbility, StandardItem, TrackedUniqueItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 20D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return null;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }

    @Override
    public List<Ability> getAbilities() {
        return List.of(
                new Ability() {
                    @Override
                    public @NotNull String getName() {
                        return "Speed Boost";
                    }

                    @Override
                    public @NotNull String getDescription() {
                        return "Grants §f+100✦ Speed §7for §a30s";
                    }

                    @Override
                    public @NotNull AbilityActivation getAbilityActivation() {
                        return AbilityActivation.RIGHT_CLICK;
                    }

                    @Override
                    public int getCooldownTicks() {
                        return 30;
                    }

                    @Override
                    public @NotNull AbilityCost getAbilityCost() {
                        return new AbilityManaCost(50);
                    }

                    @Override
                    public void onUse(@NotNull SkyBlockPlayer player, @NotNull SkyBlockItem sItem) {
                        player.getStatistics().boostStatistic(TemporaryStatistic.builder()
                                .withStatistics(ItemStatistics.builder().withBase(ItemStatistic.SPEED, 100D).build())
                                .withExpirationInTicks(30 * 20)
                                .build());
                    }
                }
        );
    }
}
