package net.swofty.commons.party.events;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.party.PendingParty;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyTransferRequestEvent extends PartyEvent {
    private final UUID currentLeader;
    private final UUID newLeader;

    public PartyTransferRequestEvent(UUID currentLeader, UUID newLeader) {
        super(null);
        this.currentLeader = currentLeader;
        this.newLeader = newLeader;
    }

    public UUID getCurrentLeader() { return currentLeader; }
    public UUID getNewLeader() { return newLeader; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(currentLeader, newLeader);
    }

    @Override
    public Serializer<PartyTransferRequestEvent> getSerializer() {
        return new Serializer<PartyTransferRequestEvent>() {
            @Override
            public String serialize(PartyTransferRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("currentLeader", value.currentLeader.toString());
                json.put("newLeader", value.newLeader.toString());
                return json.toString();
            }

            @Override
            public PartyTransferRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID currentLeader = UUID.fromString(jsonObject.getString("currentLeader"));
                UUID newLeader = UUID.fromString(jsonObject.getString("newLeader"));
                return new PartyTransferRequestEvent(currentLeader, newLeader);
            }

            @Override
            public PartyTransferRequestEvent clone(PartyTransferRequestEvent value) {
                return new PartyTransferRequestEvent(value.currentLeader, value.newLeader);
            }
        };
    }
}