package net.swofty.types.generic.item.items.vanilla.bow;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.PlayerProjectile;
import net.swofty.types.generic.entity.ArrowEntityImpl;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.BowImpl;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.QuiverDisplayOnHold;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class Bow implements CustomSkyBlockItem, BowImpl, QuiverDisplayOnHold {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 30D)
                .build();
    }

    @Override
    public boolean shouldBeArrow() {
        return true;
    }

    @Override
    public void onBowShoot(SkyBlockPlayer player, SkyBlockItem item) {
        SkyBlockItem arrow = player.getAndConsumeArrow();
        if (arrow == null) return;

        ArrowEntityImpl arrowEntity = new ArrowEntityImpl(player);
        Vec arrowVelocity = calculateArrowVelocity(
                player.getPosition().pitch(),
                player.getPosition().yaw());
        arrowEntity.setVelocity(arrowVelocity);
        arrowEntity.setInstance(player.getInstance(), player.getPosition().add(0, 1, 0));
    }
}
