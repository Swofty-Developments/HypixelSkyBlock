package net.swofty.service.generic;

import net.swofty.commons.ServiceType;

import java.util.List;

public class PingProtocolSpecification extends ProtocolSpecification {
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
