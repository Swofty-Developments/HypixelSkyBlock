package net.swofty.commons.party.events.response;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PartyMemberKickResponseEvent extends PartyResponseEvent {
    private final UUID kicker;
    private final UUID kicked;

    public PartyMemberKickResponseEvent(FullParty party, UUID kicker, UUID kicked) {
        super(party);
        this.kicker = kicker;
        this.kicked = kicked;
    }

    public UUID getKicker() { return kicker; }
    public UUID getKicked() { return kicked; }

    @Override
    public Serializer<PartyMemberKickResponseEvent> getSerializer() {
        return new Serializer<PartyMemberKickResponseEvent>() {
            @Override
            public String serialize(PartyMemberKickResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((FullParty) value.getParty()));
                json.put("kicker", value.kicker.toString());
                json.put("kicked", value.kicked.toString());
                return json.toString();
            }

            @Override
            public PartyMemberKickResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID kicker = UUID.fromString(jsonObject.getString("kicker"));
                UUID kicked = UUID.fromString(jsonObject.getString("kicked"));
                return new PartyMemberKickResponseEvent(party, kicker, kicked);
            }

            @Override
            public PartyMemberKickResponseEvent clone(PartyMemberKickResponseEvent value) {
                return new PartyMemberKickResponseEvent((FullParty) value.getParty(), value.kicker, value.kicked);
            }
        };
    }
}