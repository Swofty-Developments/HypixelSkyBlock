package net.swofty.types.generic.item.set.sets;

import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.RegenerationValueUpdateEvent;
import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.region.SkyBlockRegion;

import java.util.Arrays;
import java.util.List;

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
    public List<String> getDescription() {
        return Arrays.asList(
                "§7While in a Forest zone you regain",
                "§c55.0 Health §7every second.");
    }
}
