package net.swofty.service.election.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.election.SaveElectionDataProtocolObject;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;

public class SaveElectionDataEndpoint implements ServiceEndpoint
        <SaveElectionDataProtocolObject.SaveElectionDataMessage,
                SaveElectionDataProtocolObject.SaveElectionDataResponse> {

    @Override
    public ProtocolObject<SaveElectionDataProtocolObject.SaveElectionDataMessage,
            SaveElectionDataProtocolObject.SaveElectionDataResponse> associatedProtocolObject() {
        return new SaveElectionDataProtocolObject();
    }

    @Override
    public SaveElectionDataProtocolObject.SaveElectionDataResponse onMessage(
            ServiceProxyRequest message,
            SaveElectionDataProtocolObject.SaveElectionDataMessage messageObject) {
        try {
            ElectionDatabase.saveElectionData(messageObject.serializedData());
            return new SaveElectionDataProtocolObject.SaveElectionDataResponse(true);
        } catch (Exception e) {
            return new SaveElectionDataProtocolObject.SaveElectionDataResponse(false);
        }
    }
}
