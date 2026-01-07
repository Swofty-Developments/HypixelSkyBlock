package net.swofty.commons.party.events.response;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PartyMemberDisconnectTimeoutResponseEvent extends PartyResponseEvent {
    private final UUID timedOutPlayer;
    private final boolean wasLeader;

    public PartyMemberDisconnectTimeoutResponseEvent(FullParty party, UUID timedOutPlayer, boolean wasLeader) {
        super(party);
        this.timedOutPlayer = timedOutPlayer;
        this.wasLeader = wasLeader;
    }

    public UUID getTimedOutPlayer() { return timedOutPlayer; }
    public boolean wasLeader() { return wasLeader; }

    @Override
    public Serializer<PartyMemberDisconnectTimeoutResponseEvent> getSerializer() {
        return new Serializer<PartyMemberDisconnectTimeoutResponseEvent>() {
            @Override
            public String serialize(PartyMemberDisconnectTimeoutResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((FullParty) value.getParty()));
                json.put("timedOutPlayer", value.timedOutPlayer.toString());
                json.put("wasLeader", value.wasLeader);
                return json.toString();
            }

            @Override
            public PartyMemberDisconnectTimeoutResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID timedOutPlayer = UUID.fromString(jsonObject.getString("timedOutPlayer"));
                boolean wasLeader = jsonObject.getBoolean("wasLeader");
                return new PartyMemberDisconnectTimeoutResponseEvent(party, timedOutPlayer, wasLeader);
            }

            @Override
            public PartyMemberDisconnectTimeoutResponseEvent clone(PartyMemberDisconnectTimeoutResponseEvent value) {
                return new PartyMemberDisconnectTimeoutResponseEvent((FullParty) value.getParty(), value.timedOutPlayer, value.wasLeader);
            }
        };
    }
}
