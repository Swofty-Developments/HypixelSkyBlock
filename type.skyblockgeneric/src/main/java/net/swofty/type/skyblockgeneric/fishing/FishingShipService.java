package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointShipState;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public final class FishingShipService {
    private FishingShipService() {
    }

    public static DatapointShipState.ShipState getState(SkyBlockPlayer player) {
        return player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.SHIP_STATE, DatapointShipState.class).getValue();
    }

    public static void installPart(SkyBlockPlayer player, ShipPartDefinition definition) {
        DatapointShipState.ShipState state = getState(player);
        switch (definition.slot()) {
            case HELM -> state.setHelm(definition.itemId());
            case ENGINE -> state.setEngine(definition.itemId());
            case HULL -> state.setHull(definition.itemId());
        }
        player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.SHIP_STATE, DatapointShipState.class).setValue(state);
    }

    public static void unlockDestination(SkyBlockPlayer player, String destinationId) {
        DatapointShipState.ShipState state = getState(player);
        state.unlockDestination(destinationId);
        player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.SHIP_STATE, DatapointShipState.class).setValue(state);
    }
}
