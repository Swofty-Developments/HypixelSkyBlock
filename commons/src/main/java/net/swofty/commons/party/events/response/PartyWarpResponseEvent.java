package net.swofty.commons.party.events.response;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PartyWarpResponseEvent extends PartyResponseEvent {
    private final UUID warper;

    public PartyWarpResponseEvent(FullParty party, UUID warper) {
        super(party);
        this.warper = warper;
    }

    public UUID getWarper() { return warper; }

    @Override
    public Serializer<PartyWarpResponseEvent> getSerializer() {
        return new Serializer<PartyWarpResponseEvent>() {
            @Override
            public String serialize(PartyWarpResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((FullParty) value.getParty()));
                json.put("warper", value.warper.toString());
                return json.toString();
            }

            @Override
            public PartyWarpResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID warper = UUID.fromString(jsonObject.getString("warper"));
                return new PartyWarpResponseEvent(party, warper);
            }

            @Override
            public PartyWarpResponseEvent clone(PartyWarpResponseEvent value) {
                return new PartyWarpResponseEvent((FullParty) value.getParty(), value.warper);
            }
        };
    }
}
