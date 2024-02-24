package net.swofty.types.generic.protocol.bazaar;

import net.swofty.service.protocol.JacksonSerializer;
import net.swofty.service.protocol.ProtocolSpecification;
import org.bson.Document;

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
                new ProtocolEntries<Document>("item", true, new JacksonSerializer<>(
                        Document.class
                ))
        ));
    }

    @Override
    public String getEndpoint() {
        return "bazaar-get-item";
    }
}
