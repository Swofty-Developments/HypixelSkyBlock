package net.swofty.commons.party.events.response;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PartyMemberRejoinedResponseEvent extends PartyResponseEvent {
    private final UUID rejoinedPlayer;

    public PartyMemberRejoinedResponseEvent(FullParty party, UUID rejoinedPlayer) {
        super(party);
        this.rejoinedPlayer = rejoinedPlayer;
    }

    public UUID getRejoinedPlayer() { return rejoinedPlayer; }

    @Override
    public Serializer<PartyMemberRejoinedResponseEvent> getSerializer() {
        return new Serializer<PartyMemberRejoinedResponseEvent>() {
            @Override
            public String serialize(PartyMemberRejoinedResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((FullParty) value.getParty()));
                json.put("rejoinedPlayer", value.rejoinedPlayer.toString());
                return json.toString();
            }

            @Override
            public PartyMemberRejoinedResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID rejoinedPlayer = UUID.fromString(jsonObject.getString("rejoinedPlayer"));
                return new PartyMemberRejoinedResponseEvent(party, rejoinedPlayer);
            }

            @Override
            public PartyMemberRejoinedResponseEvent clone(PartyMemberRejoinedResponseEvent value) {
                return new PartyMemberRejoinedResponseEvent((FullParty) value.getParty(), value.rejoinedPlayer);
            }
        };
    }
}
