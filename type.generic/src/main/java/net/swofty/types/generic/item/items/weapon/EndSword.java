package net.swofty.types.generic.item.items.weapon;

import net.minestom.server.entity.EntityType;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.PlayerDamageMobValueUpdateEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class EndSword extends SkyBlockValueEvent implements CustomSkyBlockItem, StandardItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 35D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "Deals §a+100% §7damage to Ender",
                "Dragons, Endermites, and Endermen."
        ));
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
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
        if(item.getAttributeHandler().getItemTypeAsType() != ItemType.END_SWORD) return;
        if (event.getMob().getEntityType() == EntityType.ENDER_DRAGON || event.getMob().getEntityType() == EntityType.ENDERMITE || event.getMob().getEntityType() == EntityType.ENDERMAN) {
            event.setValue((((float) event.getValue()) * 2));
        }
    }
}