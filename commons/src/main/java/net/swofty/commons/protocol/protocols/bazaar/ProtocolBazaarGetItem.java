package net.swofty.commons.protocol.protocols.bazaar;

import net.swofty.commons.bazaar.BazaarItem;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolSpecification;

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
                new ProtocolEntries<BazaarItem>("item", true, new JacksonSerializer<>(
                        BazaarItem.class
                ))
        ));
    }

    @Override
    public String getEndpoint() {
        return "bazaar-get-item";
    }
}
