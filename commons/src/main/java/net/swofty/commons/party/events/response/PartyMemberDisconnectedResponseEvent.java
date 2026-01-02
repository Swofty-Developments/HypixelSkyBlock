package net.swofty.commons.party.events.response;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PartyMemberDisconnectedResponseEvent extends PartyResponseEvent {
    private final UUID disconnectedPlayer;
    private final long timeoutSeconds;

    public PartyMemberDisconnectedResponseEvent(FullParty party, UUID disconnectedPlayer, long timeoutSeconds) {
        super(party);
        this.disconnectedPlayer = disconnectedPlayer;
        this.timeoutSeconds = timeoutSeconds;
    }

    public UUID getDisconnectedPlayer() { return disconnectedPlayer; }
    public long getTimeoutSeconds() { return timeoutSeconds; }

    @Override
    public Serializer<PartyMemberDisconnectedResponseEvent> getSerializer() {
        return new Serializer<PartyMemberDisconnectedResponseEvent>() {
            @Override
            public String serialize(PartyMemberDisconnectedResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((FullParty) value.getParty()));
                json.put("disconnectedPlayer", value.disconnectedPlayer.toString());
                json.put("timeoutSeconds", value.timeoutSeconds);
                return json.toString();
            }

            @Override
            public PartyMemberDisconnectedResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID disconnectedPlayer = UUID.fromString(jsonObject.getString("disconnectedPlayer"));
                long timeoutSeconds = jsonObject.getLong("timeoutSeconds");
                return new PartyMemberDisconnectedResponseEvent(party, disconnectedPlayer, timeoutSeconds);
            }

            @Override
            public PartyMemberDisconnectedResponseEvent clone(PartyMemberDisconnectedResponseEvent value) {
                return new PartyMemberDisconnectedResponseEvent((FullParty) value.getParty(), value.disconnectedPlayer, value.timeoutSeconds);
            }
        };
    }
}
