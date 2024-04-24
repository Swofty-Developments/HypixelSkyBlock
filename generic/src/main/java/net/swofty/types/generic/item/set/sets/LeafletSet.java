package net.swofty.types.generic.item.set.sets;

import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.RegenerationValueUpdateEvent;
import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.region.SkyBlockRegion;

import java.util.ArrayList;
import java.util.Arrays;

public class LeafletSet extends SkyBlockValueEvent implements ArmorSet {
    @Override
    public String getName() {
        return "Energy of the Forest";
    }

    @Override
    public ArrayList<String> getDescription() {
        return new ArrayList<>(Arrays.asList(
                "While in a Forest zone you regain §a5.0",
                "§c♥ Health §7every second."
        ));
    }

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

        event.setValue(value + 5);
    }
}
