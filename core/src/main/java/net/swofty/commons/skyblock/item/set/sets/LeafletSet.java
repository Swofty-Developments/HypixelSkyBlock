package net.swofty.commons.skyblock.item.set.sets;

import net.swofty.commons.skyblock.item.set.impl.ArmorSet;
import net.swofty.commons.skyblock.region.SkyBlockRegion;
import net.swofty.commons.skyblock.event.value.SkyBlockValueEvent;
import net.swofty.commons.skyblock.event.value.ValueUpdateEvent;
import net.swofty.commons.skyblock.event.value.events.RegenerationValueUpdateEvent;
import net.swofty.commons.skyblock.region.RegionType;

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
