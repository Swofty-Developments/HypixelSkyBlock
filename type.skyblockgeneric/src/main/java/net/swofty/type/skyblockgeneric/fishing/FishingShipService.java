package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointShipState;
import net.swofty.type.skyblockgeneric.item.components.FishingShipPartComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public final class FishingShipService {
    private FishingShipService() {
    }

    public static DatapointShipState.ShipState getState(SkyBlockPlayer player) {
        return player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.SHIP_STATE, DatapointShipState.class).getValue();
    }

    public static void installPart(SkyBlockPlayer player, String itemId, FishingShipPartComponent definition) {
        DatapointShipState.ShipState state = getState(player);
        switch (definition.getSlot()) {
            case HELM -> state.setHelm(itemId);
            case ENGINE -> state.setEngine(itemId);
            case HULL -> state.setHull(itemId);
        }
        player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.SHIP_STATE, DatapointShipState.class).setValue(state);
    }

    public static void unlockDestination(SkyBlockPlayer player, String destinationId) {
        DatapointShipState.ShipState state = getState(player);
        state.unlockDestination(destinationId);
        player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.SHIP_STATE, DatapointShipState.class).setValue(state);
    }
}
