package net.swofty.commons.skyblock.data.datapoints;

import net.swofty.commons.skyblock.data.Datapoint;
import net.swofty.commons.skyblock.serializer.InventorySerializer;
import net.swofty.commons.skyblock.user.SkyBlockInventory;

public class DatapointInventory extends Datapoint<SkyBlockInventory> {
    private static final InventorySerializer<SkyBlockInventory> serializer = new InventorySerializer<>(SkyBlockInventory.class);

    public DatapointInventory(String key, SkyBlockInventory value) {
        super(key, value, serializer);
    }

    public DatapointInventory(String key) {
        super(key, null, serializer);
    }
}