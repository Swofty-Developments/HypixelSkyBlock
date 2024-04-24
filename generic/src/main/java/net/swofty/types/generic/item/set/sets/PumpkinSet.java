package net.swofty.types.generic.item.set.sets;

import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.PlayerDamagedByMobValueUpdateEvent;
import net.swofty.types.generic.item.set.impl.ArmorSet;

import java.util.ArrayList;
import java.util.Arrays;

public class PumpkinSet extends SkyBlockValueEvent implements ArmorSet {
    @Override
    public String getName() {
        return "Pumpkin Buff";
    }

    @Override
    public ArrayList<String> getDescription() {
        return new ArrayList<>(Arrays.asList(
                "Reduces all taken damage by §a+10%",
                "and deal &a+10% §7more damage."
        ));
    }

    @Override
    public Class<? extends ValueUpdateEvent> getValueEvent() {
        return PlayerDamagedByMobValueUpdateEvent.class;
    }

    @Override
    public void run(ValueUpdateEvent event) {
        float value = (float) event.getValue();

        if (!isWearingSet(event.getPlayer())) return;

        event.setValue(value * 0.9);
    }
}
