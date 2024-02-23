package net.swofty.types.generic.protocol.bazaar;

import net.swofty.service.generic.ProtocolSpecification;

import java.util.ArrayList;
import java.util.List;

public class ProtocolBazaarGetItem extends ProtocolSpecification {
    @Override
    public List<ProtocolEntries<?>> getServiceProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<String>("item-name", true)
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
        return "bazaar-get-item";
    }
}
