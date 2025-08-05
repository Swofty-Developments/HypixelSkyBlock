package net.swofty.commons.party.events.response;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PartyPlayerSwitchedServerResponseEvent extends PartyResponseEvent {
    private final UUID mover;

    public PartyPlayerSwitchedServerResponseEvent(FullParty party, UUID mover) {
        super(party);
        this.mover = mover;
    }

    public UUID getMover() { return mover; }

    @Override
    public Serializer<PartyPlayerSwitchedServerResponseEvent> getSerializer() {
        return new Serializer<PartyPlayerSwitchedServerResponseEvent>() {
            @Override
            public String serialize(PartyPlayerSwitchedServerResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((FullParty) value.getParty()));
                json.put("mover", value.mover.toString());
                return json.toString();
            }

            @Override
            public PartyPlayerSwitchedServerResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID mover = UUID.fromString(jsonObject.getString("mover"));
                return new PartyPlayerSwitchedServerResponseEvent(party, mover);
            }

            @Override
            public PartyPlayerSwitchedServerResponseEvent clone(PartyPlayerSwitchedServerResponseEvent value) {
                return new PartyPlayerSwitchedServerResponseEvent((FullParty) value.getParty(), value.mover);
            }
        };
    }
}
