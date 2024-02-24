package net.swofty.types.generic.protocol.auctions;

import net.swofty.service.protocol.JacksonSerializer;
import net.swofty.service.protocol.ProtocolSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtocolDeprecateItem extends ProtocolSpecification {
    @Override
    public List<ProtocolEntries<?>> getServiceProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<UUID>("uuid", true,
                        new JacksonSerializer<>(UUID.class))
        ));
    }

    @Override
    public List<ProtocolEntries<?>> getReturnedProtocolEntries() {
        return new ArrayList<>();
    }

    @Override
    public String getEndpoint() {
        return "deprecate-item";
    }
}
