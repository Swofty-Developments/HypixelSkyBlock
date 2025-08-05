package net.swofty.commons.party.events.response;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PartyLeaderTransferResponseEvent extends PartyResponseEvent {
    private final UUID oldLeader;
    private final UUID newLeader;

    public PartyLeaderTransferResponseEvent(FullParty party, UUID oldLeader, UUID newLeader) {
        super(party);
        this.oldLeader = oldLeader;
        this.newLeader = newLeader;
    }

    public UUID getOldLeader() { return oldLeader; }
    public UUID getNewLeader() { return newLeader; }

    @Override
    public Serializer<PartyLeaderTransferResponseEvent> getSerializer() {
        return new Serializer<PartyLeaderTransferResponseEvent>() {
            @Override
            public String serialize(PartyLeaderTransferResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((FullParty) value.getParty()));
                json.put("oldLeader", value.oldLeader.toString());
                json.put("newLeader", value.newLeader.toString());
                return json.toString();
            }

            @Override
            public PartyLeaderTransferResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID oldLeader = UUID.fromString(jsonObject.getString("oldLeader"));
                UUID newLeader = UUID.fromString(jsonObject.getString("newLeader"));
                String oldLeaderName = jsonObject.getString("oldLeaderName");
                String newLeaderName = jsonObject.getString("newLeaderName");
                return new PartyLeaderTransferResponseEvent(party, oldLeader, newLeader);
            }

            @Override
            public PartyLeaderTransferResponseEvent clone(PartyLeaderTransferResponseEvent value) {
                return new PartyLeaderTransferResponseEvent((FullParty) value.getParty(), value.oldLeader, value.newLeader);
            }
        };
    }
}
