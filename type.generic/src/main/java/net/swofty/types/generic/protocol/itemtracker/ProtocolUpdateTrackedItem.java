package net.swofty.types.generic.protocol.itemtracker;

import net.swofty.service.protocol.ProtocolSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtocolUpdateTrackedItem extends ProtocolSpecification {
    @Override
    public List<ProtocolEntries<?>> getServiceProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<UUID>("item-uuid", true),
                new ProtocolEntries<UUID>("attached-player-uuid", true),
                new ProtocolEntries<UUID>("attached-player-profile", true),
                new ProtocolEntries<String>("item-type", true)
        ));
    }

    @Override
    public List<ProtocolEntries<?>> getReturnedProtocolEntries() {
        return new ArrayList<>(List.of());
    }

    @Override
    public String getEndpoint() {
        return "update-tracked-item";
    }
}
