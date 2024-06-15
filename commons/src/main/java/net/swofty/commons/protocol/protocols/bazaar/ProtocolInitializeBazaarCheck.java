package net.swofty.commons.protocol.protocols.bazaar;

import net.swofty.commons.bazaar.BazaarInitializationRequest;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolSpecification;

import java.util.ArrayList;
import java.util.List;

public class ProtocolInitializeBazaarCheck extends ProtocolSpecification {
    @Override
    public List<ProtocolEntries<?>> getServiceProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<BazaarInitializationRequest>("init-request", true,
                        new JacksonSerializer<>(BazaarInitializationRequest.class))
        ));
    }

    @Override
    public List<ProtocolEntries<?>> getReturnedProtocolEntries() {
        return new ArrayList<>();
    }

    @Override
    public String getEndpoint() {
        return "bazaar-initialize";
    }
}
