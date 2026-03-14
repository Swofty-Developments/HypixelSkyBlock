package net.swofty.commons.guild.events;

import lombok.Getter;
import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class GuildTransferRequestEvent extends GuildEvent {
    private final UUID currentOwner;
    private final UUID newOwner;

    public GuildTransferRequestEvent(UUID currentOwner, UUID newOwner) {
        super(null);
        this.currentOwner = currentOwner;
        this.newOwner = newOwner;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(currentOwner, newOwner);
    }

    @Override
    public Serializer<GuildTransferRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildTransferRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("currentOwner", value.currentOwner.toString());
                json.put("newOwner", value.newOwner.toString());
                return json.toString();
            }

            @Override
            public GuildTransferRequestEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GuildTransferRequestEvent(
                    UUID.fromString(obj.getString("currentOwner")),
                    UUID.fromString(obj.getString("newOwner"))
                );
            }

            @Override
            public GuildTransferRequestEvent clone(GuildTransferRequestEvent value) {
                return new GuildTransferRequestEvent(value.currentOwner, value.newOwner);
            }
        };
    }
}
