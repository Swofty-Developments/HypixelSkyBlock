package net.swofty.types.generic.item.items.weapon;

import net.minestom.server.coordinate.Vec;
import net.swofty.types.generic.entity.ArrowEntityImpl;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.ShortBowImpl;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class ArtisanalShortbow implements CustomSkyBlockItem, ShortBowImpl {
    @Override
    public void onBowShoot(SkyBlockPlayer player, SkyBlockItem item) {
        SkyBlockItem arrow = player.getAndConsumeArrow();
        if (arrow == null) return;

        ArrowEntityImpl arrowEntity = new ArrowEntityImpl(player, item);
        Vec arrowVelocity = calculateArrowVelocity(
                player.getPosition().pitch(),
                player.getPosition().yaw());
        arrowEntity.setVelocity(arrowVelocity);
        arrowEntity.setInstance(player.getInstance(), calculateArrowSpawnPosition(player));
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§9Shortbow: Instantly shoots!"
        ));
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 40D)
                .build();
    }

    @Override
    public boolean shouldBeArrow() {
        return false;
    }
}
