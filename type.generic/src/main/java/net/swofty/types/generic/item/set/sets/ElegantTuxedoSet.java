package net.swofty.types.generic.item.set.sets;

import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.MaxHealthValueUpdateEvent;
import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class ElegantTuxedoSet extends SkyBlockValueEvent implements ArmorSet {
    @Override
    public String getName() {
        return "Dashing";
    }

    @Override
    public ArrayList<String> getDescription() {
        return new ArrayList<>(Arrays.asList(
                "Max health set to §c250♥§7.",
                "Deal §c+150% §7damage!",
                "§8Very stylish."
        ));
    }

    @Override
    public Class<? extends ValueUpdateEvent> getValueEvent() {
        return MaxHealthValueUpdateEvent.class;
    }

    @Override
    public void run(ValueUpdateEvent tempEvent) {
        MaxHealthValueUpdateEvent event = (MaxHealthValueUpdateEvent) tempEvent;

        if (!isWearingSet(event.getPlayer())) return;

        event.setValue(250F);
    }

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .withAdditivePercentage(ItemStatistic.DAMAGE, 150D)
                .build();
    }
}
