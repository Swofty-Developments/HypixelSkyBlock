package net.swofty.types.generic.data.datapoints;

import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.serializer.InventorySerializer;
import net.swofty.types.generic.user.SkyBlockInventory;

public class DatapointInventory extends Datapoint<SkyBlockInventory> {
    private static final InventorySerializer<SkyBlockInventory> serializer = new InventorySerializer<>(SkyBlockInventory.class);

    public DatapointInventory(String key, SkyBlockInventory value) {
        super(key, value, serializer);
    }

    public DatapointInventory(String key) {
        super(key, null, serializer);
    }
}