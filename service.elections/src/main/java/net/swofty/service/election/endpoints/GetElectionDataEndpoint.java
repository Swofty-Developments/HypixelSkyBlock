package net.swofty.service.election.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.election.GetElectionDataProtocolObject;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;

public class GetElectionDataEndpoint implements ServiceEndpoint
        <GetElectionDataProtocolObject.GetElectionDataMessage,
                GetElectionDataProtocolObject.GetElectionDataResponse> {

    @Override
    public ProtocolObject<GetElectionDataProtocolObject.GetElectionDataMessage,
            GetElectionDataProtocolObject.GetElectionDataResponse> associatedProtocolObject() {
        return new GetElectionDataProtocolObject();
    }

    @Override
    public GetElectionDataProtocolObject.GetElectionDataResponse onMessage(
            ServiceProxyRequest message,
            GetElectionDataProtocolObject.GetElectionDataMessage messageObject) {
        String data = ElectionDatabase.loadElectionData();
        if (data == null) {
            return new GetElectionDataProtocolObject.GetElectionDataResponse(false, null);
        }
        return new GetElectionDataProtocolObject.GetElectionDataResponse(true, data);
    }
}
