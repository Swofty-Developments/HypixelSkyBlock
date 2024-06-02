package net.swofty.types.generic.protocol.bazaar;

import net.swofty.service.protocol.ProtocolSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtocolBazaarAttemptBuyOrder extends ProtocolSpecification {
    @Override
    public List<ProtocolEntries<?>> getServiceProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<String>("item-name", true),
                new ProtocolEntries<UUID>("player-uuid", true),
                new ProtocolEntries<Integer>("price", true),
                new ProtocolEntries<Integer>("amount", true)
        ));
    }

    @Override
    public List<ProtocolEntries<?>> getReturnedProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<Boolean>("successful", true)
        ));
    }

    @Override
    public String getEndpoint() {
        return "bazaar-buy-order";
    }
}
