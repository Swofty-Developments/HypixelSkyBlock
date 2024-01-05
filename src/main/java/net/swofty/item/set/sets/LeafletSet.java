package net.swofty.item.set.sets;

import net.swofty.event.value.SkyBlockValueEvent;
import net.swofty.event.value.ValueUpdateEvent;
import net.swofty.event.value.events.RegenerationValueUpdateEvent;
import net.swofty.item.set.impl.ArmorSet;
import net.swofty.region.RegionType;
import net.swofty.region.SkyBlockRegion;

public class LeafletSet extends SkyBlockValueEvent implements ArmorSet {
    @Override
    public Class<? extends ValueUpdateEvent> getValueEvent() {
        return RegenerationValueUpdateEvent.class;
    }

    @Override
    public void run(ValueUpdateEvent event) {
        float value = (float) event.getValue();

        if (!isWearingSet(event.getPlayer())) return;

        SkyBlockRegion region = event.getPlayer().getRegion();

        if (region == null || region.getType() != RegionType.FOREST) {
            return;
        }

        event.setValue(value + 55);
    }

    @Override
    public String getName() {
        return "Energy of the Forest";
    }

    @Override
    public String getDescription() {
        return "§7While in a Forest zone you regain §c55.0 Health §7every second.";
    }
}
