package net.swofty.types.generic.protocol.auctions;

import net.swofty.service.generic.ProtocolSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtocolFetchItem extends ProtocolSpecification {
    @Override
    public List<ProtocolEntries<?>> getServiceProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<UUID>("uuid", true)
        ));
    }

    @Override
    public List<ProtocolEntries<?>> getReturnedProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<String>("item", true)
        ));
    }

    @Override
    public String getEndpoint() {
        return "fetch-item";
    }
}
