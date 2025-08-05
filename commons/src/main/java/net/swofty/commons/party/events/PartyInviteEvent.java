package net.swofty.commons.party.events;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.party.PendingParty;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

public class PartyInviteEvent extends PartyEvent {
    public PartyInviteEvent(PendingParty party) {
        super(party);
    }

    @Override
    public Serializer<PartyInviteEvent> getSerializer() {
        return new Serializer<PartyInviteEvent>() {
            @Override
            public String serialize(PartyInviteEvent value) {
                JSONObject json = new JSONObject();
                Serializer<PendingParty> partySerializer = PendingParty.getStaticSerializer();
                json.put("party", partySerializer.serialize((PendingParty) value.getParty()));
                return json.toString();
            }

            @Override
            public PartyInviteEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                PendingParty party = PendingParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                return new PartyInviteEvent(party);
            }

            @Override
            public PartyInviteEvent clone(PartyInviteEvent value) {
                return new PartyInviteEvent((PendingParty) value.getParty());
            }
        };
    }
}
