package net.swofty.commons.party.events.response;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.Party;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PartyChatMessageResponseEvent extends PartyResponseEvent {
    private final UUID player;
    private final String message;
    private final FullParty party;

    public PartyChatMessageResponseEvent(FullParty party, UUID player, String message) {
        super(party);

        this.player = player;
        this.message = message;
        this.party = party;
    }

    public UUID getPlayer() { return player; }
    public String getMessage() { return message; }
    public FullParty getParty() { return party; }

    @Override
    public Serializer<PartyChatMessageResponseEvent> getSerializer() {
        return new Serializer<PartyChatMessageResponseEvent>() {
            @Override
            public String serialize(PartyChatMessageResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((FullParty) value.getParty()));
                json.put("player", value.player.toString());
                json.put("message", value.message);
                return json.toString();
            }

            @Override
            public PartyChatMessageResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID player = UUID.fromString(jsonObject.getString("player"));
                String message = jsonObject.getString("message");
                return new PartyChatMessageResponseEvent(party, player, message);
            }

            @Override
            public PartyChatMessageResponseEvent clone(PartyChatMessageResponseEvent value) {
                return new PartyChatMessageResponseEvent(value.getParty(), value.player, value.message);
            }
        };
    }
}
