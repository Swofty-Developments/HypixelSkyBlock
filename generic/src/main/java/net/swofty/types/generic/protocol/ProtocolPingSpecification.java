package net.swofty.types.generic.protocol;

import net.swofty.service.protocol.ProtocolSpecification;

import java.util.List;

public class ProtocolPingSpecification extends ProtocolSpecification {
    @Override
    public List<ProtocolEntries<?>> getServiceProtocolEntries() {
        return List.of();
    }

    @Override
    public List<ProtocolEntries<?>> getReturnedProtocolEntries() {
        return List.of();
    }

    @Override
    public String getEndpoint() {
        return "service-ping";
    }
}
