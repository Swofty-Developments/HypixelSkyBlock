package net.swofty.commons.party.events;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyAcceptInviteEvent extends PartyEvent {
    private final UUID accepter;
    private final UUID inviter;

    public PartyAcceptInviteEvent(UUID accepter, UUID inviter) {
        super(null);
        this.accepter = accepter;
        this.inviter = inviter;
    }

    public UUID getAccepter() { return accepter; }
    public UUID getInviter() { return inviter; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(accepter, inviter);
    }

    @Override
    public Serializer<PartyAcceptInviteEvent> getSerializer() {
        return new Serializer<PartyAcceptInviteEvent>() {
            @Override
            public String serialize(PartyAcceptInviteEvent value) {
                JSONObject json = new JSONObject();
                json.put("accepter", value.accepter.toString());
                json.put("inviter", value.inviter.toString());
                return json.toString();
            }

            @Override
            public PartyAcceptInviteEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID accepter = UUID.fromString(jsonObject.getString("accepter"));
                UUID inviter = UUID.fromString(jsonObject.getString("inviter"));
                return new PartyAcceptInviteEvent(accepter, inviter);
            }

            @Override
            public PartyAcceptInviteEvent clone(PartyAcceptInviteEvent value) {
                return new PartyAcceptInviteEvent(value.accepter, value.inviter);
            }
        };
    }
}