package net.swofty.types.generic.item.items.weapon;

import net.minestom.server.entity.EntityType;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.PlayerDamageMobValueUpdateEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.BowImpl;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class WitherBow extends SkyBlockValueEvent implements CustomSkyBlockItem, BowImpl {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 30D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "Deals §a+100% §7damage to §aWithers §7and",
                "§aWither Skeletons§7."
        ));
    }

    @Override
    public void onBowShoot(SkyBlockPlayer player, SkyBlockItem item) {}

    @Override
    public boolean shouldBeArrow() {
        return false;
    }

    @Override
    public Class<? extends ValueUpdateEvent> getValueEvent() {
        return PlayerDamageMobValueUpdateEvent.class;
    }

    @Override
    public void run(ValueUpdateEvent tempEvent) {
        PlayerDamageMobValueUpdateEvent event = (PlayerDamageMobValueUpdateEvent) tempEvent;
        SkyBlockPlayer player = event.getPlayer();
        SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());
        if(item.isNA() || item.isAir()) return;
        if(item.getAttributeHandler().getItemTypeAsType() != ItemType.WITHER_BOW) return;
        if ( event.getMob().getEntityType() == EntityType.WITHER_SKELETON || event.getMob().getEntityType() == EntityType.WITHER) {
            event.setValue((((float) event.getValue()) *2));
        }
    }
}
