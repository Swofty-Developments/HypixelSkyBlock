package net.swofty.commons.party.events.response;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PartyDisbandResponseEvent extends PartyResponseEvent {
    private final UUID disbander;
    private final String reason;
    private final FullParty party;

    public PartyDisbandResponseEvent(FullParty party, UUID disbander, String reason) {
        super(party);
        this.disbander = disbander;
        this.reason = reason;
        this.party = party;
    }

    public UUID getDisbander() { return disbander; }
    public String getReason() { return reason; }
    public FullParty getParty() { return party; }

    @Override
    public Serializer<PartyDisbandResponseEvent> getSerializer() {
        return new Serializer<PartyDisbandResponseEvent>() {
            @Override
            public String serialize(PartyDisbandResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((FullParty) value.getParty()));
                json.put("disbander", value.disbander.toString());
                json.put("reason", value.reason);
                return json.toString();
            }

            @Override
            public PartyDisbandResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID disbander = UUID.fromString(jsonObject.getString("disbander"));
                String reason = jsonObject.getString("reason");
                return new PartyDisbandResponseEvent(party, disbander, reason);
            }

            @Override
            public PartyDisbandResponseEvent clone(PartyDisbandResponseEvent value) {
                return new PartyDisbandResponseEvent((FullParty) value.getParty(), value.disbander, value.reason);
            }
        };
    }
}

