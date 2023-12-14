package net.swofty.data.datapoints;

import net.swofty.data.Datapoint;
import net.swofty.data.serializer.InventorySerializer;
import net.swofty.data.serializer.JacksonSerializer;
import net.swofty.user.SkyBlockInventory;

public class DatapointInventory extends Datapoint<SkyBlockInventory> {
    private static final InventorySerializer<SkyBlockInventory> serializer = new InventorySerializer<>(SkyBlockInventory.class);

    public DatapointInventory(String key, SkyBlockInventory value) {
        super(key, value, serializer);
    }

    public DatapointInventory(String key) {
        super(key, null, serializer);
    }
}