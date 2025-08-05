package net.swofty.commons.party.events;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyPlayerSwitchedServerEvent extends PartyEvent {
    private final UUID mover;

    public PartyPlayerSwitchedServerEvent(UUID mover) {
        super(null);
        this.mover = mover;
    }

    public UUID getMover() { return mover; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(mover);
    }

    @Override
    public Serializer<PartyPlayerSwitchedServerEvent> getSerializer() {
        return new Serializer<PartyPlayerSwitchedServerEvent>() {
            @Override
            public String serialize(PartyPlayerSwitchedServerEvent value) {
                JSONObject json = new JSONObject();
                json.put("mover", value.mover.toString());
                return json.toString();
            }

            @Override
            public PartyPlayerSwitchedServerEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID mover = UUID.fromString(jsonObject.getString("mover"));
                return new PartyPlayerSwitchedServerEvent(mover);
            }

            @Override
            public PartyPlayerSwitchedServerEvent clone(PartyPlayerSwitchedServerEvent value) {
                return new PartyPlayerSwitchedServerEvent(value.mover);
            }
        };
    }
}
